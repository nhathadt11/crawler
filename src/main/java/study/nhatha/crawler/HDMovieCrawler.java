package study.nhatha.crawler;

import study.nhatha.middleware.*;

import java.util.Arrays;
import java.util.List;

import static study.nhatha.util.AppConstants.HD_MOVIE_STYLE_SHEET;
import static study.nhatha.util.AppConstants.HD_MOVIE_TOTAL_PAGE_NUMBER;

public class HDMovieCrawler extends AbstractMovieCrawler {
  private static List<TransformerMiddleware.Transform> transforms;

  static {
    transforms = getTransformLogicList();
  }

  public HDMovieCrawler() {
    super(
        "http://hdonline.vn/xem-phim-hoat-hinh/",
        "trang-%d.html",
        HD_MOVIE_TOTAL_PAGE_NUMBER,
        "<div class=\"tn-bxitem\"><a href=\"(.+?)\"",
        1,
        "(<div class=\"block-movie\".+?<\\/div>.+?)<div class=\"block-movie\"",
        1,
        HD_MOVIE_STYLE_SHEET,
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
