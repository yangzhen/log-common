package com.yxy.common;

/**
 * @author yangxinyan
 * @date 2019/3/14 19:10
 */
public class Context {

  private static final ThreadLocal<Context> THREAD_LOCAL = new ThreadLocal<>();

  private long startTime;
  private String traceId;
  private String clientId;

  public static Context getOrNewInstance() {
    if (THREAD_LOCAL.get() == null) {
      Context context = new Context();
      THREAD_LOCAL.set(context);
    }
    return THREAD_LOCAL.get();
  }

  public void remove() {
    THREAD_LOCAL.remove();
  }

  public long getStartTime() {
    return startTime;
  }

  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }

  public String getTraceId() {
    return traceId;
  }

  public void setTraceId(String traceId) {
    this.traceId = traceId;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

}
