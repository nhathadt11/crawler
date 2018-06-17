package study.nhatha.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.stream.Collectors;

public final class StringUtils {

  public static String normalize(BufferedReader reader) {
    return normalize(reader, "\n");
  }

  public static String normalize(BufferedReader reader, String lineDelimiter) {
    return reader
        .lines()
        .collect(Collectors.joining(lineDelimiter));
  }

  public static String remove(String content, String regex) {
    return content.replaceAll(regex, "");
  }

  public static InputStream toInputStream(String content) {
    return new ByteArrayInputStream(content.getBytes());
  }

  private StringUtils() {
  }
}
