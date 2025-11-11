package com.dmarcosl.agent.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AgentConfig {

  @Bean
  ChatClient chatClient(ChatClient.Builder builder, ToolCallbackProvider toolProvider) {
    return builder
        .defaultToolCallbacks(toolProvider)
        .defaultOptions(
            OpenAiChatOptions.builder()
                .parallelToolCalls(false)
                .maxTokens(800)
                .build())
        .build();
  }
}
