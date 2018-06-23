package study.nhatha.crawler;

import study.nhatha.spider.MovieDetailSpider;
import study.nhatha.util.ThreadUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static study.nhatha.util.NetUtils.connect;
import static study.nhatha.util.NetUtils.toBufferedReader;
import static study.nhatha.util.ResourceUtils.getResource;
import static study.nhatha.util.StringUtils.normalize;

public abstract class AbstractMovieCrawler implements Crawlable {
  private String baseUrl;
  private String pageUrlTemplate;
  private String detailLinkExtractor;
  private int detailLinkExtractorGroupNumber;
  private String stylesheetPath;

  AbstractMovieCrawler(
      String baseUrl,
      String pageUrlTemplate,
      String detailLinkExtractor,
      int detailLinkExtractorGroupNumber,
      String stylesheetPath) {

    this.baseUrl = baseUrl;
    this.pageUrlTemplate = pageUrlTemplate;
    this.detailLinkExtractor = detailLinkExtractor;
    this.detailLinkExtractorGroupNumber = detailLinkExtractorGroupNumber;
    this.stylesheetPath = stylesheetPath;
  }

  public void crawl() throws IOException {
    String pageFullLink = getPageFullLink(
        this.baseUrl,
        this.pageUrlTemplate,
        1
    );

    BufferedReader reader = toBufferedReader(connect(pageFullLink));

    if (reader.ready()) {
      String html = normalize(reader, "");

      Pattern pattern = Pattern.compile(detailLinkExtractor);
      Matcher matcher = pattern.matcher(html);

      while (matcher.find()) {
        String movieDetailLink = matcher.group(detailLinkExtractorGroupNumber);
        String movieDetailHtmlFragmentExtractor = "(<div class=\"block-movie\".+?<\\/div>.+?)<div class=\"block-movie\"";

        ThreadUtils.start(
            new MovieDetailSpider(
                movieDetailLink,
                movieDetailHtmlFragmentExtractor,
                1,
                getResource(stylesheetPath)
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
