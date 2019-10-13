package com.yxy.common.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yxy.common.RES_STATUS;
import com.yxy.common.annoation.LogItem;
import com.yxy.common.annoation.LogMapping;
import com.yxy.common.bean.ThemisLog;
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
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
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
public class ThemisLogAop {

  private static final Logger stat = LoggerFactory.getLogger(AppConstant.THEMIS_STAT);

  private static final Logger log = LoggerFactory.getLogger(ThemisLogAop.class);

  @Pointcut("@annotation(com.hwl.themis.log.annoation.LogMapping)")
  public void shareCut() {

  }

  @Around("shareCut()")
  public Object around(ProceedingJoinPoint jp) {
    Object retVal = null;
    Integer code = 0;
    String traceId = ThemisContext.getOrNewInstance().getTraceId();
    traceId = Optional.ofNullable(traceId).orElse(ThemisUtill.getUUID());
    ThemisContext.getOrNewInstance().setTraceId(traceId);

    long start = System.currentTimeMillis();
    try {
      retVal = jp.proceed();
    } catch (AppException exception) {
      code = exception.getCode();
      throw exception;
    } catch (Throwable throwable) {
      code = RES_STATUS.SERVER_UNKONW_ERROR.code;
      throwable.printStackTrace();
    }
    MethodInvocation mi = ExposeInvocationInterceptor.currentInvocation();
    Method method = mi.getMethod();
    LogMapping logMapping = method.getAnnotation(LogMapping.class);
    if(log.isDebugEnabled()) {
      log.debug("拦截打印方法,method:" + method.toString());
    }

    JSONObject json = new JSONObject();
    String ip = NetUtil.getLocalHost();

    if(logMapping.item() == LogItem.ALL || logMapping.item() == LogItem.REQUEST) {
      HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
      if(request != null) {
        ip = RequestUtil.getIp(request);
        json.put("request", RequestUtil.getHttpParameter(request));
        json.put("requestBody", RequestUtil.getRequestBody(request));
      } else {
        Object[] args = jp.getArgs();
        json.put("request", args);
      }
    }

    if(logMapping.item() == LogItem.ALL || logMapping.item() == LogItem.RESPONSE) {
      json.put("response", retVal);
    }

    long cost = System.currentTimeMillis() - start;
    ThemisLog themisLog = new ThemisLog();
    themisLog.setTime(new Date());
    themisLog.setAppName(ClientConfigManager.INSTANCE.getAppName());
    themisLog.setTraceId(ThemisContext.getOrNewInstance().getTraceId());
    themisLog.setClientIp(ip);
    themisLog.setMethod(method.getName());
    themisLog.setCost(cost);
    themisLog.setBusinessLog(json);
    themisLog.setCode(code);
    themisLog.setSuccess(code == RES_STATUS.SUCCESS.code);

    stat.info(JSON.toJSONString(themisLog));

    return retVal;
  }
  
public static void main(String[] args) {
	String trace = null;
	String traceId = Optional.ofNullable(trace).orElse(ThemisUtill.getUUID());
	System.out.println(traceId);
}

}
