package com.bryan.libarterbe.controller.User;

import com.bryan.libarterbe.DTO.ConversationDTO;
import com.bryan.libarterbe.DTO.MessageCreateDTO;
import com.bryan.libarterbe.model.Conversation;
import com.bryan.libarterbe.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user/message")
public class MessageController {

    @Autowired
    MessageService messageService;

    @GetMapping("/getConversations/{asClient}")
    public ResponseEntity<List<ConversationDTO>> getConversations(@PathVariable boolean asClient)
    {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int uid = Math.toIntExact(jwt.getClaim("uid"));

        List<Conversation> conversations = messageService.getAllConversationsOfUser(asClient, uid);
        return ResponseEntity.ok(ConversationDTO.conversationListToConversationDTOList(conversations));
    }

//    @GetMapping("/getConversation/{id}")
//    public ResponseEntity<>



    @PostMapping("/addMessage")
    public ResponseEntity<String> addMessage(@RequestBody MessageCreateDTO messageDTO)
    {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int uid = Math.toIntExact(jwt.getClaim("uid"));
        Conversation conversation;

        try {
            conversation = messageService.getConversationByBookAndUser(messageDTO.getBookId(), uid);
        } catch (Exception e) {
            conversation = messageService.addConversation(messageDTO.getBookId(), uid);
        }
        boolean response = messageService.addMessage(messageDTO.getBody(), LocalDateTime.now(), conversation, uid);

        if(response == false)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok().build();
    }
}
