package com.amazonaws.kda.sample.mwaa.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppProperties {

    private InputStream inputStream;
    private static final String PROPERTIES_FILE_NAME = "config.properties";

    public String getProperty(String key) throws IOException {
        Properties props = new Properties();
        try {
            inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);
            if (inputStream != null) {
                props.load(inputStream);
            } else {
                throw new FileNotFoundException("Property file '"+PROPERTIES_FILE_NAME+"' not found in CLASSPATH");
            }
        } catch (Exception e) {
            System.err.println("Exception: "+e);
        } finally {
            inputStream.close();
        }
        return props.getProperty(key);
    }

}
