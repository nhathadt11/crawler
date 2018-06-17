package study.nhatha;

import study.nhatha.crawler.Crawlable;
import study.nhatha.crawler.HDMovieCrawler;
import study.nhatha.util.ThreadUtils;

public class App {
  public static void main( String[] args ) {
    Crawlable hdMovieCrawler = new HDMovieCrawler();

    ThreadUtils.start(hdMovieCrawler);
  }
}
