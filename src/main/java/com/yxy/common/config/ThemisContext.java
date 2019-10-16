package com.yxy.common.config;

import javax.servlet.http.HttpServletRequest;

public class ThemisContext {

  private static final ThreadLocal<ThemisContext> THREAD_LOCAL = new ThreadLocal<>();

  private Long startTime;

  private String traceId;

  private String appName;

  private HttpServletRequest request;

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


  public Long getStartTime() {
    return startTime;
  }

  public void setStartTime(Long startTime) {
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

  public HttpServletRequest getRequest() {
    return request;
  }

  public void setRequest(HttpServletRequest request) {
    this.request = request;
  }
}
