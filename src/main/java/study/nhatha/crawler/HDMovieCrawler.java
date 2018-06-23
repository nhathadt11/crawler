package study.nhatha.crawler;

import study.nhatha.util.AppConstants;

public class HDMovieCrawler extends AbstractMovieCrawler {

  private HDMovieCrawler(String baseUrl,
                         String pageUrlTemplate,
                         String detailLinkExtractor,
                         int detailLinkExtractorGroupNumber,
                         String stylesheetPath) {
    super(baseUrl, pageUrlTemplate, detailLinkExtractor, detailLinkExtractorGroupNumber, stylesheetPath);
  }

  public HDMovieCrawler() {
    this(
        "http://hdonline.vn/xem-phim-hoat-hinh/",
        "trang-%d.html",
        "<div class=\"tn-bxitem\"><a href=\"(.+?)\"",
        1,
        AppConstants.HD_MOVIE_STYLE_SHEET
    );
  }
}
