package com.bryan.libarterbe.controller.User;

import com.bryan.libarterbe.DTO.*;
import com.bryan.libarterbe.model.Conversation;
import com.bryan.libarterbe.model.Message;
import com.bryan.libarterbe.service.MessageService;
import com.bryan.libarterbe.utils.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/user/message")
public class MessageController {

    @Autowired
    MessageService messageService;

    @GetMapping("/getConversations/{asClient}")
    public ResponseEntity<List<ConversationDTO>> getConversations(@PathVariable boolean asClient)
    {
        List<Conversation> conversations = messageService.getAllConversationsOfUser(asClient);
        return ResponseEntity.ok(messageService.conversationListToConversationDTOList(conversations));
    }

    @PostMapping("/addConversation/{offerId}")
    public ResponseEntity<Integer> addConversation(@PathVariable int offerId)
    {
        int uid = JwtUtility.getUid();
        Conversation conversation;

        try {
            conversation = messageService.getConversationByBookAndUser(offerId, uid);
        } catch (Exception e) {
            conversation = messageService.addConversation(offerId, uid);
        }
        return ResponseEntity.ok(conversation.getId());
    }

    @PostMapping("/addMessage")
    public ResponseEntity<MessageDTO> addMessage(@RequestBody MessageCreateDTO messageDTO)
    {
        int uid = JwtUtility.getUid();
        Conversation conversation;
        try {
            conversation = messageService.getConversationById(messageDTO.getConversationId());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
        Message response = messageService.addMessage(messageDTO.getBody(), LocalDateTime.now(), conversation, uid);

        if(response == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(messageService.messageToMessageDTO(response, uid));
    }

    @PostMapping("/getMessagesByConversation")
    public ResponseEntity<MessagePageDTO> getMessagesByConversation(@RequestBody GetMessagesDTO getMessagesDTO)
    {
        int uid = JwtUtility.getUid();

        Conversation conversation = messageService.getConversationById(getMessagesDTO.getConversationId());

        if(!messageService.isUserInConversation(conversation, uid))
            return ResponseEntity.internalServerError().build();

        Page<Message> messagePage = messageService.getMessagesByConversation(getMessagesDTO.getConversationId(), getMessagesDTO.getPageNum(), 10);

        return ResponseEntity.ok(new MessagePageDTO(messagePage.getTotalPages(), messageService.messageListToMessageDTOList(messagePage.getContent())));
    }
}
