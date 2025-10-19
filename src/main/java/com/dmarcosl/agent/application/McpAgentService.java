package com.dmarcosl.agent.application;

import com.dmarcosl.agent.domain.McpAgentResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.client.McpAsyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class McpAgentService {

  private final List<McpAsyncClient> clients;

  private final McpClientService mcpClientService;
  private final ChatClient chatClient;
  private final ObjectMapper mapper = new ObjectMapper();

  public Mono<McpAgentResult> handle(String userPrompt) {
    // Build current catalog (mcp + tools) for the LLM
    return getMcpInfoAsString()
        .flatMap(
            servers -> {
              // Prompt LLM to choose tool
              String system =
                  """
              You are a tool-routing agent.
              You MUST choose exactly one tool to call given the user's prompt.
              Output STRICT JSON with fields: mcp, tool, arguments (object). No prose.
              """;
              String user =
                  """
              Available tools:
              %s

              User prompt: %s

              Return ONLY this JSON shape (no code fences, no extra text):
              {"mcp":"<serverName>","tool":"<toolName>","arguments":{...}}
              """
                      .formatted(servers, userPrompt);

              // Request the decision from the model
              var result = chatClient.prompt().system(system).user(user).call().content();

              // Parse the JSON decision and call the appropriate MCP tool
              return routeAndExecute(result, userPrompt);
            });
  }

  private Mono<String> getMcpInfoAsString() {
    return mcpClientService
        .getServers()
        .collectList()
        .flatMapMany(
            mcpServers ->
                Mono.just(
                    mcpServers.stream()
                        .map(
                            mcpServer -> {
                              try {
                                return mapper
                                    .writerWithDefaultPrettyPrinter()
                                    .writeValueAsString(mcpServer);
                              } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                              }
                            })
                        .collect(Collectors.joining(", "))))
        .next();
  }

  private Mono<McpAgentResult> routeAndExecute(String decisionJson, String originalPrompt) {
    try {
      JsonNode root = mapper.readTree(decisionJson);
      String mcp = safeText(root, "mcp");
      String tool = safeText(root, "tool");
      JsonNode argsNode = root.get("arguments");
      Map<String, Object> args =
          (argsNode != null && argsNode.isObject())
              ? mapper.convertValue(argsNode, Map.class)
              : Map.of();

      McpAsyncClient client =
          clients.stream()
              .filter(c -> c.getServerInfo().name().equals(mcp))
              .findFirst()
              .orElseThrow(() -> new IllegalArgumentException("Unknown MCP: " + mcp));

      return client
          .callTool(McpSchema.CallToolRequest.builder().name(tool).arguments(args).build())
          .map(
              resp -> {
                // The response is a Map/Json with the tool result
                JsonNode toolResp = mapper.valueToTree(resp);
                return new McpAgentResult("Ok", mcp, tool, toolResp);
              });

    } catch (Exception ex) {
      return Mono.error(
          new IllegalArgumentException("Bad tool selection JSON: " + decisionJson, ex));
    }
  }

  private String safeText(JsonNode n, String field) {
    JsonNode v = n.get(field);
    if (v == null || !v.isTextual()) throw new IllegalArgumentException("Missing field: " + field);
    return v.asText();
  }
}
