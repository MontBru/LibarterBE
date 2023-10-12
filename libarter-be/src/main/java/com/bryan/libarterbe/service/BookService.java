package com.bryan.libarterbe.service;

import com.bryan.libarterbe.DTO.BookAPIResponseDTO;
import com.bryan.libarterbe.DTO.BookDTO;
import com.bryan.libarterbe.DTO.BookInfoDTO;
import com.bryan.libarterbe.model.Book;
import com.bryan.libarterbe.model.Tag;
import com.bryan.libarterbe.repository.TagRepository;
import com.nimbusds.jose.shaded.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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

    public byte[] downloadImageAsBytes(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);

        try (InputStream in = url.openStream()) {
            byte[] imageBytes = StreamUtils.copyToByteArray(in);
            return imageBytes;
        }
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public void deleteById(int id) {
        bookRepository.deleteById(id);
    }

    public Page<Book> getBooksBySearch(String searchTerm, double max, double min, Pageable pageable)
    {
        return bookRepository.findBooksByNameContainingIgnoreCaseAndPriceBetweenOrDescriptionContainingIgnoreCaseAndPriceBetween(searchTerm, min, max,searchTerm, min, max,pageable);
    }

    public Page<Book> getBookByAuthorSearch(String searchTerm, double max, double min, Pageable pageable)
    {
        return bookRepository.findBooksByAuthorContainingIgnoreCaseAndPriceBetween(searchTerm, min, max,pageable);
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
            Book book = new Book(
                    bookDTO.getName(),
                    bookDTO.getAuthor(),
                    bookDTO.getDescription(),
                    bookDTO.getPrice(),
                    userService.getUserById(bookDTO.getUserId()),
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
            System.out.println(bookDTO.getUserId());
            Book existingBook = new Book(
                    id,
                    bookDTO.getName(),
                    bookDTO.getAuthor(),
                    bookDTO.getDescription(),
                    bookDTO.getPrice(),
                    userService.getUserById(bookDTO.getUserId()),
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

    public Page<Book> getBookByTagSearch(String searchTerm, double max, double min, Pageable pageable)
    {
        return bookRepository.findBooksByTagsTextContainingIgnoreCaseAndPriceBetween(searchTerm, min, max, pageable);
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
                    System.out.println("found");
                    jsonResponse = matcherRemoveISBN.group(1);
                }

                Gson gson = new Gson();
                BookAPIResponseDTO bookAPIInfo = gson.fromJson(jsonResponse, BookAPIResponseDTO.class);

                Pattern patternGetYear = Pattern.compile("^.*\\D(\\d+)$");
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
