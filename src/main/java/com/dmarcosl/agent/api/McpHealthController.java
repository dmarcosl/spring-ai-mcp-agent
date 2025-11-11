package com.dmarcosl.agent.api;

import com.dmarcosl.agent.application.McpListService;
import com.dmarcosl.agent.domain.health.McpServer;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@AllArgsConstructor
public class McpHealthController {

  private final McpListService mcpListService;

  @CrossOrigin(origins = "*")
  @GetMapping("/health")
  public Flux<McpServer> health() {
    return mcpListService.getServers();
  }
}
