package study.nhatha.spider;

import org.xml.sax.SAXException;
import study.nhatha.middleware.TransformerMiddleware;
import study.nhatha.util.AppConstants;
import study.nhatha.util.NetUtils;
import study.nhatha.util.StreamUtils;
import study.nhatha.util.XmlUtils;
import study.nhatha.validation.XmlValidationHandler;
import study.nhatha.validation.XmlValidator;

import javax.xml.transform.TransformerException;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
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
  private List<TransformerMiddleware.Transform> tranforms;

  public MovieDetailSpider(
      String url,
      String htmlFragmentExtractor,
      int htmlFragmentExtractorGroupNumber,
      InputStream stylesheetStream,
      List<TransformerMiddleware.Transform> transforms) {

    this.url = url;
    this.htmlFragmentExtractor = htmlFragmentExtractor;
    this.htmlFragmentExtractorGroupNumber = htmlFragmentExtractorGroupNumber;
    this.stylesheetStream = stylesheetStream;
    this.tranforms = transforms;
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
            .useAll(tranforms)
            .apply();

        ByteArrayOutputStream outputStream = XmlUtils.transform(toInputStream(processed), stylesheetStream);

        validateXml(StreamUtils.toInputStream(outputStream));
      }
    } catch (IOException | TransformerException e) {
      System.out.println("ERROR / " + e.getMessage());
    }
  }

  private void validateXml(InputStream xmlContent) {
    XmlValidator xmlValidator = new XmlValidator(AppConstants.HD_MOVIE_SCHEMA, xmlContent, new XmlValidationHandler() {
      @Override
      public void onPassed() {
        System.out.println("Passed\n");
      }

      @Override
      public void onRejected(SAXException e) {
        System.out.println("Rejected\n");
      }
    });

    xmlValidator.validate();
  }
}
