package com.dmarcosl.agent.application;

import com.dmarcosl.agent.domain.health.McpServer;
import com.dmarcosl.agent.domain.health.McpTool;
import io.modelcontextprotocol.client.McpAsyncClient;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@AllArgsConstructor
public class McpListService {

  private final List<McpAsyncClient> clients;

  public Flux<McpServer> getServers() {
    return Flux.fromIterable(clients)
        .flatMap(
            client ->
                client
                    .listTools()
                    .map(
                        toolList ->
                            McpServer.builder()
                                .name(client.getServerInfo().name())
                                .toolNumber(toolList.tools().size())
                                .tools(
                                    toolList.tools().stream()
                                        .map(
                                            tool ->
                                                McpTool.builder()
                                                    .name(tool.name())
                                                    .description(tool.description())
                                                    .build())
                                        .toList())
                                .build()));
  }
}
