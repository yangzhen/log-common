package com.yxy.common.config;

import com.yxy.common.constants.AppConstant;
import com.yxy.common.utils.PropertiesUtil;
import java.util.Objects;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;

/**
 * @author yangxinyan
 * @date 2019/10/13 17:18
 */
public enum ClientConfigManager {

  INSTANCE;

  private static final String resourceName = "application.properties";

  private String systemName;

  private Boolean appLog = true;

  private ClientConfigManager() {
    load();
  }

  public void load() {
    this.systemName = PropertiesUtil.getPropertiesKey(resourceName, AppConstant.APP_SYSTENAME);
    if (StringUtils.isBlank(this.systemName)) {
      this.systemName = PropertiesUtil.getPropertiesKey(resourceName, AppConstant.APP_ID);
    }

    String appLogStr = PropertiesUtil.getPropertiesKey(resourceName, AppConstant.APP_LOG);
    if (StringUtils.isNotBlank(appLogStr) && Objects.equals(appLogStr.toLowerCase(), "false")) {
      appLog = false;
    }

  }


  public String getAppName() {
    return systemName;
  }

  public Boolean getAppLog() {
    return appLog;
  }

  public void setAppLog(Boolean appLog) {
    this.appLog = appLog;
  }
}
