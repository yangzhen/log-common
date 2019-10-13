package com.yxy.common.config;

import com.yxy.common.constants.AppConstant;
import com.yxy.common.utils.PropertiesUtil;

/**
 * @author yangxinyan
 * @date 2019/10/13 17:18
 */
public enum ClientConfigManager {

  INSTANCE
  ;

  private String appName;

  private ClientConfigManager(){
    load();
  }

  public void load() {
    String resourceName = "application.properties";
    String key = AppConstant.APP_NAME;
    this.appName = PropertiesUtil.getPropertiesKey(resourceName, key);
  }

  public String getAppName() {
    return appName;
  }


}
