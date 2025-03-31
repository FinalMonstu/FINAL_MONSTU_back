package com.icetea.MonStu.service;

import com.icetea.MonStu.dto.request.TransDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AiService {

    private final ChatClient chatClient;

    public AiService(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    public String transTarget(TransDTO transDTO) {
        String prompt = String.format(
                "Please answer the form only as \"word before transformation - transformed word\"\n" +
                        "I don't need to know how to read it\n" +
                        "Please translate \"%s\" into korean.",
                transDTO.getTarget()
        );
        log.info("prompt : {}", prompt);
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}
