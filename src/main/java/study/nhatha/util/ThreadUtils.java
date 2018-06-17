package study.nhatha.util;

public class ThreadUtils {

  public static void start(Runnable runnable) {
    new Thread(runnable).start();
  }

  private ThreadUtils() {
  }
}
