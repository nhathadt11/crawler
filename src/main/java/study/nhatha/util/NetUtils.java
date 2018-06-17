package study.nhatha.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public final class NetUtils {

  public static InputStream connect(String urlString) throws IOException {
    URL url = new URL(urlString);
    URLConnection connection = url.openConnection();

    connection.setRequestProperty(
        "User-Agent",
        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11"
    );

    return connection.getInputStream();
  }

  public static BufferedReader toBufferedReader(InputStream inputStream) {
    return new BufferedReader(new InputStreamReader(inputStream));
  }

  private NetUtils() {
  }
}
