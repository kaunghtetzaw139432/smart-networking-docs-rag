# Spring AI Networking RAG Assistant

<div align="center">

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Groq](https://img.shields.io/badge/LLM-Groq%20Llama%203-orange?style=for-the-badge)
![License](https://img.shields.io/badge/license-MIT-blue?style=for-the-badge)

</div>

This project is a high-level implementation of **Retrieval-Augmented Generation (RAG)** architecture using **Spring Boot** and **Spring AI**. It demonstrates how a local knowledge base can be integrated with Large Language Models (LLMs) to provide contextually accurate responses.

---

## 🎯 Project Objective
The primary goal of this project is to understand the **System Architecture** of RAG rather than just learning syntax. It explores how unstructured data is processed, stored, and retrieved to augment AI prompts, ensuring the AI operates on private or specific domain knowledge (Computer Networking).

---

## 🏗️ Architectural Overview

The system follows a 4-stage pipeline:

1.  **Ingestion Layer**: Reads documentation from `src/main/resources/docs/network.txt`.
2.  **Transformation Layer**: Uses `TokenTextSplitter` to break long documents into manageable chunks (tokens).
3.  **Embedding & Storage Layer**:
    * Generates semantic vectors using a **Local Transformers Embedding Model**.
    * Indexes these vectors in a `SimpleVectorStore` for local persistence.
4.  **Orchestration & Retrieval Layer**:
    * Performs a **Similarity Search** based on the user's query.
    * Augments the prompt template with retrieved context and routes to Groq.



---

## 🧩 Key Components

* **`FaqController.java`**: Acts as the Orchestrator. It manages the flow between the user query, the Vector Store, and the Chat Client.
* **`RagConfiguration.java`**: The core configuration bean that handles the lifecycle of the Vector Store.
* **`networking.st`**: A specialized **String Template** that defines the AI's persona and constraints.

---

## 🛠️ Tech Stack
* **Backend**: Spring Boot 3.3.0
* **AI Framework**: Spring AI
* **Vector Database**: SimpleVectorStore
* **Embeddings**: Local Transformers (via DJL)
* **LLM Provider**: Groq (Llama 3)
* **Java Version**: JDK 21

---

## 🚀 Getting Started

### 1. Prerequisites
* Obtain a **Groq API Key** from [Groq Console](https://console.groq.com/).
* Ensure **JDK 21** is installed.

### 2. Configuration
Add your API key to your environment variables or `application.properties`:
```properties
spring.ai.openai.api-key=${GROQ_API_KEY}
spring.ai.openai.base-url=[https://api.groq.com/openai/v1](https://api.groq.com/openai/v1)
vector.store.name=networking-vector-store.json
```
### 3. Execution
```
mvn spring-boot:run
```

### 🧪 API Usage
* **Endpoint**: ```GET /faq```

* ***Parameter***: ```message```

* **Example Request:**

```http://localhost:8080/faq?message=Explain the OSI Model.```
