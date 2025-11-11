package com.dmarcosl.agent.application;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
public class AgentService {

  private final ChatClient chatClient;
  private final String systemPrompt;

  public AgentService(ChatClient chatClient, ResourceLoader loader) throws IOException {
    this.chatClient = chatClient;

    var res = loader.getResource("classpath:system-prompt.txt");
    this.systemPrompt = new String(res.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
  }

  public Flux<String> streamAgent(String userPrompt) {
    return Flux.defer(
            () -> chatClient.prompt().system(systemPrompt).user(userPrompt).stream().content())
        .subscribeOn(Schedulers.boundedElastic())
        .onErrorResume(
            WebClientResponseException.class,
            e -> {
              var body = e.getResponseBodyAsString(StandardCharsets.UTF_8);
              log.error("OpenAI 400 body: {}", body);
              return Flux.error(new RuntimeException("OpenAI 400: " + body));
            });
  }
}
