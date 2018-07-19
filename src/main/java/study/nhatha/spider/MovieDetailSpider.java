package study.nhatha.spider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import study.nhatha.middleware.TransformerMiddleware;
import study.nhatha.model.Movie;
import study.nhatha.repository.HibernateMovieRepository;
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
  private static final Logger LOGGER = LoggerFactory.getLogger(MovieDetailSpider.class);
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
    LOGGER.info(String.format("[GET] URL:%s", url));

    try (InputStream inputStream = NetUtils.connect(url)) {
      BufferedReader reader = toBufferedReader(inputStream);
      String html = normalize(reader, "");

      Pattern pattern = Pattern.compile(htmlFragmentExtractor);
      Matcher matcher = pattern.matcher(html);

      if (matcher.find()) {
        String movieDetailHtmlFragment = matcher.group(htmlFragmentExtractorGroupNumber);

        String processed = makeWellFormed(movieDetailHtmlFragment);

        ByteArrayOutputStream outputStream = XmlUtils.transform(toInputStream(processed), stylesheetStream);
//        LOGGER.info(outputStream.toString());

        validateXml(StreamUtils.toInputStream(outputStream));
      }
    } catch (IOException | TransformerException e) {
      LOGGER.error(String.format("[REASON]: %s [URL]: %s", e.getMessage(), url));
    }
  }

  private String makeWellFormed(String nonWellFormed) {
    return
        new TransformerMiddleware(nonWellFormed)
        .useAll(tranforms)
        .apply();
  }

  private void validateXml(InputStream xmlContent) throws IOException {
    XmlValidator xmlValidator = new XmlValidator(AppConstants.MOVIE_SCHEMA, xmlContent, new XmlValidationHandler() {
      @Override
      public void onPassed(InputStream validXmlContent) {
        Movie movie = XmlUtils.unmarshal(validXmlContent, Movie.class);
        movie.setUrl(url);

        persistToStorage(movie);

        LOGGER.info(String.format("[PASSED][TITLE]: %s", movie.getTitle()));
      }

      @Override
      public void onRejected(SAXException e) {
        LOGGER.error(String.format("[REJECTED][REASON]: %s [URL]: %s", e.getMessage(), url));
        LOGGER.error(String.format("[XML CONTENT]: %s", StreamUtils.toString(xmlContent)));
      }
    });

    xmlValidator.validate();
  }

  private void persistToStorage(Movie movie) {
    HibernateMovieRepository
        .getInstance()
        .create(movie);
  }
}
