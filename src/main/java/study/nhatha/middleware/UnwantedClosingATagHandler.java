package study.nhatha.middleware;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnwantedClosingATagHandler implements TransformerMiddleware.Transform {
  @Override
  public String transform(String previous) {
    Pattern p = Pattern.compile("(<span title.+?tn-texth3.+?)(</a>)(</span>)");
    Matcher m = p.matcher(previous);

    StringBuffer sb = new StringBuffer();
    while (m.find()) {
      m.appendReplacement(sb, "$1$3");
    }
    m.appendTail(sb);

    return sb.toString();
  }
}
