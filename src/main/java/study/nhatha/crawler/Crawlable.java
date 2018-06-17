package study.nhatha.crawler;

import java.io.IOException;

public interface Crawlable extends Runnable {
  void crawl() throws IOException;
}
