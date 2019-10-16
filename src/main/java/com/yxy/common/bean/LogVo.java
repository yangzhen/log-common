package com.yxy.common.bean;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.Date;

public class LogVo {
    /**
     * 本次请求ip
     */
    private String ip;

    private String traceId;
    /**
     * 接口路径
     */
    private String path;
    /**
     * 请求开始时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /**
     * 请求结束时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    /**
     * 请求时长
     */
    private Long executeTime;
    /**
     * 接口描述
     */
    private String desc;
    /**
     * 系统标记（paper paperRest）
     */
    private String systemName;
    /**
     * 错误信息（参数为空，id不存在等）
     */
    private String errorMessage;

    /**
     * 请求参数
     */
    private Object params;

    /**
     * 本次执行异常码
     */
    private Integer code;

    /**
     * 请求是否成功
     */
    private Boolean success;

    /**
     * 本次执行方法返回结果
     */
    private Object response;

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getExecuteTime() {
        if (executeTime == null && startTime != null && endTime != null) {
            executeTime = endTime.getTime() - startTime.getTime();
        }
        return executeTime;
    }

    public void setExecuteTime(Long executeTime) {
        this.executeTime = executeTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Object getParams() {
        return params;
    }

    public void setParams(Object params) {
        this.params = params;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }
}
