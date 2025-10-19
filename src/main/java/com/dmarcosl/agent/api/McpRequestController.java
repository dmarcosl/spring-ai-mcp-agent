package com.dmarcosl.agent.api;

import com.dmarcosl.agent.application.McpAgentService;
import com.dmarcosl.agent.domain.McpAgentResult;
import com.dmarcosl.agent.domain.McpRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
public class McpRequestController {

  private final McpAgentService mcpAgentService;

  @PostMapping("/agent")
  public Mono<McpAgentResult> handlePrompt(@RequestBody McpRequest request) {
    return mcpAgentService.handle(request.prompt());
  }
}
