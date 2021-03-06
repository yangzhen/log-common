package com.yxy.common.utils;


import com.yxy.common.RES_STATUS;

public class AppException extends RuntimeException {

  public AppException() {
    super();
  }

  public AppException(int code, String msg) {
    super(msg);
    this.code = code;
    this.msg = msg;
  }

  public AppException(RES_STATUS info) {
    super(info.msg);
    this.code = info.code;
    this.msg = info.msg;
  }

  private int code;
  private String msg;

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }
}
