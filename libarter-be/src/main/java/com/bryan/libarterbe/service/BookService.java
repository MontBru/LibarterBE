package com.bryan.libarterbe.service;

import com.bryan.libarterbe.DTO.BookDTO;
import com.bryan.libarterbe.model.Book;
import com.bryan.libarterbe.model.Tag;
import com.bryan.libarterbe.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.bryan.libarterbe.repository.BookRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StreamUtils;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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

//    public List<Tag> stringListToTagList(List<String> stringList)
//    {
//        return stringList.stream().map(element ->{
//            return new Tag(element);
//        }).toList();
//    }

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
                    tags);
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
                    tags
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
}
