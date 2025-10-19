package com.dmarcosl.agent.domain;

import com.fasterxml.jackson.databind.JsonNode;

public record McpAgentResult(String response, String mcp, String tool, JsonNode toolResponse) {}
