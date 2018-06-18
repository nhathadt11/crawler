package study.nhatha.util;

public class ThreadUtils {

  public static Thread start(Runnable runnable) {
    Thread thread = new Thread(runnable);

    thread.start();

    return thread;
  }

  private ThreadUtils() {
  }
}
