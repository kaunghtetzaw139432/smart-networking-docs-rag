package dev.danvega.faq;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FaqController {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

  
    @Value("classpath:/prompt/networking.st")
    private Resource networkingPromptResource;

    public FaqController(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder.build();
        this.vectorStore = vectorStore;
    }

    @GetMapping("/faq")
    public String faq(@RequestParam(value = "message", defaultValue = "What is Networking?") String message) {
        
      
        List<Document> similarDocuments = vectorStore.similaritySearch(
                SearchRequest.query(message).withTopK(2)
        );

     
        String context = similarDocuments.stream()
                .map(Document::getContent)
                .collect(Collectors.joining("\n"));

        PromptTemplate promptTemplate = new PromptTemplate(networkingPromptResource);
        
        Map<String, Object> promptParameters = new HashMap<>();
        promptParameters.put("context", context);
        promptParameters.put("question", message);
        
        Prompt prompt = promptTemplate.create(promptParameters);

        // ၄။ ChatClient မှတစ်ဆင့် Groq (Llama) ဆီသို့ ပို့လွှတ်ခြင်း
       String ai=chatClient.prompt(prompt)
                .call()
                .content();
                System.out.println(ai);
                return ai;
    }
}