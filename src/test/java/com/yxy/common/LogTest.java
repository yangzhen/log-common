package com.yxy.common;

import com.yxy.common.logger.Logger;
import com.yxy.common.logger.LoggerFactory;
import org.junit.Test;

/**
 * @author yangxinyan
 * @date 2019/10/12 13:54
 */
public class LogTest {

  @Test
  public void test() {
    Logger logger = LoggerFactory.getLogger(LogTest.class);
    logger.info("xxxxxx");

  }
}
