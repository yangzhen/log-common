package com.yxy.common.utils;

import com.yxy.common.logger.Logger;
import com.yxy.common.logger.LoggerFactory;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author yangxinyan
 * @date 2019/6/5 16:11
 */
public class PropertiesUtil {

  private static final Logger log = LoggerFactory.getLogger(PropertiesUtil.class);

  public static String getPropertiesKey(String resourceName, String key) {
      Properties properties = getProperties(resourceName);
    return properties.getProperty(key);
  }

  public static Properties  getProperties(String resourceName) {

    ClassLoader classLoaderToUse = getDefaultClassLoader();
    try {
      InputStream inputStream = (classLoaderToUse != null ? classLoaderToUse.getResourceAsStream(resourceName) :
          ClassLoader.getSystemResourceAsStream(resourceName));
      Properties props = new Properties();
      props.load(inputStream);
      return props;
    } catch (Exception e) {
      log.error("=====>load properties error,resourceName:" + resourceName, e);
      throw new RuntimeException(e);
    }

  }

  private static ClassLoader getDefaultClassLoader() {
    ClassLoader cl = null;
    try {
      cl = Thread.currentThread().getContextClassLoader();
    }
    catch (Throwable ex) {
      // Cannot access thread context ClassLoader - falling back...
    }
    if (cl == null) {
      // No thread context class loader -> use class loader of this class.
      cl = PropertiesUtil.class.getClassLoader();
      if (cl == null) {
        // getClassLoader() returning null indicates the bootstrap ClassLoader
        try {
          cl = ClassLoader.getSystemClassLoader();
        }
        catch (Throwable ex) {
          // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
        }
      }
    }
    return cl;
  }

}
