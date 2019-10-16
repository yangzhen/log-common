package com.yxy.common.config;

import com.yxy.common.constants.AppConstant;
import com.yxy.common.logger.Logger;
import com.yxy.common.logger.LoggerFactory;
import com.yxy.common.utils.ThemisUtill;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

/**
 * @author yangxinyan
 * @date 2019/9/20 13:18
 */
public class TraceFilter implements Filter {

  private FilterConfig filterConfig = null;

  private Logger log = LoggerFactory.getLogger(TraceFilter.class);

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    filterConfig = filterConfig;
  }

  @Override
  public void destroy() {
    filterConfig = null;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) throws IOException, ServletException {

    HttpServletRequest requestWrapper = new RequestWrapper((HttpServletRequest) request);
    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
    ThemisContext.getOrNewInstance().setRequest(requestWrapper);

    ThemisContext.getOrNewInstance().setStartTime(System.currentTimeMillis());

    String traceId = requestWrapper.getHeader(AppConstant.TRACE_ID);
    if(StringUtils.isBlank(traceId)) {
      traceId = ThemisUtill.getUUID();
    }
    ThemisContext.getOrNewInstance().setTraceId(traceId);



    httpServletResponse.addHeader(AppConstant.TRACE_ID, traceId);
    try {
      log.debug("======== Log TraceFilter start in,uri:" + requestWrapper.getRequestURI());
      chain.doFilter(request, response);
    } finally {
      log.debug("======== Log TraceFilter end out,uri:" + requestWrapper.getRequestURI());
      ThemisContext.getOrNewInstance().remove();
    }
  }

}
