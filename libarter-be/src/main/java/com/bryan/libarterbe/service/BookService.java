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
import java.util.ArrayList;
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

    @Autowired
    private StorageService storageService;

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }


    public Book getBookById(int id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if(!optionalBook.isPresent())
            return null;

        return optionalBook.get();
    }

    public BookDTO getBookDTOById(int id)
    {
        Book book = getBookById(id);
        if(book == null)
            return null;

        BookDTO bookDTO = bookToBookDTO(book);
        return bookDTO;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public void deleteById(int id) {
        Book book = getBookById(id);
        if(book == null)
            return;


        book.getPhotos().forEach((photo)->{
            storageService.deleteResource(photo);
        });
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

        List<BookDTO> bookDTOList = booklistToBookDTOlist(bookPage.getContent());

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
        List<BookDTO> bookDTOList = booklistToBookDTOlist(bookPage.getContent());

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
            int uid = Math.toIntExact(jwt.getClaim("uid"));

            List<String> photos = new LinkedList<>();

            for(int i = 0; i<bookDTO.getPhotos().size();i++)
            {
                String filename = storageService.generateFilename(uid, i);
                storageService.writeResource(filename, bookDTO.getPhotos().get(i));
                photos.add(filename);
            }

            Book book = new Book(
                    bookDTO.getIsRequest(),
                    bookDTO.getName(),
                    bookDTO.getAuthor(),
                    bookDTO.getDescription(),
                    bookDTO.getPrice(),
                    userService.getUserById(Math.toIntExact(jwt.getClaim("uid"))),
                    photos,
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
        Book existingBook = getBookById(id);

        if(existingBook == null)
            throw new Exception();

        List<Tag> tags=stringsToTags(bookDTO.getTags());
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int uid = Math.toIntExact(jwt.getClaim("uid"));

        if(uid != bookDTO.getUserId() || uid != existingBook.getUser().getId())
            return null;

        existingBook.getPhotos().forEach((photo)->{
            storageService.deleteResource(photo);
        });

        List<String> photos = new LinkedList<>();

        for(int i = 0; i<bookDTO.getPhotos().size();i++)
        {
            System.out.println(bookDTO.getPhotos().get(i));
            String filename = storageService.generateFilename(uid, i);
            storageService.writeResource(filename, bookDTO.getPhotos().get(i));
            photos.add(filename);
        }

        existingBook = new Book(
                id,
                bookDTO.getIsRequest(),
                bookDTO.getName(),
                bookDTO.getAuthor(),
                bookDTO.getDescription(),
                bookDTO.getPrice(),
                userService.getUserById(Math.toIntExact(jwt.getClaim("uid"))),
                photos,
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

    public BookDTO bookToBookDTO(Book book) {
        List<String> photos = new LinkedList<>();
        book.getPhotos().forEach((photo)->{
            photos.add(storageService.readResource(photo));
        });
        return new BookDTO(
                book.getId(),
                book.getIsRequest(),
                book.getName(),
                book.getAuthor(),
                book.getDescription(),
                book.getPrice(),
                book.getUser().getId(),
                photos,
                book.isAcceptsTrade(),
                book.isNew(),
                book.getIsbn(),
                book.getTags().stream().map((Tag tag) ->{ return tag.getText();}).collect(Collectors.toList()),
                book.getPublisher(),
                book.getLanguage(),
                book.getYearPublished()
        );
    }

    public List<BookDTO> booklistToBookDTOlist(List<Book> books)
    {
        return books.stream()
                .map(this::bookToBookDTO)
                .collect(Collectors.toList());
    }
}
