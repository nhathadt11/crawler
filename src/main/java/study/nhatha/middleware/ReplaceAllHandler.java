package study.nhatha.middleware;

public class ReplaceAllHandler implements TransformerMiddleware.Transform {
  private String pattern;
  private String replacement;

  public ReplaceAllHandler(String pattern, String replacement) {
    this.pattern = pattern;
    this.replacement = replacement;
  }

  @Override
  public String transform(String previous) {
    return previous.replaceAll(pattern, replacement);
  }
}
