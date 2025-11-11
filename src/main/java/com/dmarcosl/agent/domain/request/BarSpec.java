package com.dmarcosl.agent.domain.request;

import java.util.List;

public record BarSpec(String type, String title, List<String> labels, List<Double> data)
    implements UiSpec {
  public BarSpec {
    type = "bar";
  }
}
