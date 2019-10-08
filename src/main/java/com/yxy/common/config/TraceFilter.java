package com.yxy.common.config;

import com.yxy.common.AppConstant;
import com.yxy.common.Context;
import com.yxy.common.logger.Logger;
import com.yxy.common.logger.LoggerFactory;
import com.yxy.common.utils.RequestUtil;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yangxinyan
 * @date 2019/9/20 13:18
 */
public class TraceFilter implements Filter {

    private FilterConfig filterConfig = null;

    private Logger logger = LoggerFactory.getLogger(TraceFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        filterConfig = filterConfig;
    }

    @Override
    public void destroy() {
        logger.info("##################### web context is destroy");
        filterConfig = null;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException {

        Context.getOrNewInstance().setStartTime(System.currentTimeMillis());

        String traceId = UUID.randomUUID().toString();
        Context.getOrNewInstance().setTraceId(traceId);

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.addHeader(AppConstant.TRACE_ID, traceId);

        String clientId = RequestUtil.getIp(httpServletRequest);
        Context.getOrNewInstance().setClientId(clientId);

        try {
            logger.debug("======== start in,uri:" + httpServletRequest.getRequestURI());
            chain.doFilter(request, response);
        } finally {
            logger.debug("======== end out,uri:" + httpServletRequest.getRequestURI());
            Context.getOrNewInstance().remove();
        }
    }
}
