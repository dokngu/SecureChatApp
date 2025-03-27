package com.example.demo.Controller;

import com.example.demo.Model.ChatMessage;
import com.example.demo.Model.ChatService;
import com.example.demo.Model.MyAppUser;
import com.example.demo.Model.MyAppUserRepository;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private MyAppUserRepository myAppUserRepository;

    @GetMapping("/chat")
    public String getChatPage(Model model) {
        List<ChatMessage> messages = chatService.getAllMessages();
        //SECURITY: encode every message obtained through OWASP Encoder
        List<ChatMessage> encodedMessages = messages.stream()
                .map(message -> {
                    message.setMessage(Encode.forHtmlContent(message.getMessage()));
                    return message;
                }
                ).collect(Collectors.toList());

        model.addAttribute("messages", encodedMessages);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<MyAppUser> user = myAppUserRepository.findByUsername(username);
        user.ifPresent(u -> model.addAttribute("user", u));
        return "chat";
    }

    @PostMapping("/chat/send")
    public String sendMessage(@RequestParam("message") String message) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<MyAppUser> user = myAppUserRepository.findByUsername(username);

        //SECURITY: encode every message obtained through OWASP Encoder
        String encodedMessage = Encode.forHtmlContent(message);
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessage(encodedMessage);
        if(user.isPresent()) {
            chatMessage.setUser(user.get());
        }
        chatService.sendMessage(chatMessage);
        return "redirect:/chat";
    }
}
