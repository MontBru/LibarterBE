package com.bryan.libarterbe.service;

import com.bryan.libarterbe.DTO.*;
import com.bryan.libarterbe.model.Book;
import com.bryan.libarterbe.model.Tag;
import com.bryan.libarterbe.repository.TagRepository;
import com.nimbusds.jose.shaded.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import com.bryan.libarterbe.repository.BookRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StreamUtils;

import javax.swing.text.html.Option;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserService userService;

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }


    public Optional<Book> getBookById(int id) {
        return bookRepository.findById(id);
    }

//    public byte[] downloadImageAsBytes(String imageUrl) throws IOException {
//        URL url = new URL(imageUrl);
//
//        try (InputStream in = url.openStream()) {
//            byte[] imageBytes = StreamUtils.copyToByteArray(in);
//            return imageBytes;
//        }
//    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public void deleteById(int id) {
        bookRepository.deleteById(id);
    }

    public List<BookDTO> searchSuggestedBooks(BookDTO bookDTO)
    {
        Sort sort = Sort.by(Sort.Order.asc("price"));

        Pageable pageable = PageRequest.of(0, 5, sort);

        Page<Book> bookPage;
        if(bookDTO.isNew())
            bookPage = bookRepository.findBooksByNameContainingAndAuthorContainingAndLanguageContainingAndIsNewIsAndIsRequestIs(bookDTO.getName(), bookDTO.getAuthor(), bookDTO.getLanguage(), bookDTO.isNew(), bookDTO.getIsRequest(), pageable);
        else
            bookPage = bookRepository.findBooksByNameContainingAndAuthorContainingAndLanguageContainingAndIsRequestIs(bookDTO.getName(), bookDTO.getAuthor(), bookDTO.getLanguage(), bookDTO.getIsRequest(), pageable);

        List<BookDTO> bookDTOList = BookDTO.booklistToBookDTOlist(bookPage.getContent());

        return bookDTOList;
    }
    public ResponseEntity<BookPageDTO> searchBooks(SearchBooksDTO body, int searchType, boolean isRequest)
    {
        Pageable pageable = PageRequest.of(body.getPageNum(), 20);
        Page<Book> bookPage;
        if(searchType==1) {
            if (isRequest == false)
                bookPage = bookRepository.findBooksByNameContainingIgnoreCaseAndPriceBetweenAndIsRequestIsFalseOrDescriptionContainingIgnoreCaseAndPriceBetweenAndIsRequestIsFalse(body.getSearchTerm(), body.getMinPrice(), body.getMaxPrice(), body.getSearchTerm(), body.getMinPrice(), body.getMaxPrice(), pageable);
            else
                bookPage = bookRepository.findBooksByNameContainingIgnoreCaseAndPriceBetweenAndIsRequestIsTrueOrDescriptionContainingIgnoreCaseAndPriceBetweenAndIsRequestIsTrue(body.getSearchTerm(), body.getMinPrice(), body.getMaxPrice(), body.getSearchTerm(), body.getMinPrice(), body.getMaxPrice(), pageable);
        }
        else if(searchType == 2)
        {
            if(isRequest == false)
                bookPage = bookRepository.findBooksByAuthorContainingIgnoreCaseAndPriceBetweenAndIsRequestIsFalse(body.getSearchTerm(), body.getMinPrice(), body.getMaxPrice(), pageable);
            else
                bookPage = bookRepository.findBooksByAuthorContainingIgnoreCaseAndPriceBetweenAndIsRequestIsTrue(body.getSearchTerm(), body.getMinPrice(), body.getMaxPrice(), pageable);
        }
        else
        {
            if(isRequest == false)
                bookPage = bookRepository.findBooksByTagsTextContainingIgnoreCaseAndPriceBetweenAndIsRequestIsFalse(body.getSearchTerm(), body.getMinPrice(), body.getMaxPrice(), pageable);
            else
                bookPage = bookRepository.findBooksByTagsTextContainingIgnoreCaseAndPriceBetweenAndIsRequestIsTrue(body.getSearchTerm(), body.getMinPrice(), body.getMaxPrice(), pageable);
        }
        List<BookDTO> bookDTOList = BookDTO.booklistToBookDTOlist(bookPage.getContent());


        return ResponseEntity.ok(new BookPageDTO(bookDTOList, bookPage.getTotalPages()));
    }
    private List<Tag> stringsToTags(List<String> tagStrings)
    {
        List<Tag> tags=new LinkedList<>();
        for (String tag: tagStrings
        ) {
            Tag tagFound = tagRepository.findByText(tag);
            if(tagFound == null)
            {
                tagFound = new Tag(tag);
                tagFound = tagRepository.save(tagFound);
            }
            tags.add(tagFound);
        }
        return tags;
    }

    public Book addBook(BookDTO bookDTO)
    {
        List<Tag> tags=stringsToTags(bookDTO.getTags());
        try {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Book book = new Book(
                    bookDTO.getIsRequest(),
                    bookDTO.getName(),
                    bookDTO.getAuthor(),
                    bookDTO.getDescription(),
                    bookDTO.getPrice(),
                    userService.getUserById(Math.toIntExact(jwt.getClaim("uid"))),
                    //bookDTO.getPhotos(),
                    new LinkedList<>(),
                    bookDTO.isAcceptsTrade(),
                    bookDTO.isNew(),
                    bookDTO.getIsbn(),
                    tags,
                    bookDTO.getPublisher(),
                    bookDTO.getLanguage(),
                    bookDTO.getYearPublished());
            saveBook(book);
            return book;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public Book updateBook(BookDTO bookDTO, int id) throws Exception {
        Optional<Book> existingBookOptional = getBookById(id);

        if (existingBookOptional.isPresent()) {
            List<Tag> tags=stringsToTags(bookDTO.getTags());
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Book existingBook = new Book(
                    id,
                    bookDTO.getIsRequest(),
                    bookDTO.getName(),
                    bookDTO.getAuthor(),
                    bookDTO.getDescription(),
                    bookDTO.getPrice(),
                    userService.getUserById(Math.toIntExact(jwt.getClaim("uid"))),
                    //bookDTO.getPhotos(),
                    new LinkedList<>(),
                    bookDTO.isAcceptsTrade(),
                    bookDTO.isNew(),
                    bookDTO.getIsbn(),
                    tags,
                    bookDTO.getPublisher(),
                    bookDTO.getLanguage(),
                    bookDTO.getYearPublished()
                    );

            saveBook(existingBook);
            return existingBook;
        } else {
            throw new Exception();
        }
    }

    private static HttpURLConnection con;
    public BookInfoDTO getBookByISBN(long isbn) throws Exception {
        String url = "https://openlibrary.org/api/books?bibkeys=ISBN:" + isbn + "&jscmd=data&format=json";

        try {

            URL myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK)
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                String jsonResponse = response.toString();

                Pattern patternRemoveISBN = Pattern.compile("^\\{\"ISBN:\\d+\":(.*)\\}$");

                Matcher matcherRemoveISBN = patternRemoveISBN.matcher(jsonResponse);
                if (matcherRemoveISBN.find()) {
                    jsonResponse = matcherRemoveISBN.group(1);
                }

                Gson gson = new Gson();
                BookAPIResponseDTO bookAPIInfo = gson.fromJson(jsonResponse, BookAPIResponseDTO.class);

                Pattern patternGetYear = Pattern.compile("(\\d{4})");
                Matcher matcherGetYear = patternGetYear.matcher(bookAPIInfo.getPublish_date());

                String yearPublished = "0";
                if(matcherGetYear.find())
                    yearPublished = matcherGetYear.group(1);


                Pattern patternGetName = Pattern.compile("^.*name=([^},\\,]*)[},\\,]");
                Matcher matcherGetAuthorName = patternGetName.matcher(bookAPIInfo.getAuthors().get(0).toString());
                String authorName = "";
                if(matcherGetAuthorName.find())
                {
                    authorName = matcherGetAuthorName.group(1);
                }

                Matcher matcherGetPublisherName = patternGetName.matcher(bookAPIInfo.getPublishers().get(0).toString());
                String publisherName = "";
                if(matcherGetPublisherName.find())
                {
                    publisherName = matcherGetPublisherName.group(1);
                }

                BookInfoDTO bookInfo = new BookInfoDTO(
                        bookAPIInfo.getTitle(),
                        authorName,
                        bookAPIInfo.getSubjects()
                                .stream()
                                .map((subject)->
                                {
                                    Matcher matcherGetSubjectName = patternGetName.matcher(subject.toString());
                                    String subjectName = "";
                                    if(matcherGetSubjectName.find())
                                        subjectName=matcherGetSubjectName.group(1);
                                    return subjectName;
                                })
                                .collect(Collectors.joining(", ")),
                        0,
                        publisherName,
                        "",
                        Integer.parseInt(yearPublished),
                        isbn
                );

                return bookInfo;
            }
            else
            {
                throw new Exception();
            }
        }
        catch (Exception e)
        {
            throw e;
        }
    }
}
