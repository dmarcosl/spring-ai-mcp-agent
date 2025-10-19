# MCP Agent Demo

This project is an agent service built with Java 25, Spring Boot 3.5.6, Spring AI MCP (1.1.0-M3) and WebFlux.

It connects to multiple MCP servers and exposes a REST API that routes user prompts to the most relevant tool automatically using an OpenAI model.

This project is designed to be used together with MCP servers like:

https://github.com/dmarcosl/spring-ai-mcp-stockmcp
https://github.com/dmarcosl/spring-ai-mcp-salesmcp

You can use it independently or with this other solution:
https://github.com/dmarcosl/spring-ai-mcp-demo

---

## 🐳 Run with Docker

### 1. Build the image

```bash
docker build -t agent .
```

### 2. Run the container

```bash
docker run --rm -p 8086:8086 -e OPENAI_API_KEY=XXX agent
```

The server will expose this two endpoints:

```
GET http://localhost:8086/health
POST http://localhost:8086/agent
```

---

## 🪪 License

MIT
