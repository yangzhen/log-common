package com.yxy.common.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yxy.common.AppException;
import com.yxy.common.RES_STATUS;
import com.yxy.common.Result;
import com.yxy.common.ThemisContext;
import com.yxy.common.bean.ThemisLog;
import com.yxy.common.constants.AppConstant;
import com.yxy.common.logger.Logger;
import com.yxy.common.logger.LoggerFactory;
import com.yxy.common.utils.RequestUtil;
import java.lang.reflect.Method;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.interceptor.ExposeInvocationInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author yangxinyan
 * @date 2019/9/25 17:27
 */
@Aspect
@Component
public class WebControllerAop {

  private static final Logger stat = LoggerFactory.getLogger(AppConstant.THEMIS_STAT);

  private static final Logger log = LoggerFactory.getLogger(WebControllerAop.class);

  @Pointcut("@within(org.springframework.web.bind.annotation.RestController) or @within(org.springframework.web.bind.annotation.Controller)")
  public void shareCut() {

  }

  @AfterReturning(pointcut="shareCut()", returning = "retVal")
  public void around(JoinPoint jp, Object retVal) {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    if(request == null) {
      return ;
    }
    Integer code = null;
    if (retVal instanceof Result<?>) {
      code = ((Result) retVal).getCode();
    } else {
      code = RES_STATUS.SUCCESS.code;
    }

    pringThemisLogStat(retVal, request, code);

    return ;
  }

  @AfterThrowing(pointcut="shareCut()", throwing = "error")
  public void around(JoinPoint jp, Throwable error) {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    if(request == null) {
      return ;
    }
    Integer code = null;
    if (error instanceof AppException) {
      code = ((AppException) error).getCode();
    } else {
      code = RES_STATUS.SERVER_UNKONW_ERROR.code;
    }

    pringThemisLogStat(null,request, code);

    return ;
  }

  private void pringThemisLogStat(Object retVal, HttpServletRequest request, Integer code) {
    if(log.isDebugEnabled()) {
      MethodInvocation mi = ExposeInvocationInterceptor.currentInvocation();
      Method method = mi.getMethod();
      log.debug("拦截打印方法,method:" + method.toString());
    }

    JSONObject json = new JSONObject();
    json.put("request", RequestUtil.getHttpParameter(request));
    json.put("requestBody", RequestUtil.getRequestBody(request));
    json.put("response", retVal);

    long cost = System.currentTimeMillis() - ThemisContext.getOrNewInstance().getStartTime();
    ThemisLog themisLog = new ThemisLog();
    themisLog.setTime(new Date());
    themisLog.setAppName(AppConstant.APP_NAME);
    themisLog.setTraceId(ThemisContext.getOrNewInstance().getTraceId());
    themisLog.setClientIp(RequestUtil.getIp(request));
    themisLog.setMethod(request.getRequestURI());
    themisLog.setCost(cost);
    themisLog.setBusinessLog(json);
    themisLog.setCode(code);
    themisLog.setSuccess(code == RES_STATUS.SUCCESS.code);

    stat.info(JSON.toJSONString(themisLog));
  }

}
