package com.dmarcosl.agent.domain.request;

import java.util.List;

public record PieSpec(String type, String title, List<String> labels, List<Double> data)
    implements UiSpec {
  public PieSpec {
    type = "pie";
  }
}
