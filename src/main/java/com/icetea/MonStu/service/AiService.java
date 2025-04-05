package com.icetea.MonStu.service;

import com.icetea.MonStu.dto.request.TransDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
public class AiService {

    private final ChatClient chatClient;

    public AiService(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    public TransDTO transTarget(TransDTO transDTO) {
        String prompt = String.format(
                "Please answer the form only as \"word before transformation - transformed word\"\n" +
                        "I don't need to know how to read it\n" +
                        "Please translate \"%s\" into korean." +
                        "If you don't know, just answer \"X\"",
                transDTO.getTarget()
        );
        log.info("prompt : {}", prompt);
        transDTO.setTransed(chatClient.prompt()
                                    .user(prompt)
                                    .call()
                                    .content());
        return transDTO;
    }

    public TransDTO forTestingCode(TransDTO transDTO) {
        String[] sampleSentences = {
                "dog",
                "Hello, world!",
                "Java is fun.",
                "Random text generation.",
                "cat",
                "Coding is great!",
                "girl",
                "Let's mix characters and sentences."
        };
        transDTO.setTransed(sampleSentences[ThreadLocalRandom.current().nextInt(0, 8)]);
        return transDTO;
    }
}
