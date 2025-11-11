package com.dmarcosl.agent.api;

import com.dmarcosl.agent.application.AgentService;
import com.dmarcosl.agent.domain.request.*;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
public class McpRequestController {

  private final AgentService agentService;

  @CrossOrigin(origins = "*")
  @PostMapping(value = "/agent/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<ServerSentEvent<String>> agentStream(@RequestBody McpRequest request) {
    return agentService.streamAgent(request.prompt()).map(s -> ServerSentEvent.builder(s).build());
  }

  @PostMapping(value = "/agent", produces = MediaType.TEXT_PLAIN_VALUE)
  public Mono<String> agent(@RequestBody McpRequest req) {
    return agentService
        .streamAgent(req.prompt())
        .reduce(new StringBuilder(), StringBuilder::append)
        .map(StringBuilder::toString);
  }
}
