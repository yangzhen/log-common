package com.yxy.common.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yxy.common.RES_STATUS;
import com.yxy.common.Result;
import com.yxy.common.annoation.LogMapping;
import com.yxy.common.bean.LogVo;
import com.yxy.common.bean.ThemisLog;
import com.yxy.common.config.ClientConfigManager;
import com.yxy.common.config.ThemisContext;
import com.yxy.common.constants.AppConstant;
import com.yxy.common.logger.Logger;
import com.yxy.common.logger.LoggerFactory;
import com.yxy.common.utils.AppException;
import com.yxy.common.utils.RequestUtil;
import java.lang.reflect.Method;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
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

  /**
   * 拦截以Controller结尾类名的所有方法,标有@注解类的方法
   */
  @Pointcut("within(com..*.*Controller) or @within(org.springframework.stereotype.Controller)")
  public void shareCut() {

  }

  @AfterReturning(pointcut="shareCut()", returning = "retVal")
  public void webAfterReturning(JoinPoint jp, Object retVal) {
    if(!ClientConfigManager.INSTANCE.getAppLog()) {
      return;
    }

    MethodInvocation mi = ExposeInvocationInterceptor.currentInvocation();
    Method method = mi.getMethod();
    LogMapping logMapping = method.getAnnotation(LogMapping.class);
    if(logMapping != null) {
      //交给其他代理处理
      return;
    }

    Integer code = null;
    Boolean success = false;
    String errorMessage = null;

    try {
      if (retVal instanceof Result<?>) {
        Result<?> result = (Result<?>) retVal;
        code = result.getCode();
        success = result.getSuccess();
        errorMessage = result.getMessage();
      } else {
        Object object = JSON.toJSON(retVal);
        if(object instanceof JSONObject) {
          JSONObject json = (JSONObject) object;
          code = json.getInteger(AppConstant.CODE);
          success = json.getBoolean(AppConstant.SUCCESS);
          errorMessage = json.getString(AppConstant.MESSAGE);
          if(StringUtils.isBlank(errorMessage)) {
            errorMessage = json.getString(AppConstant.ERROR_MESSAGE);
          }
        } else {
          code = RES_STATUS.SUCCESS.code;
          success = true;
          errorMessage = RES_STATUS.SUCCESS.msg;
        }
      }

      pringThemisLogStat(retVal, code, success, errorMessage);
    } catch (Exception e) {
      log.error("themis skd core print log error", e);
    }
    return ;
  }

  @AfterThrowing(pointcut="shareCut()", throwing = "error")
  public void webAfterThrowing(JoinPoint jp, Throwable error) {
    if(!ClientConfigManager.INSTANCE.getAppLog()) {
      return;
    }

    MethodInvocation mi = ExposeInvocationInterceptor.currentInvocation();
    Method method = mi.getMethod();
    LogMapping logMapping = method.getAnnotation(LogMapping.class);
    if(logMapping != null) {
      //交给其他代理处理
      return;
    }
    HttpServletRequest request = ThemisContext.getOrNewInstance().getRequest();
    Integer code = null;
    Boolean success = false;
    String errorMessage = error.getMessage();
    if (error instanceof AppException) {
      code = ((AppException) error).getCode();
    } else {
      code = RES_STATUS.SERVER_UNKONW_ERROR.code;
    }

    try {
      pringThemisLogStat(null, code, success, errorMessage);
    } catch (Exception e) {
      log.error("themis skd core print log error", e);
    }

    return ;
  }

  private void pringThemisLogStat(Object retval, Integer code, Boolean success, String errorMessage) {
    HttpServletRequest request = ThemisContext.getOrNewInstance().getRequest();

    LogVo logVo = new LogVo();
    Long startTime = ThemisContext.getOrNewInstance().getStartTime();
    logVo.setIp(RequestUtil.getIp(request));
    logVo.setTraceId(ThemisContext.getOrNewInstance().getTraceId());
    logVo.setPath(request.getRequestURI());
    logVo.setStartTime(new Date(startTime));
    logVo.setEndTime(new Date());

    long cost = System.currentTimeMillis() - ThemisContext.getOrNewInstance().getStartTime();
    logVo.setExecuteTime(cost);

    logVo.setSystemName(ClientConfigManager.INSTANCE.getAppName());
    logVo.setErrorMessage(errorMessage);

    JSONObject requestParam = new JSONObject();
    requestParam.putAll(RequestUtil.getHttpParameter(request));
    JSONObject requestBody = JSONObject.parseObject(RequestUtil.getRequestBody(request));
    if(requestBody != null) {
      requestParam.putAll(requestBody);
    }
    logVo.setParams(requestParam);

    logVo.setCode(code);
    logVo.setSuccess(success);

    stat.info(JSON.toJSONString(logVo));
  }

}
