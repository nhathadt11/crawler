package study.nhatha.crawler;

public class HDMovieCrawler extends AbstractMovieCrawler {

  private HDMovieCrawler(String baseUrl,
                         String pageUrlTemplate,
                         String detailLinkExtractor,
                         int detailLinkExtractorGroupNumber) {
    super(baseUrl, pageUrlTemplate, detailLinkExtractor, detailLinkExtractorGroupNumber);
  }

  public HDMovieCrawler() {
    this(
        "http://hdonline.vn/xem-phim-hoat-hinh/",
        "trang-%d.html",
        "<div class=\"tn-bxitem\"><a href=\"(.+?)\"",
        1
    );
  }
}
