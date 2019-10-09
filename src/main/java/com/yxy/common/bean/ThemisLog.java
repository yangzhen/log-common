package com.yxy.common.bean;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import java.util.Date;

/**
 * @author yangxinyan
 * @date 2019/9/26 20:27
 */
public class ThemisLog {

  /**
   * 日志打印时间 yyyy-MM-dd hh:mm:ss
   */
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date time;

  /**
   * 应用名字
   */
  private String appName;

  /**
   * 本次请求traceId
   */
  private String traceId;

  /**
   * 本次请求客户端来源ip
   */
  private String clientIp;

  /**
   * 本次请求方法名
   */
  private String method;

  /**
   * 本次请求成功失败表示,true:success,false:fail
   */
  private Boolean success;

  /**
   * 本次请求异常码
   */
  private Integer code;

  /**
   * 本次请求耗时
   */
  private Long cost;

  /**
   * 本次请求业务自定义参数日志
   */
  private JSONObject businessLog;

  public Date getTime() {
    return time;
  }

  public void setTime(Date time) {
    this.time = time;
  }

  public String getAppName() {
    return appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }

  public String getTraceId() {
    return traceId;
  }

  public void setTraceId(String traceId) {
    this.traceId = traceId;
  }

  public String getClientIp() {
    return clientIp;
  }

  public void setClientIp(String clientIp) {
    this.clientIp = clientIp;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public Boolean getSuccess() {
    return success;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public Long getCost() {
    return cost;
  }

  public void setCost(Long cost) {
    this.cost = cost;
  }

  public JSONObject getBusinessLog() {
    return businessLog;
  }

  public void setBusinessLog(JSONObject businessLog) {
    this.businessLog = businessLog;
  }

}
