package com.yxy.common.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yxy.common.RES_STATUS;
import com.yxy.common.Result;
import com.yxy.common.annoation.LogItem;
import com.yxy.common.annoation.LogMapping;
import com.yxy.common.bean.LogVo;
import com.yxy.common.config.ClientConfigManager;
import com.yxy.common.config.ThemisContext;
import com.yxy.common.constants.AppConstant;
import com.yxy.common.logger.Logger;
import com.yxy.common.logger.LoggerFactory;
import com.yxy.common.utils.AppException;
import com.yxy.common.utils.NetUtil;
import com.yxy.common.utils.RequestUtil;
import com.yxy.common.utils.ThemisUtill;
import java.lang.reflect.Method;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.interceptor.ExposeInvocationInterceptor;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;

/**
 * @author yangxinyan
 * @date 2019/9/25 17:27
 */
@Aspect
@Component
public class ThemisLogAop {

  private static final Logger stat = LoggerFactory.getLogger(AppConstant.THEMIS_STAT);

  private static final Logger log = LoggerFactory.getLogger(ThemisLogAop.class);

  /**
   * 拦截标有此注解类的所有方法，或者标有此注解的方法
   * 方法上的注解会覆盖类注解
   */
  @Pointcut("@within(com.yxy.common.annoation.LogMapping) || @annotation(com.yxy.common.annoation.LogMapping)")
  public void shareCut() {

  }

  @Around("shareCut()")
  public Object around(ProceedingJoinPoint jp) {
    Object retVal = null;
    Integer code = null;
    //适配部分老项目，只有success值，无code
    Boolean success = false;
    //适配部分老项目
    String errorMessage = null;

    String traceId = ThemisContext.getOrNewInstance().getTraceId();
    if(StringUtils.isBlank(traceId)) {
      ThemisContext.getOrNewInstance().setTraceId(ThemisUtill.getUUID());
    }

    long startTime = System.currentTimeMillis();

    try {
      retVal = jp.proceed();
      success = true;
    } catch (AppException exception) {
      code = exception.getCode();
      success = false;
      errorMessage = exception.getMsg();
      throw exception;
    } catch (Throwable throwable) {
      code = RES_STATUS.SERVER_UNKONW_ERROR.code;
      success = false;
      errorMessage = throwable.getMessage();
      throw new RuntimeException(throwable);
    } finally {
      operatorLog(jp, retVal, code, success, errorMessage, startTime);
    }

    return retVal;
  }

  private void operatorLog(ProceedingJoinPoint jp, Object retVal, Integer code, Boolean success,
      String errorMessage,Long startTime) {
    if(!ClientConfigManager.INSTANCE.getAppLog()) {
      return;
    }
    MethodInvocation mi = ExposeInvocationInterceptor.currentInvocation();
    Method method = mi.getMethod();
    Class<?> clasz = method.getDeclaringClass();
    LogMapping logMapping = clasz.getAnnotation(LogMapping.class);
    if(method.isAnnotationPresent(LogMapping.class)) {
      logMapping = method.getAnnotation(LogMapping.class);
    }
    if(logMapping.item() == LogItem.NONE) {
      return;
    }

    if(code == null) {
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
    }

    try {
      LogVo logVo = new LogVo();
      if(logMapping.item() == LogItem.ALL || logMapping.item() == LogItem.REQUEST) {
        JSONObject requestParam = getRequestParam(jp.getArgs(), method, logMapping);
        logVo.setParams(requestParam);
      }
      if(logMapping.item() == LogItem.ALL || logMapping.item() == LogItem.RESPONSE) {
        logVo.setResponse(retVal);
      }
      logVo.setTraceId(ThemisContext.getOrNewInstance().getTraceId());
      logVo.setIp(getIp());
      logVo.setPath(getPath(method));
      long cost = System.currentTimeMillis() - startTime;
      logVo.setStartTime(new Date(startTime));
      logVo.setEndTime(new Date());
      logVo.setExecuteTime(cost);
      logVo.setCode(code);
      logVo.setSuccess(success);
      logVo.setErrorMessage(errorMessage);
      logVo.setSystemName(ClientConfigManager.INSTANCE.getAppName());
      stat.info(JSON.toJSONString(logVo));
    } catch (Exception e) {
      log.error("themis skd core print log error", e);
    }
  }

  private JSONObject getRequestParam(Object[] args, Method method,LogMapping logMapping) {
    JSONObject requestParam = null;
    if(logMapping.item() == LogItem.ALL || logMapping.item() == LogItem.REQUEST) {
      return getRequestParam(method, args);
    }
    return requestParam;
  }

  public JSONObject getRequestParam(Method method, Object[] args) {
    JSONObject requestParam = null;
    if (args != null && args.length > 0) {
      ParameterNameDiscoverer pnd = new LocalVariableTableParameterNameDiscoverer();
      String[] parameterNames = pnd.getParameterNames(method);
      requestParam = new JSONObject();
      for (int i = 0; i < args.length; i++) {
        if (args[i] instanceof HttpServletRequest) {
          continue;
        }
        if (args[i] instanceof HttpServletResponse) {
          continue;
        }
        String parameterName = null;
        if(parameterNames == null){
          parameterName = "param" + i;
        } else {
          parameterName = parameterNames[i];
        }
        requestParam.put(parameterName, args[i]);
      }
    }
    return requestParam;
  }


  private String getIp() {
    HttpServletRequest request = ThemisContext.getOrNewInstance().getRequest();
    if(request != null) {
      return RequestUtil.getIp(request);
    } else {
      return NetUtil.getLocalHost();
    }
  }

  private String getPath(Method method) {
    return method.getDeclaringClass().getName()+"."+method.getName();
  }

}
