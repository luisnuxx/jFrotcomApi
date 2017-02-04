package com.luisnuxx.frotcom.utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Properties;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.HashMap;


/**
 * Created by luisnuxx on 12/10/2016.
 */
public class FileSystem {
    private static final Logger LOGGER = LogManager.getLogger();
    @org.jetbrains.annotations.NotNull
    public static String getWorkingDirectory() {
        return System.getProperty("user.dir").toString();
    }


    public static HashMap<String, String> getConfigSettings(String filename) {
        HashMap<String, String> hmap = new HashMap<String, String>();

        Properties properties = new Properties();

        ArrayList<String> list = new ArrayList<>();

        try {
            properties.load(new FileInputStream(filename));
        } catch (IOException e) {
            LOGGER.error("Error loading INI file:: {}",filename);

        }
        for(String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            hmap.put(key,value);
        }
        return hmap;
    }
}
