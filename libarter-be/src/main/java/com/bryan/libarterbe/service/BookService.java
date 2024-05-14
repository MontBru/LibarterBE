package com.bryan.libarterbe.service;

import com.bryan.libarterbe.DTO.*;
import com.bryan.libarterbe.model.ApplicationUser;
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

import javax.imageio.ImageIO;
import javax.swing.text.html.Option;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class BookService {


    public BookService(BookRepository bookRepository, TagRepository tagRepository, UserService userService, StorageService storageService) {
        this.bookRepository = bookRepository;
        this.tagRepository = tagRepository;
        this.userService = userService;
        this.storageService = storageService;
    }

    private final BookRepository bookRepository;


    private final TagRepository tagRepository;


    private final UserService userService;


    private final StorageService storageService;

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
        if(bookDTO.isRequest())
            sort = Sort.by(Sort.Order.asc("price"));
        else
            sort = Sort.by(Sort.Order.desc("price"));

        Pageable pageable = PageRequest.of(0, 5, sort);

        Page<Book> bookPage;
        if(bookDTO.isNew())
            bookPage = bookRepository.findBooksByNameContainingAndAuthorContainingAndLanguageContainingAndIsNewIsAndIsRequestIs(bookDTO.name(), bookDTO.author(), bookDTO.language(), bookDTO.isNew(), !bookDTO.isRequest(), pageable);
        else
            bookPage = bookRepository.findBooksByNameContainingAndAuthorContainingAndLanguageContainingAndIsRequestIs(bookDTO.name(), bookDTO.author(), bookDTO.language(), !bookDTO.isRequest(), pageable);

        List<BookDTO> bookDTOList = booklistToBookDTOlist(bookPage.getContent());

        return bookDTOList;
    }
    public ResponseEntity<BookPageDTO> searchBooks(SearchBooksDTO body, boolean isRequest)
    {
        try {
            Pageable pageable = PageRequest.of(body.pageNum(), 20);
            Page<Book> bookPage;
            if (body.searchType() == 1) {
                if (isRequest == false)
                    bookPage = bookRepository.findBooksByNameContainingIgnoreCaseAndPriceBetweenAndIsRequestIsFalseOrDescriptionContainingIgnoreCaseAndPriceBetweenAndIsRequestIsFalse(body.searchTerm(), body.minPrice(), body.maxPrice(), body.searchTerm(), body.minPrice(), body.maxPrice(), pageable);
                else
                    bookPage = bookRepository.findBooksByNameContainingIgnoreCaseAndPriceBetweenAndIsRequestIsTrueOrDescriptionContainingIgnoreCaseAndPriceBetweenAndIsRequestIsTrue(body.searchTerm(), body.minPrice(), body.maxPrice(), body.searchTerm(), body.minPrice(), body.maxPrice(), pageable);
            } else if (body.searchType() == 2) {
                if (isRequest == false)
                    bookPage = bookRepository.findBooksByAuthorContainingIgnoreCaseAndPriceBetweenAndIsRequestIsFalse(body.searchTerm(), body.minPrice(), body.maxPrice(), pageable);
                else
                    bookPage = bookRepository.findBooksByAuthorContainingIgnoreCaseAndPriceBetweenAndIsRequestIsTrue(body.searchTerm(), body.minPrice(), body.maxPrice(), pageable);
            } else {
                if (isRequest == false)
                    bookPage = bookRepository.findBooksByTagsTextContainingIgnoreCaseAndPriceBetweenAndIsRequestIsFalse(body.searchTerm(), body.minPrice(), body.maxPrice(), pageable);
                else
                    bookPage = bookRepository.findBooksByTagsTextContainingIgnoreCaseAndPriceBetweenAndIsRequestIsTrue(body.searchTerm(), body.minPrice(), body.maxPrice(), pageable);
            }
            List<BookDTO> bookDTOList = booklistToBookCardDTOlist(bookPage.getContent());

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



    private String compressImage(String image, boolean isThumbnail) {
        //convert image to BufferedImage
        int commaIndex = image.indexOf(",");
        if (commaIndex != -1)
            image = image.substring(commaIndex + 1);


//        image = image.replace("data:image/png;base64,", "");
        System.out.println(image.substring(0,70));

        byte[] imageBytes = Base64.getDecoder().decode(image);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
        BufferedImage originalImage;
        try {
            originalImage = ImageIO.read(inputStream);
        }catch (Exception e)
        {
            return "";
        }

        //get min and max sizes
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        int maxSize;
        int minSize = 1;

        if(isThumbnail)
            maxSize = 400;
        else
            maxSize = 1300;

        //get new sizes for image
        int newHeight = originalHeight;
        int newWidth = originalWidth;

        if(originalHeight > maxSize || originalWidth > maxSize){
            if(originalHeight>originalWidth)
            {
                newHeight = maxSize;
                newWidth = newHeight * originalWidth / originalHeight;
                if(newWidth < minSize)
                    newWidth = minSize;
            }
            else
            {
                newWidth = maxSize;
                newHeight = newWidth * originalHeight / originalWidth;
                if(newHeight < minSize)
                    newHeight = minSize;
            }
        }

        //compress image
        Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        //convert Image to Base64
        BufferedImage bufferedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(resizedImage, 0, 0, newWidth, newHeight, null);
        g2d.dispose();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
        } catch (IOException e) {
            return "";
        }
        byte[] compressedImgBytes = byteArrayOutputStream.toByteArray();
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(compressedImgBytes);
    }

    private List<String> addPhotosToStorage(List<String> photos, int uid)
    {
        List<String> res = new LinkedList<>();
        if(photos.isEmpty())
        {
            return res;
        }
        String thumbnailPhoto = photos.get(0);
        thumbnailPhoto = compressImage(thumbnailPhoto, true);
        photos.add(0, thumbnailPhoto);
        for(int i = 0; i<photos.size();i++)
        {
            String filename = storageService.generateFilename(uid, i);
            System.out.println(filename);
            storageService.writeResource(filename, compressImage(photos.get(i), false));
            res.add(filename);
        }
        return res;
    }

    public Book addBook(BookDTO bookDTO) throws Exception
    {
        List<Tag> tags=stringsToTags(bookDTO.tags());
        try {
            int uid = JwtUtility.getUid();

            ApplicationUser user = userService.getUserById(uid);
            if(user.getBooks().size() >= 20)
                throw new Exception("User can't add more books");

            List<String> photos = addPhotosToStorage(bookDTO.photos(), bookDTO.userId());

            Book book = new Book(
                    bookDTO.isRequest(),
                    bookDTO.name(),
                    bookDTO.author(),
                    bookDTO.description(),
                    bookDTO.price(),
                    userService.getUserById(Math.toIntExact(uid)),
                    photos,
                    bookDTO.acceptsTrade(),
                    bookDTO.isNew(),
                    bookDTO.isbn(),
                    tags,
                    bookDTO.publisher(),
                    bookDTO.language(),
                    bookDTO.yearPublished());

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

        List<Tag> tags=stringsToTags(bookDTO.tags());

        existingBook.getPhotos().forEach((photo)->{
            storageService.deleteResource(photo);
        });

        List<String> photos = addPhotosToStorage(bookDTO.photos(), bookDTO.userId());

        existingBook = new Book(
                id,
                bookDTO.isRequest(),
                bookDTO.name(),
                bookDTO.author(),
                bookDTO.description(),
                bookDTO.price(),
                userService.getUserById(uid),
                photos,
                bookDTO.acceptsTrade(),
                bookDTO.isNew(),
                bookDTO.isbn(),
                tags,
                bookDTO.publisher(),
                bookDTO.language(),
                bookDTO.yearPublished()
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
        int yearPublished = getYearFromAPIReturnedYear(bookAPIInfo.publish_date());

        Pattern patternGetName = Pattern.compile("^.*name=([^},\\,]*)[},\\,]");

        String authorName = getNameWithPattern(bookAPIInfo.authors().get(0).toString(), patternGetName);
        String publisherName = getNameWithPattern(bookAPIInfo.publishers().get(0).toString(), patternGetName);

        BookInfoDTO bookInfo = new BookInfoDTO(
            bookAPIInfo.title(),
            authorName,
            bookAPIInfo.subjects()
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

    public BookDTO bookToBookCardDTO(Book book){
//        List<String> photos = new LinkedList<>();
//        List<String> photoLinks = book.getPhotos();
//        if(!photoLinks.isEmpty())
//            photos.add(storageService.readResource(photoLinks.get(0)));
        return new BookDTO(
                book.getId(),
                book.getIsRequest(),
                book.getName(),
                book.getAuthor(),
                book.getDescription(),
//                photos,
                book.getPhotos(),
                book.isAcceptsTrade(),
                book.isNew(),
                book.getPrice(),
                book.getUser().getId(),
                book.getIsbn(),
                book.getTags().stream().map((Tag tag) -> tag.getText()).collect(Collectors.toList()),
                book.getPublisher(),
                book.getLanguage(),
                book.getYearPublished()
        );
    }

    public BookDTO bookToBookDTO(Book book) {
//        List<String> photos = new LinkedList<>();
//        List<String> photoLinks = book.getPhotos();
//        photoLinks.forEach((photo)->{
//            photos.add(storageService.readResource(photo));
//        });

        return new BookDTO(
                book.getId(),
                book.getIsRequest(),
                book.getName(),
                book.getAuthor(),
                book.getDescription(),
                book.getPhotos(),
//                photos,
                book.isAcceptsTrade(),
                book.isNew(),
                book.getPrice(),
                book.getUser().getId(),
                book.getIsbn(),
                book.getTags().stream().map((Tag tag) -> tag.getText()).collect(Collectors.toList()),
                book.getPublisher(),
                book.getLanguage(),
                book.getYearPublished()
        );
    }

    public List<BookDTO> booklistToBookCardDTOlist(List<Book> books)
    {
        return books.stream()
                .map(this::bookToBookCardDTO)
                .collect(Collectors.toList());
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
