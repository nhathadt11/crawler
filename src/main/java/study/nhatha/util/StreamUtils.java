package study.nhatha.util;

import java.io.*;
import java.util.stream.Collectors;

public final class StreamUtils {
  public static InputStream toInputStream(ByteArrayOutputStream outputStream) {
    return new ByteArrayInputStream(outputStream.toByteArray());
  }

  public static String toString(InputStream inputStream) {
    return new BufferedReader(new InputStreamReader(inputStream))
        .lines()
        .collect(Collectors.joining());
  }

  private StreamUtils() {
  }
}
