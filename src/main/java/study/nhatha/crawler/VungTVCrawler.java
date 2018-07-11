package study.nhatha.crawler;

import study.nhatha.middleware.ReplaceAllHandler;
import study.nhatha.middleware.TransformerMiddleware;

import java.util.Arrays;
import java.util.List;

import static study.nhatha.util.AppConstants.VUNG_TV_MOVIE_STYLE_SHEET;
import static study.nhatha.util.AppConstants.VUNG_TV_MOVIE_TOTAL_PAGE_NUMBER;

public class VungTVCrawler extends AbstractMovieCrawler {
  private static List<TransformerMiddleware.Transform> transforms;

  static {
    transforms = getTransformLogicList();
  }

  public VungTVCrawler() {
    super(
        "http://vung.tv",
        "/filter/page/%d?the-loai=hanh-dong",
        VUNG_TV_MOVIE_TOTAL_PAGE_NUMBER,
        "<a href=\"(.*?)\" class=\".*?film-small\">",
        1,
        "(<div class=\"group-detail\".+?)<div class=\"group-vote-detail\">",
        1,
        VUNG_TV_MOVIE_STYLE_SHEET,
        transforms);
  }

  private static List<TransformerMiddleware.Transform> getTransformLogicList() {
    return Arrays.asList(
        new ReplaceAllHandler("itemscope", ""),
        new ReplaceAllHandler("<script.+?</script>", ""),
        new ReplaceAllHandler("<br>", "<br/>")
    );
  }
}
