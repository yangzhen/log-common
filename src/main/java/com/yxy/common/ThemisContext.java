package com.yxy.common;

/**
 * @author yangxinyan
 * @date 2019/3/14 19:10
 */
public class ThemisContext {

  private static final ThreadLocal<ThemisContext> THREAD_LOCAL = new ThreadLocal<>();

  private long startTime;
  private String traceId;
  private String appName;

  public static ThemisContext getOrNewInstance() {
    if (THREAD_LOCAL.get() == null) {
      ThemisContext context = new ThemisContext();
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

  public String getAppName() {
    return appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }
}
