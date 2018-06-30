package study.nhatha;

import study.nhatha.crawler.Crawlable;
import study.nhatha.crawler.HDMovieCrawler;
import study.nhatha.crawler.VungTVCrawler;
import study.nhatha.util.ThreadUtils;

public class CrawlerApp {
  public static void main( String[] args ) {
    Crawlable hdMovieCrawler = new HDMovieCrawler();
    Crawlable vungTVMovieCrawler = new VungTVCrawler();

    ThreadUtils.start(hdMovieCrawler);
    ThreadUtils.start(vungTVMovieCrawler);
  }
}
