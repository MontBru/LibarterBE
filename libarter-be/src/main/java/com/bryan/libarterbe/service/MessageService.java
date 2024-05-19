package com.bryan.libarterbe.service;

import com.bryan.libarterbe.DTO.ConversationDTO;
import com.bryan.libarterbe.DTO.MessageDTO;
import com.bryan.libarterbe.model.ApplicationUser;
import com.bryan.libarterbe.model.Book;
import com.bryan.libarterbe.model.Conversation;
import com.bryan.libarterbe.model.Message;
import com.bryan.libarterbe.repository.ConversationRepository;
import com.bryan.libarterbe.repository.MessageRepository;
import com.bryan.libarterbe.utils.JwtUtility;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageService {
    final
    ConversationRepository conversationRepository;

    final
    MessageRepository messageRepository;

    final
    BookService bookService;

    final
    UserService userService;

    final
    StorageService storageService;

    public MessageService(ConversationRepository conversationRepository, MessageRepository messageRepository, BookService bookService, UserService userService, StorageService storageService) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.bookService = bookService;
        this.userService = userService;
        this.storageService = storageService;
    }

    public void deleteMessageById(int id)
    {
        messageRepository.deleteById(id);
    }

    public void deleteConversationById(int id)
    {
        conversationRepository.deleteById(id);
    }

    public Conversation getConversationByBookAndUser(int bookId, int userId) throws Exception
    {
        Optional<Conversation> conversationOptional = conversationRepository.findConversationByBook_IdAndUser_Id(bookId, userId);
        if(conversationOptional.isPresent())
            return conversationOptional.get();
        else
            throw new Exception("not found");
    }

    public boolean isUserInConversation(Conversation conversation, int uid)
    {
        return !(conversation == null || (conversation.getUser().getId() != uid && conversation.getBook().getUser().getId() != uid));
    }

    public List<Conversation> getAllConversationsOfUser(boolean asClient)
    {
        int uid = JwtUtility.getUid();

        List<Conversation> conversations;

        if(asClient)
        {
            conversations = conversationRepository.findConversationsByUser_Id(uid);
        }
        else
        {
            conversations = conversationRepository.findConversationsByBook_User_Id(uid);
        }

        return conversations;
    }

    public Conversation addConversation(int bookId, int userId)
    {
        Book book = bookService.getBookById(bookId);
        if(book == null)
            return null;

        ApplicationUser user = userService.getUserById(userId);
        if(user == null)
            return null;

        Conversation conversation = new Conversation(book, user);
        conversationRepository.save(conversation);
        return conversation;
    }

    public Message addMessage(String body, LocalDateTime time, Conversation conversation, int userId)
    {
        if(!isUserInConversation(conversation, userId))
            return null;
        ApplicationUser user = userService.getUserById(userId);
        if(user == null)
            return null;
        Message message = new Message(body, time, conversation, user);
        messageRepository.save(message);
        return message;
    }

    public Conversation getConversationById(int id)
    {
        Optional<Conversation> conversationOptional = conversationRepository.findById(id);
        return conversationOptional.orElse(null);
    }

    public Page<Message> getMessagesByConversation(int conversationId, int pageNum, int messagesPP)
    {
        Sort sort = Sort.by(Sort.Order.desc("time"));

        Pageable pageable = PageRequest.of(pageNum, messagesPP, sort);

        return messageRepository.getMessagesByConversation_Id(conversationId, pageable);
    }

    public MessageDTO messageToMessageDTO(Message message, int uid)
    {
        boolean you = false;
        if(uid == message.getUser().getId())
            you = true;
        return new MessageDTO(message.getBody(), you, message.getTime(), message.getUser().getUsername(), message.getId());
    }

    public List<MessageDTO> messageListToMessageDTOList(List<Message> messageList)
    {
        int uid = JwtUtility.getUid();
        return messageList.stream().map(message -> messageToMessageDTO(message, uid)).toList();
    }

    public ConversationDTO conversationToConversationDTO(Conversation conversation)
    {
        List<Message> lastMessageList = getMessagesByConversation(conversation.getId(), 0, 1).getContent();
        MessageDTO lastMessage;
        int uid = JwtUtility.getUid();
        if(lastMessageList.size() == 0)
            lastMessage = null;
        else
            lastMessage = messageToMessageDTO(lastMessageList.get(0), uid);

        String displayName;
        if(uid == conversation.getUser().getId())
            displayName = conversation.getBook().getUser().getUsername();
        else
            displayName = conversation.getUser().getUsername();

        return new ConversationDTO(conversation.getId(), conversation.getBook().getName(), displayName,
                conversation.getBook().getPhotos().get(0),
                lastMessage);
    }

    public List<ConversationDTO> conversationListToConversationDTOList(List<Conversation> conversationList)
    {
        return conversationList.stream().map(conversation -> conversationToConversationDTO(conversation)).filter((conversation)->conversation.lastMessage()!=null).sorted((conversation1, conversation2)->-conversation1.lastMessage().time().compareTo(conversation2.lastMessage().time())).collect(Collectors.toList());
    }


}
