package study.nhatha.crawler;

import study.nhatha.middleware.TransformerMiddleware;
import study.nhatha.spider.MovieDetailSpider;
import study.nhatha.util.ThreadUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static study.nhatha.util.NetUtils.connect;
import static study.nhatha.util.NetUtils.toBufferedReader;
import static study.nhatha.util.ResourceUtils.getResource;
import static study.nhatha.util.StringUtils.normalize;

public abstract class AbstractMovieCrawler implements Crawlable {
  private String baseUrl;
  private String pageUrlTemplate;
  private int totalPageNumber;
  private String detailLinkExtractor;
  private int detailLinkExtractorGroupNumber;
  private String movieDetailHtmlFragmentExtractor;
  private int movieDetailHtmlFragmentExtractorGroupNumber;
  private String stylesheetPath;
  private List<TransformerMiddleware.Transform> transforms;

  AbstractMovieCrawler(
      String baseUrl,
      String pageUrlTemplate,
      int totalPageNumber,
      String detailLinkExtractor,
      int detailLinkExtractorGroupNumber,
      String movieDetailHtmlFragmentExtractor,
      int movieDetailHtmlFragmentExtractorGroupNumber,
      String stylesheetPath,
      List<TransformerMiddleware.Transform> transforms) {

    this.baseUrl = baseUrl;
    this.pageUrlTemplate = pageUrlTemplate;
    this.totalPageNumber = totalPageNumber;
    this.detailLinkExtractor = detailLinkExtractor;
    this.detailLinkExtractorGroupNumber = detailLinkExtractorGroupNumber;
    this.movieDetailHtmlFragmentExtractor = movieDetailHtmlFragmentExtractor;
    this.movieDetailHtmlFragmentExtractorGroupNumber = movieDetailHtmlFragmentExtractorGroupNumber;
    this.stylesheetPath = stylesheetPath;
    this.transforms = transforms;
  }

  public void crawl() throws IOException {
    for (int i = 1; i <= totalPageNumber; i++) {
      crawlSinglePage(i);
    }
  }

  private void crawlSinglePage(int pageNumber) throws IOException {
    String pageFullLink = getPageFullLink(
        this.baseUrl,
        this.pageUrlTemplate,
        pageNumber
    );

    BufferedReader reader = toBufferedReader(connect(pageFullLink));

    if (reader.ready()) {
      String html = normalize(reader, "");

      Pattern pattern = Pattern.compile(detailLinkExtractor);
      Matcher matcher = pattern.matcher(html);

      while (matcher.find()) {
        String movieDetailLink = matcher.group(detailLinkExtractorGroupNumber);

        ThreadUtils.start(
            new MovieDetailSpider(
                movieDetailLink,
                this.movieDetailHtmlFragmentExtractor,
                this.movieDetailHtmlFragmentExtractorGroupNumber,
                getResource(stylesheetPath),
                this.transforms
            )
        );
      }
    }
  }

  @Override
  public void run() {
    try {
      crawl();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String getPageFullLink(String baseUrl, String pageUrlTemplate, int pageNumber) {
    return String.format(baseUrl + pageUrlTemplate, pageNumber);
  }
}
