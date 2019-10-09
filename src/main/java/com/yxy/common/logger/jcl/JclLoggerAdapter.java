package com.yxy.common.logger.jcl;

import com.yxy.common.logger.Level;
import com.yxy.common.logger.Logger;
import com.yxy.common.logger.LoggerAdapter;
import java.io.File;
import org.apache.commons.logging.LogFactory;

public class JclLoggerAdapter implements LoggerAdapter {

    private Level level;
    private File file;

    @Override
    public Logger getLogger(String key) {
        return new JclLogger(LogFactory.getLog(key));
    }

    @Override
    public Logger getLogger(Class<?> key) {
        return new JclLogger(LogFactory.getLog(key));
    }

    @Override
    public Level getLevel() {
        return level;
    }

    @Override
    public void setLevel(Level level) {
        this.level = level;
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public void setFile(File file) {
        this.file = file;
    }

}
