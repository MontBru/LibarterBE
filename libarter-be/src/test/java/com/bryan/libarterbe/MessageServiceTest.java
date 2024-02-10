package com.bryan.libarterbe;

import com.bryan.libarterbe.model.ApplicationUser;
import com.bryan.libarterbe.model.Book;
import com.bryan.libarterbe.model.Conversation;
import com.bryan.libarterbe.model.Message;
import com.bryan.libarterbe.repository.ConversationRepository;
import com.bryan.libarterbe.repository.MessageRepository;

import com.bryan.libarterbe.service.BookService;
import com.bryan.libarterbe.service.MessageService;
import com.bryan.libarterbe.service.UserService;
import com.bryan.libarterbe.utils.JwtUtility;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MessageServiceTest {
    public MessageServiceTest() {
    }

    @Mock
    private ConversationRepository conversationRepository;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private BookService bookService;

    @Mock
    private UserService userService;

    @InjectMocks
    private MessageService messageService;

    static private Conversation conversation;
    static private ApplicationUser user;
    static private Book book;

    static private ApplicationUser user2;
    static private Conversation conversation2;

    @BeforeClass
    public static void setUp() {
        user = new ApplicationUser();
        user.setId(1);
        user.setUsername("TestUser");

        user2 = new ApplicationUser();
        user2.setId(1);
        user2.setUsername("TestUser2");

        book = new Book();
        book.setId(1);
        book.setUser(user);

        Book book2 = new Book();
        book2.setId(2);
        book2.setUser(user2);

        conversation = new Conversation();
        conversation.setId(1);
        conversation.setBook(book);
        conversation.setUser(user);

        conversation2 = new Conversation();
        conversation2.setId(2);
        conversation2.setBook(book);
        conversation2.setUser(user2);
    }

    @Test
    public void testGetConversationByBookAndUser() throws Exception {
        when(conversationRepository.findConversationByBook_IdAndUser_Id(1, 1)).thenReturn(Optional.of(conversation));

        Conversation result = messageService.getConversationByBookAndUser(1, 1);

        assertEquals(1, result.getId());
    }

    @Test
    public void testGetAllConversationsOfUser() {
        when(conversationRepository.findConversationsByUser_Id(1)).thenReturn(List.of(conversation));
        MockedStatic<JwtUtility> utilityMockedStatic = mockStatic(JwtUtility.class);
        when(JwtUtility.getUid()).thenReturn(1);

        List<Conversation> result = messageService.getAllConversationsOfUser(true);

        utilityMockedStatic.close();
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    public void test2GetAllConversationsOfUser() {
        when(conversationRepository.findConversationsByBook_User_Id(1)).thenReturn(List.of(conversation2));
        MockedStatic<JwtUtility> utilityMockedStatic = mockStatic(JwtUtility.class);
        when(JwtUtility.getUid()).thenReturn(1);

        List<Conversation> result = messageService.getAllConversationsOfUser(false);

        utilityMockedStatic.close();
        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getId());
    }

    @Test
    public void testAddConversation_Success() {
        when(bookService.getBookById(1)).thenReturn(book);
        when(userService.getUserById(1)).thenReturn(user);

        Conversation savedConversation = new Conversation(book, user);
        when(conversationRepository.save(any(Conversation.class))).thenReturn(savedConversation);

        Conversation result = messageService.addConversation(1, 1);

        assertEquals(savedConversation.getId(), result.getId());
        verify(conversationRepository, times(1)).save(any(Conversation.class));
    }

    @Test
    public void testAddConversation_BookNotFound() {
        when(bookService.getBookById(1)).thenReturn(null);

        Conversation result = messageService.addConversation(1, 1);

        assertNull(result);
        verify(conversationRepository, never()).save(any(Conversation.class));
    }

    @Test
    public void testAddConversation_UserNotFound() {
        when(bookService.getBookById(1)).thenReturn(book);
        when(userService.getUserById(1)).thenReturn(null);

        Conversation result = messageService.addConversation(1, 1);

        assertNull(result);
        verify(conversationRepository, never()).save(any(Conversation.class));
    }

    @Test
    public void testAddMessage_Success() {
        when(userService.getUserById(1)).thenReturn(user);

        LocalDateTime time = LocalDateTime.now();

        Message savedMessage = new Message("Hello", time, conversation2, user);

        when(messageRepository.save(any(Message.class))).thenReturn(savedMessage);

        Message message = messageService.addMessage("Hello", time, conversation2, 1);

        assertEquals(savedMessage.getId(), message.getId());
        verify(messageRepository, times(1)).save(message);
    }

    @Test
    public void testAddMessage_UserNotFromConversation() {
        LocalDateTime time = LocalDateTime.now();

        Message message = messageService.addMessage("Hello", time, conversation2, 2);

        assertNull(message);
        verify(messageRepository, never()).save(any());
    }

    @Test
    public void getConversationById() {
        when(conversationRepository.findById(1)).thenReturn(Optional.of(conversation));

        Conversation res = messageService.getConversationById(1);

        assertEquals(conversation.getId(), res.getId());
    }

    @Test
    public void getConversationById_Fail() {
        when(conversationRepository.findById(3)).thenReturn(Optional.empty());

        Conversation res = messageService.getConversationById(3);

        assertNull(res);
    }

}