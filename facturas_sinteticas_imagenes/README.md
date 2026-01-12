# facturas_sinteticas_imagenes

Comprehensive README for the `facturas_sinteticas_imagenes` Spring Boot project.

**Project Overview**
- **Purpose:** Generate synthetic invoice images and text output using OpenAI image/chat models and stream results to clients via Server-Sent Events (SSE).
- **Language & Frameworks:** Java 17, Spring Boot 3.x, Spring AI starter (OpenAI image/chat), Maven wrapper (`mvnw`).

**Key Components**
- **Controllers:**
  - `GenerationImageController` — endpoints under `/image` for image generation and streaming.
  - `GenerationBasicTemplateController` — endpoints under `/basic-template` for sending files, setting prompts and streaming template generation.
- **Services:**
  - `ExecutingPromptImageService` — calls the OpenAI image model and returns Base64 image payloads.
  - `ExecutingPromptExtractTemplateService` — sends prompts + text file contents to the chat client to obtain template data.
  - `MessageToChatClientService` — builds chat messages including text-file content extraction.
  - `ConverterFileService` — converts uploaded `MultipartFile` instances to a `Map<String, byte[]>`.
  - `StoreFilesService` — transient in-memory storage for file parts between requests.
- **Utilities:** `ConverterUtil`, `ExtensionUtils`, `AsyncUtil`, `SseStreamUtil` (helpers for SSE and asynchronous execution).

**Prerequisites**
- Java 17 (or compatible JDK).
- Maven (the project includes the Maven wrapper `mvnw`).
- An OpenAI API key for production usage (set in `OPENAI_API_KEY` environment variable or `application.properties`).

**Configuration**
- Primary config file: `src/main/resources/application.properties`.
  - Important properties:
    - `spring.ai.openai.api-key` — your OpenAI API key (or set `OPENAI_API_KEY` env var).
    - `spring.ai.openai.base-url` — base URL for OpenAI (default in repo: `https://api.openai.com/`).
    - `image.model.name` — model name used by image-generation code (e.g., `gpt-image-1`).

**Environment variables**
- Export OpenAI key (example):
```bash
export OPENAI_API_KEY="sk-..."
```

**Build & Run**
- Build (skip tests):
```bash
./mvnw -DskipTests package
```
- Run via Spring Boot:
```bash
./mvnw spring-boot:run
```
- Or run packaged JAR:
```bash
java -jar target/facturas_sinteticas_imagenes-0.0.1-SNAPSHOT.jar
```

**API Endpoints**
- `POST /image/set-prompt` — sets prompt for image generation. Request body: `GenerationImageRequest` (JSON with `prompt` array).
- `GET /image/stream-image` — SSE endpoint that streams image-generation events. Produces `text/event-stream`.
- `POST /basic-template/prompt` — set prompt for basic template extraction. Request body: `GenerationImageRequest`.
- `POST /basic-template/sending-files` — multipart upload endpoint, form part name `files` (List<MultipartFile>).
- `GET /basic-template/stream-basic-template` — SSE endpoint for chat/template generation.

Example `GenerationImageRequest` JSON:
```json
{
  "prompt": ["Create an invoice layout with totals and items"]
}
```

**Testing & Coverage**
- Unit tests live under `src/test/java`. A minimal Spring test exists in `FacturasSinteticasImagenesApplicationTests`.
- To run tests:
```bash
./mvnw test
```
- To generate JaCoCo coverage report (HTML):
```bash
./mvnw test jacoco:report
# open target/site/jacoco/index.html
```

Notes: for local unit tests I added `src/test/resources/application.properties` with placeholder OpenAI config so that Spring context creation succeeds without needing a real API key during tests.

**Troubleshooting**
- Bean creation errors referencing `OpenAiImageApi` / `customOpenAiImageModel` commonly mean the OpenAI properties are missing or invalid. Ensure `OPENAI_API_KEY` is set or `application.properties` contains `spring.ai.openai.api-key`.
- If you want to avoid creating real OpenAI clients during tests, mock `ImageModel` / `ChatClient.Builder` beans in your test configuration or use `@MockBean` on test classes.

**Development notes**
- SSE handling and async execution were consolidated in `SseStreamUtil` and `AsyncUtil` to avoid duplication.
- `ConverterFileService` converts multipart uploads into a map keyed by original filename; duplicate names keep the first occurrence.

**Contribution**
- Fork, create a feature branch, add tests for new behavior, run `./mvnw test`, and open a pull request.

**License**
- No license included by default. Add a `LICENSE` file if you plan to open-source the repository.

---

If you want I can also add example curl commands that exercise every endpoint and a small integration test script — tell me which you prefer.
