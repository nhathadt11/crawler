package study.nhatha.util;

import java.io.*;

public final class StreamUtils {
  public static InputStream toInputStream(ByteArrayOutputStream outputStream) {
    return new ByteArrayInputStream(outputStream.toByteArray());
  }

  private StreamUtils() {
  }
}
