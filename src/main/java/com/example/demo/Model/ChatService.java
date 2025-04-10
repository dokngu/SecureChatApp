package com.example.demo.Model;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

@AllArgsConstructor
public class ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public List<ChatMessage> getAllMessages() {
        return chatMessageRepository.findAllByOrderByTimestampAsc();
    }

    public void sendMessage(ChatMessage message) {
        chatMessageRepository.save(message);
    }

}
