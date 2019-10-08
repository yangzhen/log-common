package com.yxy.common;

public enum RES_STATUS {

  FAILED(-1, "未知错误"),

  SUCCESS(0, "SUCCESS"),

  USER_NOT_LOGIN_ERROR(400, "您好您必须登录才能操作"),

  SERVER_UNKONW_ERROR(500, "服务器开小差了,请稍后再试"),

  BAD_PARAM(410, "参数异常"),

  DB_ERROR(10000, "DB操作失败"),

  USER_HAS_BAN(11000, "账号被封禁"),

  USER_ACCOUNT_PASS_ERROR(11001, "用户名或密码错误"),

  ENTRY_HAS_EXIST(12000, "实体项已存在，请勿重复添加"),


  THIRD_HAS_ERROR(71000, "第三方服务器异常"),

  ;

  public final int code;
  public final String msg;

  RES_STATUS(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public static RES_STATUS findStatusByCode(int code) {
    for (RES_STATUS status : RES_STATUS.values()) {
      if (status.code == code) {
        return status;
      }
    }
    return null;
  }


  /**
   * success:Y not success:N
   */
  public static String isSuccess(int code) {
    if (code == RES_STATUS.SUCCESS.code) {
      return AppConstant.METHOD_SUCCESS;
    } else {
      return AppConstant.METHOD_FAIL;
    }
  }
}
