package com.yxy.common;

import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.junit.Test;

/**
 * @author yangxinyan
 * @date 2019/10/13 18:49
 */
public class Hhh {


  @Test
  public void test() {
    RollingFileAppender appender = RollingFileAppender.newBuilder()
        .withAppend(true)
        .build();
  }
}

