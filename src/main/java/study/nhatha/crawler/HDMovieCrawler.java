package study.nhatha.crawler;

import study.nhatha.util.AppConstants;

public class HDMovieCrawler extends AbstractMovieCrawler {

  private HDMovieCrawler(String baseUrl,
                         String pageUrlTemplate,
                         String detailLinkExtractor,
                         int detailLinkExtractorGroupNumber,
                         String movieDetailHtmlFragmentExtractor,
                         String stylesheetPath) {
    super(
        baseUrl,
        pageUrlTemplate,
        detailLinkExtractor,
        detailLinkExtractorGroupNumber,
        movieDetailHtmlFragmentExtractor,
        stylesheetPath
    );
  }

  public HDMovieCrawler() {
    this(
        "http://hdonline.vn/xem-phim-hoat-hinh/",
        "trang-%d.html",
        "<div class=\"tn-bxitem\"><a href=\"(.+?)\"",
        1,
        "(<div class=\"block-movie\".+?<\\/div>.+?)<div class=\"block-movie\"",
        AppConstants.HD_MOVIE_STYLE_SHEET
    );
  }
}
