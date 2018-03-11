package com.forever7776.sendcloud.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * @author kz
 * @date 2018-03-11
 */
public class ConfigTool {
    private static final Logger log = LoggerFactory.getLogger(ConfigTool.class);

    private static Properties prop = null;
    private static boolean isPropLoaded = false;

    public ConfigTool() {

    }

    public static String getProp(String key) {
        load();

        return prop.getProperty(key);
    }

    private static void load() {
        if (!isPropLoaded) {
            InputStream inputStream = null;
            try {
                prop = new Properties();
                inputStream = new ConfigTool().getClass().getResourceAsStream("/config.properties");
                prop.load(inputStream);
                isPropLoaded = true;
            } catch (Exception e) {
                log.error("读取config.properties异常：{}" + e);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        log.error("关闭读取config.properties文件流异常：{}" + e);
                    }
                }
            }
        }
    }
}
