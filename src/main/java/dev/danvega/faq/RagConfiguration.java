package dev.danvega.faq;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class RagConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(RagConfiguration.class);

    @Value("classpath:/docs/network.txt")
    private Resource NetworkingDoc;

    @Value("${vector.store.name}") 
    private String vectorStoreName;

    @Bean
    SimpleVectorStore vectorStore(EmbeddingModel embeddingModel) throws IOException {
        SimpleVectorStore vectorStore = new SimpleVectorStore(embeddingModel);
        File vectorStoreFile = getVectorStoreFile();

        if (vectorStoreFile.exists()) {
            logger.info("Vector Store file exists. Loading...");
            vectorStore.load(vectorStoreFile);
        } else {
            logger.info("Vector store file does not exist, generating embeddings locally...");
            TextReader textReader = new TextReader(NetworkingDoc);
            List<Document> documents = textReader.get();
            TokenTextSplitter splitter = new TokenTextSplitter();
            List<Document> splitDocuments = splitter.apply(documents);
            
            vectorStore.add(splitDocuments); 
            vectorStore.save(vectorStoreFile);
            logger.info("Vector store created and saved locally.");
        }
        return vectorStore;
    }

    private File getVectorStoreFile() {
        Path dataPath = Paths.get("src", "main", "resources", "data");
        return new File(dataPath.toFile(), vectorStoreName);
    }
}
