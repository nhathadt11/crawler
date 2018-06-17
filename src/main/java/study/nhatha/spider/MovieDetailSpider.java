package study.nhatha.spider;

import study.nhatha.middleware.ImageTagHandler;
import study.nhatha.middleware.ReplaceAllHandler;
import study.nhatha.middleware.TransformerMiddleware;
import study.nhatha.middleware.UnwantedClosingATagHandler;
import study.nhatha.util.NetUtils;
import study.nhatha.util.StringUtils;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static study.nhatha.util.AppConstants.HD_MOVIE_STYLE_SHEET;
import static study.nhatha.util.NetUtils.toBufferedReader;
import static study.nhatha.util.ResourceUtils.getResource;
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
      String html = StringUtils.normalize(reader, "");

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

        TransformerFactory factory = TransformerFactory.newInstance();

        InputStream rulesIn = getResource(HD_MOVIE_STYLE_SHEET);
        Transformer transformer = factory.newTransformer(new StreamSource(rulesIn));

        OutputStream outputStream = new ByteArrayOutputStream();

        transformer.transform(
            new StreamSource(toInputStream(processed)),
            new StreamResult(outputStream)
        );

        System.out.println(outputStream.toString());
      }
    } catch (IOException | TransformerException e) {
      e.printStackTrace();
    }
  }
}
