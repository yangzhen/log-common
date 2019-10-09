package com.yxy.common.utils;

import com.yxy.common.config.RequestWrapper;
import com.yxy.common.logger.Logger;
import com.yxy.common.logger.LoggerFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;

public class RequestUtil {

  private static final Logger log = LoggerFactory.getLogger(RequestUtil.class);

  private static String getClientIp(HttpServletRequest request) {
    String ip = request.getHeader("NS-Client-IP");

    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("x-forwarded-for");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }

    return ip;
  }

  public static String getIp(HttpServletRequest request) {
    String ip = getClientIp(request);

		/* 注册ip长度限制为15 */
    if (ip != null && ip.length() > 15) {
      //**.***.***.***".length() = 15
      if (ip.indexOf(",") > 0) {
        ip = ip.substring(0, ip.indexOf(","));
      }
    }

    return ip;
  }

  public static Map<String, String> getHttpParameter(HttpServletRequest request) {
    Map<String, String[]> map = request.getParameterMap();
    Map<String, String> paramMap = new HashMap<String, String>();
    for (Entry<String, String[]> entry : map.entrySet()) {
      String name = entry.getKey();
      String value[] = entry.getValue();
      if (value.length > 1) {
        log.debug("http request param has too many value,name:" + name);
        continue;
      }
      paramMap.put(name, value[0]);
    }
    return paramMap;
  }

  public static String getRequestBody(HttpServletRequest request) {
    if(request instanceof RequestWrapper) {
      RequestWrapper requestWrapper = (RequestWrapper) request;
      return requestWrapper.getBody();
    }
    log.debug("====>HttpServletRequest is not RequestWrapper");
    return null;
  }

}