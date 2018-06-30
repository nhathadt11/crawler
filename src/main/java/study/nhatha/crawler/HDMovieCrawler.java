package study.nhatha.crawler;

import study.nhatha.middleware.*;
import study.nhatha.util.AppConstants;

import java.util.Arrays;
import java.util.List;

public class HDMovieCrawler extends AbstractMovieCrawler {
  private static List<TransformerMiddleware.Transform> transforms;

  static {
    transforms = getTransformLogicList();
  }

  private HDMovieCrawler(String baseUrl,
                         String pageUrlTemplate,
                         String detailLinkExtractor,
                         int detailLinkExtractorGroupNumber,
                         String movieDetailHtmlFragmentExtractor,
                         int movieDetailHtmlFragmentExtractorGroupNumber,
                         String stylesheetPath,
                         List<TransformerMiddleware.Transform> transforms) {
    super(
        baseUrl,
        pageUrlTemplate,
        detailLinkExtractor,
        detailLinkExtractorGroupNumber,
        movieDetailHtmlFragmentExtractor,
        movieDetailHtmlFragmentExtractorGroupNumber,
        stylesheetPath,
        transforms
    );
  }

  public HDMovieCrawler() {
    this(
        "http://hdonline.vn/xem-phim-hoat-hinh/",
        "trang-%d.html",
        "<div class=\"tn-bxitem\"><a href=\"(.+?)\"",
        1,
        "(<div class=\"block-movie\".+?<\\/div>.+?)<div class=\"block-movie\"",
        1,
        AppConstants.HD_MOVIE_STYLE_SHEET,
        transforms
    );
  }

  private static List<TransformerMiddleware.Transform> getTransformLogicList() {
    return Arrays.asList(
        new ImageTagHandler(),
        new UnwantedClosingATagHandler(),
        new ReplaceAllHandler("itemscope", ""),
        BoundaryHandler.before("<!DOCTYPE div [<!ENTITY nbsp \"&#160;\">]>")
    );
  }
}
