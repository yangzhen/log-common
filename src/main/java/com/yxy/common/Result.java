package com.yxy.common;


public class Result<T> {

  /**
   * 对外返回的对象
   */
  private T data;

  /**
   * 请求是否成功
   */
  private Boolean success;

  /**
   * 返回状态码
   */
  private Integer code = RES_STATUS.FAILED.code;

  /**
   * 返回消息
   */
  private String message = RES_STATUS.FAILED.msg;


  public Result() {
    super();
  }

  public Result(RES_STATUS status) {
    super();
    this.code = status.code;
    this.message = status.msg;
  }

  public Result(Integer code, String message) {
    super();
    this.code = code;
    this.message = message;
  }

  public Result(T data, Integer code, String message) {
    super();
    this.data = data;
    this.code = code;
    this.message = message;
  }

  public static <T> Result<T> error(Integer code, String message) {
    return new Result<>(code, message);
  }

  public static <T> Result<T> error(RES_STATUS status) {
    return new Result<>(status.code, status.msg);
  }

  public static <T> Result<T> success(T data) {
    return new Result<T>(data, RES_STATUS.SUCCESS.code, RES_STATUS.SUCCESS.msg);
  }

  public static <T> Result<T> success() {
    return new Result<T>(null, RES_STATUS.SUCCESS.code, RES_STATUS.SUCCESS.msg);
  }

  public void setStatus(RES_STATUS status) {
    this.code = status.code;
    this.message = status.msg;
  }

  public T getData() {
    return this.data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public int getCode() {
    return this.code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public Boolean getSuccess() {
    return RES_STATUS.SUCCESS.code.equals(this.code);
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * 服务器unix utc时间戳秒值
   *
   * @return
   */
  public long getTimestamp() {
    return System.currentTimeMillis() / 1000;
  }
}
