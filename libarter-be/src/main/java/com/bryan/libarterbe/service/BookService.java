package com.bryan.libarterbe.service;

import com.bryan.libarterbe.DTO.*;
import com.bryan.libarterbe.model.Book;
import com.bryan.libarterbe.model.Tag;
import com.bryan.libarterbe.repository.TagRepository;
import com.bryan.libarterbe.utils.JwtUtility;
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
import java.time.LocalDate;
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
        return bookRepository.findById(id).orElse(null);
    }

    public void deleteById(int id) throws Exception{
        try {
            Book book = getBookById(id);
            if (book == null)
                throw new Exception("Book not found");

            book.getPhotos().forEach((photo) -> {
                storageService.deleteResource(photo);
            });

            book.getTags().forEach((tag)->{
                if(tagRepository.hasBooks(tag.getId()))
                    tagRepository.deleteById(tag.getId());
            });
            book.setTags(new LinkedList<>());
            bookRepository.deleteById(id);
        }catch (Exception e)
        {
            throw new Exception("Internal error");
        }
    }

    public BookDTO getBookDTOById(int id)
    {
        Book book = getBookById(id);
        if(book == null)
            return null;

        BookDTO bookDTO = bookToBookDTO(book);
        return bookDTO;
    }

    public List<BookDTO> searchSuggestedBooks(BookDTO bookDTO)
    {
        Sort sort;
        if(bookDTO.getIsRequest())
            sort = Sort.by(Sort.Order.asc("price"));
        else
            sort = Sort.by(Sort.Order.desc("price"));

        Pageable pageable = PageRequest.of(0, 5, sort);

        Page<Book> bookPage;
        if(bookDTO.isNew())
            bookPage = bookRepository.findBooksByNameContainingAndAuthorContainingAndLanguageContainingAndIsNewIsAndIsRequestIs(bookDTO.getName(), bookDTO.getAuthor(), bookDTO.getLanguage(), bookDTO.isNew(), !bookDTO.getIsRequest(), pageable);
        else
            bookPage = bookRepository.findBooksByNameContainingAndAuthorContainingAndLanguageContainingAndIsRequestIs(bookDTO.getName(), bookDTO.getAuthor(), bookDTO.getLanguage(), !bookDTO.getIsRequest(), pageable);

        List<BookDTO> bookDTOList = booklistToBookDTOlist(bookPage.getContent());

        return bookDTOList;
    }
    public ResponseEntity<BookPageDTO> searchBooks(SearchBooksDTO body, int searchType, boolean isRequest)
    {
        try {
            Pageable pageable = PageRequest.of(body.getPageNum(), 20);
            Page<Book> bookPage;
            if (searchType == 1) {
                if (isRequest == false)
                    bookPage = bookRepository.findBooksByNameContainingIgnoreCaseAndPriceBetweenAndIsRequestIsFalseOrDescriptionContainingIgnoreCaseAndPriceBetweenAndIsRequestIsFalse(body.getSearchTerm(), body.getMinPrice(), body.getMaxPrice(), body.getSearchTerm(), body.getMinPrice(), body.getMaxPrice(), pageable);
                else
                    bookPage = bookRepository.findBooksByNameContainingIgnoreCaseAndPriceBetweenAndIsRequestIsTrueOrDescriptionContainingIgnoreCaseAndPriceBetweenAndIsRequestIsTrue(body.getSearchTerm(), body.getMinPrice(), body.getMaxPrice(), body.getSearchTerm(), body.getMinPrice(), body.getMaxPrice(), pageable);
            } else if (searchType == 2) {
                if (isRequest == false)
                    bookPage = bookRepository.findBooksByAuthorContainingIgnoreCaseAndPriceBetweenAndIsRequestIsFalse(body.getSearchTerm(), body.getMinPrice(), body.getMaxPrice(), pageable);
                else
                    bookPage = bookRepository.findBooksByAuthorContainingIgnoreCaseAndPriceBetweenAndIsRequestIsTrue(body.getSearchTerm(), body.getMinPrice(), body.getMaxPrice(), pageable);
            } else {
                if (isRequest == false)
                    bookPage = bookRepository.findBooksByTagsTextContainingIgnoreCaseAndPriceBetweenAndIsRequestIsFalse(body.getSearchTerm(), body.getMinPrice(), body.getMaxPrice(), pageable);
                else
                    bookPage = bookRepository.findBooksByTagsTextContainingIgnoreCaseAndPriceBetweenAndIsRequestIsTrue(body.getSearchTerm(), body.getMinPrice(), body.getMaxPrice(), pageable);
            }
            List<BookDTO> bookDTOList = booklistToBookDTOlist(bookPage.getContent());

            return ResponseEntity.ok(new BookPageDTO(bookDTOList, bookPage.getTotalPages()));
        }
        catch (Exception e)
        {
            return ResponseEntity.internalServerError().build();
        }
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

    private List<String> addPhotosToStorage(List<String> photos, int uid)
    {
        List<String> res = new LinkedList<>();
        for(int i = 0; i<photos.size();i++)
        {
            String filename = storageService.generateFilename(uid, i);
            storageService.writeResource(filename, photos.get(i));
            res.add(filename);
        }
        return res;
    }

    public Book addBook(BookDTO bookDTO) throws Exception
    {
        List<Tag> tags=stringsToTags(bookDTO.getTags());
        try {
            int uid = JwtUtility.getUid();

            List<String> photos = addPhotosToStorage(bookDTO.getPhotos(), bookDTO.getUserId());

            Book book = new Book(
                    bookDTO.getIsRequest(),
                    bookDTO.getName(),
                    bookDTO.getAuthor(),
                    bookDTO.getDescription(),
                    bookDTO.getPrice(),
                    userService.getUserById(Math.toIntExact(uid)),
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
            throw new Exception("Internal error");
        }

    }

    public Book updateBook(BookDTO bookDTO, int id) throws Exception {
        Book existingBook = getBookById(id);

        if(existingBook == null)
            throw new Exception();

        int uid = JwtUtility.getUid();

        if(uid != existingBook.getUser().getId())
            throw new Exception();

        List<Tag> tags=stringsToTags(bookDTO.getTags());

        existingBook.getPhotos().forEach((photo)->{
            storageService.deleteResource(photo);
        });

        List<String> photos = addPhotosToStorage(bookDTO.getPhotos(), bookDTO.getUserId());

        existingBook = new Book(
                id,
                bookDTO.getIsRequest(),
                bookDTO.getName(),
                bookDTO.getAuthor(),
                bookDTO.getDescription(),
                bookDTO.getPrice(),
                userService.getUserById(uid),
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

    private String getJsonResponse(InputStream inputStream) throws Exception
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }

    private int getYearFromAPIReturnedYear(String year)
    {
        try {
            LocalDate date = LocalDate.parse(year);
            return date.getYear();
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    private String getNameWithPattern(String rawData, Pattern pattern)
    {
        Matcher matcherGetAuthorName = pattern.matcher(rawData);
        if(matcherGetAuthorName.find())
        {
            return matcherGetAuthorName.group(1);
        }
        return "";
    }

    public BookInfoDTO getBookByISBN(long isbn) throws Exception {
        String url = "https://openlibrary.org/api/books?bibkeys=ISBN:" + isbn + "&jscmd=data&format=json";

        URL myurl = new URL(url);
        con = (HttpURLConnection) myurl.openConnection();
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();

        if(responseCode != HttpURLConnection.HTTP_OK)
        {
            throw new Exception();
        }

        String jsonResponse = getJsonResponse(con.getInputStream());

        //remove useless data and standardise the return
        Pattern patternRemoveISBN = Pattern.compile("^\\{\"ISBN:\\d+\":(.*)\\}$");
        Matcher matcherRemoveISBN = patternRemoveISBN.matcher(jsonResponse);
        if (matcherRemoveISBN.find()) {
            jsonResponse = matcherRemoveISBN.group(1);
        }

        //read json data into class
        Gson gson = new Gson();
        BookAPIResponseDTO bookAPIInfo = gson.fromJson(jsonResponse, BookAPIResponseDTO.class);

        //try to get year else return 0
        int yearPublished = getYearFromAPIReturnedYear(bookAPIInfo.getPublish_date());

        Pattern patternGetName = Pattern.compile("^.*name=([^},\\,]*)[},\\,]");

        String authorName = getNameWithPattern(bookAPIInfo.getAuthors().get(0).toString(), patternGetName);
        String publisherName = getNameWithPattern(bookAPIInfo.getPublishers().get(0).toString(), patternGetName);

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
            yearPublished,
            isbn
        );

        return bookInfo;
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
                book.getTags().stream().map((Tag tag) -> tag.getText()).collect(Collectors.toList()),
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

    public List<Book> filterBooksByRequest(boolean request, List<Book> books)
    {
        return books
            .stream()
            .filter((Book b)->b.getIsRequest()==request)
            .collect(Collectors.toList());
    }
}
