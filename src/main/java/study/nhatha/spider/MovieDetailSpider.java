package study.nhatha.spider;

import study.nhatha.middleware.*;
import study.nhatha.util.NetUtils;
import study.nhatha.util.XmlUtils;

import javax.xml.transform.TransformerException;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static study.nhatha.util.NetUtils.toBufferedReader;
import static study.nhatha.util.StringUtils.normalize;
import static study.nhatha.util.StringUtils.toInputStream;

public class MovieDetailSpider implements Runnable {
  private String url;
  private String htmlFragmentExtractor;
  private int htmlFragmentExtractorGroupNumber;
  private InputStream stylesheetStream;

  public MovieDetailSpider(
      String url,
      String htmlFragmentExtractor,
      int htmlFragmentExtractorGroupNumber,
      InputStream stylesheetStream) {

    this.url = url;
    this.htmlFragmentExtractor = htmlFragmentExtractor;
    this.htmlFragmentExtractorGroupNumber = htmlFragmentExtractorGroupNumber;
    this.stylesheetStream = stylesheetStream;
  }

  @Override
  public void run() {
    System.out.println("GET / " + url);

    try (InputStream inputStream = NetUtils.connect(url)) {
      BufferedReader reader = toBufferedReader(inputStream);
      String html = normalize(reader, "");

      Pattern pattern = Pattern.compile(htmlFragmentExtractor);
      Matcher matcher = pattern.matcher(html);

      if (matcher.find()) {
        String movieDetailHtmlFragment = matcher.group(htmlFragmentExtractorGroupNumber);

        TransformerMiddleware middleware = new TransformerMiddleware(movieDetailHtmlFragment);
        String processed = middleware
            .use(new ImageTagHandler())
            .use(new UnwantedClosingATagHandler())
            .use(new ReplaceAllHandler("itemscope", ""))
            .use(BoundaryHandler.before("<!DOCTYPE div [<!ENTITY nbsp \"&#160;\">]>"))
            .apply();

        OutputStream outputStream = XmlUtils.transform(toInputStream(processed), stylesheetStream);

        System.out.println(outputStream.toString());
      }
    } catch (IOException | TransformerException e) {
      System.out.println("ERROR / " + e.getMessage());
    }
  }
}
