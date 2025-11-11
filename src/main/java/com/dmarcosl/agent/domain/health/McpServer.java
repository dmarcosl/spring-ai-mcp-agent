package com.dmarcosl.agent.domain.health;

import java.util.List;
import lombok.Builder;

@Builder
public record McpServer(String name, int toolNumber, List<McpTool> tools) {}
