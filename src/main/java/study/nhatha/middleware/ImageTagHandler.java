package study.nhatha.middleware;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageTagHandler implements TransformerMiddleware.Transform {

  @Override
  public String transform(String previous) {
    Pattern p = Pattern.compile("(<img.+?)(.)>");
    Matcher m = p.matcher(previous);

    StringBuffer sb = new StringBuffer(previous.length());
    while (m.find()) {
      String beforeClosingTag = m.group(2);
      if ("/".equals(beforeClosingTag)) {
        m.appendReplacement(sb, "$1></img>");
      } else {
        m.appendReplacement(sb, "$1$2></img>");
      }
    }
    m.appendTail(sb);

    return sb.toString();
  }
}
