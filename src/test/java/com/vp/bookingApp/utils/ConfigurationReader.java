package com.vp.bookingApp.utils;

import java.io.*;
import java.util.*;

public class ConfigurationReader {
    private static Properties properties = new Properties();
    static{
        try {
            FileInputStream file = new FileInputStream("configuration.properties");
            properties.load(file);
            file.close();
        } catch (IOException e) {
            System.out.println("Properties File not found");
        }
    }
    public static String getProperty(String keyWord){
        return properties.getProperty(keyWord);
    }
}
