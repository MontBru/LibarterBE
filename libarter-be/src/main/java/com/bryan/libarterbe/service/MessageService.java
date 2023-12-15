package com.bryan.libarterbe.service;

import com.bryan.libarterbe.model.ApplicationUser;
import com.bryan.libarterbe.model.Book;
import com.bryan.libarterbe.model.Conversation;
import com.bryan.libarterbe.model.Message;
import com.bryan.libarterbe.repository.ConversationRepository;
import com.bryan.libarterbe.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    @Autowired
    ConversationRepository conversationRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    BookService bookService;

    @Autowired
    UserService userService;

    public Conversation getConversationByBookAndUser(int bookId, int userId) throws Exception
    {
        Optional<Conversation> conversationOptional = conversationRepository.findConversationByBook_IdAndUser_Id(bookId, userId);
        if(conversationOptional.isPresent())
            return conversationOptional.get();
        else
            throw new Exception("not found");
    }

    public List<Conversation> getAllConversationsOfUser(boolean asClient, int uid)
    {
        List<Conversation> conversations;
        if(asClient)
        {
            conversations = conversationRepository.findConversationsByUser_Id(uid);
        }
        else
        {
            conversations = conversationRepository.findConversationsByBookUser_Id(uid);
        }

        return conversations;
    }

    public Conversation addConversation(int bookId, int userId)
    {
        try {
            Optional<Book> bookOptional = bookService.getBookById(bookId);
            Book book;
            if(bookOptional.isPresent())
                book = bookOptional.get();
            else
                return null;

            ApplicationUser user = userService.getUserById(userId);

            Conversation conversation = new Conversation(book, user);
            conversationRepository.save(conversation);
            return conversation;
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public boolean addMessage(String body, LocalDateTime time, Conversation conversation, int userId)
    {
        try{
            if(userId != conversation.getUser().getId() && userId != conversation.getBook().getUser().getId())
                return false;
            ApplicationUser user = userService.getUserById(userId);
            Message message = new Message(body, time, conversation, user);
            messageRepository.save(message);
            return true;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }


}
