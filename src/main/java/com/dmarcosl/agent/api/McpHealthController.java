package com.dmarcosl.agent.api;

import com.dmarcosl.agent.application.McpClientService;
import com.dmarcosl.agent.domain.McpServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@AllArgsConstructor
public class McpHealthController {

  private final McpClientService mcpClientService;

  private final ObjectMapper mapper = new ObjectMapper();

  @GetMapping("/health")
  public Flux<McpServer> health() {
    return mcpClientService.getServers();
  }
}
