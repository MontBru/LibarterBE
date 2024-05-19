package com.bryan.libarterbe;

import com.bryan.libarterbe.DTO.BookInfoDTO;
import com.bryan.libarterbe.repository.BookRepository;
import com.bryan.libarterbe.repository.TagRepository;
import com.bryan.libarterbe.service.BookService;
import com.bryan.libarterbe.service.StorageService;
import com.bryan.libarterbe.service.UserService;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ISBNIntegrationTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private UserService userService;

    @Mock
    private StorageService storageService;

    BookService bookService = new BookService(bookRepository, tagRepository, userService, storageService);
    @Test
    public void testGetBookByISBN() throws Exception {
        long isbn = 9788175257665L;

        BookInfoDTO bookInfo = bookService.getBookByISBN(isbn);

        assertNotNull(bookInfo);
        assertEquals("Colonialism and its effect on literature", bookInfo.name());
        assertEquals("Nighat Ahmed", bookInfo.author());
    }
}
