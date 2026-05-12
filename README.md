## mindcare-agent

一个基于 Spring Boot 3 + Spring AI 的「智能心理评估 Agent」示例项目，面向校园学生 / 医院门诊等心理场景，支持意图识别、情绪分析、Agentic RAG、邮件预警与聊天记录 Excel 导出，并提供 Mock 可运行模式。

### 技术栈

- Java 17
- Spring Boot 3.x（Web + WebFlux + Security + Data JPA）
- H2 / MySQL
- Spring AI（OpenAI / Ollama 封装）+ MockModelInferenceService
- Apache POI（导出 Excel）
- JavaMailSender（发邮件）
- Lombok、Jackson

### 项目结构

关键包结构：

- `com.mindcare`
  - `MindCareApplication`：启动类
  - `config`：安全、模型、向量库、MCP 占位配置
  - `controller`：`ChatController`、`AdminController`
  - `service`：`ChatOrchestratorService`、`AgenticRAGService`、`EmotionAnalysisService` 等
  - `repository`：JPA 仓储
  - `entity`：`User` / `ChatMessage` / `EmotionRecord`
  - `dto`：`ChatRequest` / `ChatResponse` / `IntentDecision` / `RagDecision` 等
  - `util`：`ExcelWriter` / `WhisperClient` / `MediaPipeClient`

### 启动方式

```bash
cd d:\postgraduate\project
mvn spring-boot:run
```

默认：

- 端口：`8080`
- 数据库：H2 内存库（自动建表）
- 管理员账号（Basic Auth）：`admin / password`
- H2 控制台：`http://localhost:8080/h2-console`

### Mock 模式说明

`application.yml`：

```yaml
mindcare:
  mock-model: true
```

- `true`（默认）：使用 `MockModelInferenceService`，不依赖任何大模型服务；
- `false`：使用 `RealModelInferenceService` + Spring AI，需本机有 Ollama 或正确配置 OpenAI。

Mock 行为：

- 意图识别：根据关键词返回 JSON `IntentDecision`；
- RAG 规划：返回规则化 JSON `RagDecision`；
- 最终回答：本地构造自然语言文本（带“模拟回答”提示）。

### 接口说明

#### 用户聊天

- `POST /api/chat/once`  
  请求：

  ```json
  { "userId": 1, "content": "最近总是失眠，压力好大" }
  ```

  响应：

  ```json
  {
    "answer": "...",
    "intent": "CONSULT",
    "emotionLabel": "ANXIOUS",
    "emotionScore": 3.0
  }
  ```

- `GET /api/chat/stream?userId=1&content=你好`  
  返回 `text/event-stream`，按句子流式输出。

#### 管理端（需 ADMIN）

- `GET /api/admin/messages`：所有聊天记录；
- `GET /api/admin/messages/{userId}`：指定用户聊天记录；
- `GET /api/admin/emotions/{userId}`：指定用户情绪记录；
- `GET /api/admin/messages/export?userId=1`：导出 Excel（为空则导出全部）。

### 替换为真实 Ollama / OpenAI / Chroma

1. 真实模型：

```yaml
mindcare:
  mock-model: false

spring:
  ai:
    ollama:
      base-url: http://localhost:11434
      chat:
        model: llama3
    openai:
      api-key: sk-xxxx
      chat:
        model: gpt-4.1-mini
```

2. 向量库：

- 当前使用 `InMemoryVectorStoreStub` 预置心理知识；
- 未来可在 `ChromaConfig` 中替换为真正向量库（Chroma/PGVector/Milvus），并修改 `AgenticRAGService` 使用统一 VectorStore 接口。

### 面试讲解要点

- **业务链路**：用户输入 → 意图识别(JSON) → 情绪分析(JSON) → 分流（CHAT / CONSULT / RISK）→ RAG 或预警 → 持久化 + 导出。
- **工程实践**：Prompt 统一放 `resources/prompts`；通过 `ModelInferenceService` 抽象大模型，提供 Mock/Real 两套实现；使用 Spring Security 做最小管理权限控制。
- **扩展性**：多模态预留（Whisper / MediaPipe）、MCP 风格工具封装、向量库可插拔，实现真实可演进的心理 Agent 架构 Demo。

