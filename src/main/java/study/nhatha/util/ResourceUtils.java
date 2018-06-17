package study.nhatha.util;

import java.io.InputStream;

public final class ResourceUtils {

  public static InputStream getResource(String path) {
    return ResourceUtils.class.getResourceAsStream(path);
  }

  private ResourceUtils() {
  }
}
