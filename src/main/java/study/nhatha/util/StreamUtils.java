package study.nhatha.util;

import java.io.*;

public class StreamUtils {
  public static InputStream toInputStream(ByteArrayOutputStream outputStream) {
    return new ByteArrayInputStream(outputStream.toByteArray());
  }

  private StreamUtils() {
  }
}
