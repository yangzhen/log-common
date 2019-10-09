package com.yxy.common;


public class Result<T> {

  /**
   * 对外返回的对象
   */
  private T data;

  /**
   * 返回状态码
   */
  private int code = RES_STATUS.FAILED.code;

  /**
   * 返回消息
   */
  private String msg = RES_STATUS.FAILED.msg;


  public Result() {
    super();
  }

  public Result(RES_STATUS status) {
    super();
    this.code = status.code;
    this.msg = status.msg;
  }

  public Result(int code, String msg) {
    super();
    this.code = code;
    this.msg = msg;
  }

  public Result(T data, int code, String msg) {
    super();
    this.data = data;
    this.code = code;
    this.msg = msg;
  }

  public static <T> Result<T> error(int code, String msg) {
    return new Result<>(code, msg);
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
    this.msg = status.msg;
  }

  public boolean isSuccess() {
    return this.code == RES_STATUS.SUCCESS.code;
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

  public String getMsg() {
    return this.msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  /**
   * 服务器unix utc时间戳秒值
   */
  public long getTimestamp() {
    return System.currentTimeMillis() / 1000;
  }

  @Override
  public String toString() {
    return "Result{" +
        "data=" + data +
        ", code=" + code +
        ", msg='" + msg + '\'' +
        '}';
  }
}
