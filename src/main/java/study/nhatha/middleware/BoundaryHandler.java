package study.nhatha.middleware;

public class BoundaryHandler {
  public static TransformerMiddleware.Transform before(String before) {
    return before::concat;
  }

  public static TransformerMiddleware.Transform after(String after) {
    return after::concat;
  }

  private BoundaryHandler() {
  }
}
