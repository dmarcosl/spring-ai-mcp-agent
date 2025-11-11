package com.dmarcosl.agent.domain.request;

import com.fasterxml.jackson.annotation.*;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = TableSpec.class, name = "table"),
  @JsonSubTypes.Type(value = PieSpec.class, name = "pie"),
  @JsonSubTypes.Type(value = BarSpec.class, name = "bar")
})
public sealed interface UiSpec permits TableSpec, PieSpec, BarSpec {
  String type();
}
