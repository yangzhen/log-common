package com.yxy.common.utils;

import java.util.UUID;

/**
 * @author yangxinyan
 * @date 2019/10/12 22:56
 */
public class ThemisUtill {

  public static String getUUID() {
    return UUID.randomUUID().toString().replace("-", "");
  }
}
