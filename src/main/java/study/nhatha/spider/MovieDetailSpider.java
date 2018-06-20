package study.nhatha.spider;

import study.nhatha.middleware.ImageTagHandler;
import study.nhatha.middleware.ReplaceAllHandler;
import study.nhatha.middleware.TransformerMiddleware;
import study.nhatha.middleware.UnwantedClosingATagHandler;
import study.nhatha.util.NetUtils;
import study.nhatha.util.XmlUtils;

import javax.xml.transform.TransformerException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static study.nhatha.util.AppConstants.HD_MOVIE_STYLE_SHEET;
import static study.nhatha.util.NetUtils.toBufferedReader;
import static study.nhatha.util.ResourceUtils.getResource;
import static study.nhatha.util.StringUtils.normalize;
import static study.nhatha.util.StringUtils.toInputStream;

public class MovieDetailSpider implements Runnable {
  private String url;
  private String htmlFragmentExtractor;
  private int htmlFragmentExtractorGroupNumber;

  public MovieDetailSpider(String url, String htmlFragmentExtractor, int htmlFragmentExtractorGroupNumber) {
    this.url = url;
    this.htmlFragmentExtractor = htmlFragmentExtractor;
    this.htmlFragmentExtractorGroupNumber = htmlFragmentExtractorGroupNumber;
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
            .apply();

        InputStream rulesIn = getResource(HD_MOVIE_STYLE_SHEET);
        OutputStream outputStream = XmlUtils.transform(toInputStream(processed), rulesIn);

        System.out.println(outputStream.toString());
      }
    } catch (IOException | TransformerException e) {
      e.printStackTrace();
    }
  }
}
