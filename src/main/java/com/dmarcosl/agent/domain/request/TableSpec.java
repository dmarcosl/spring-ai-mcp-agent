package com.dmarcosl.agent.domain.request;

import java.util.List;
import java.util.Map;

public record TableSpec(
    String type, String title, List<Column> columns, List<Map<String, Object>> rows)
    implements UiSpec {
  public TableSpec {
    type = "table";
  }
}
