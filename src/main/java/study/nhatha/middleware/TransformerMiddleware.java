package study.nhatha.middleware;

import java.util.LinkedList;
import java.util.List;

public class TransformerMiddleware {

  @FunctionalInterface
  interface Transform {
    String transform(String previous);
  }

  private String raw;
  private List<Transform> transforms;

  public TransformerMiddleware(String raw) {
    this.raw = raw;
    transforms = new LinkedList<>();
  }

  public TransformerMiddleware use(Transform transform) {
    this.transforms.add(transform);

    return this;
  }

  public String apply() {
    transforms.forEach(transform -> raw = transform.transform(raw));

    return raw;
  }
}
