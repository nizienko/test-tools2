package ru.yamoney.test.testtools2.common;

import java.text.SimpleDateFormat;

/**
 * User: nizienko
 * Date: 08.09.2015
 * Time: 16:35
 */
public class Utils {
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    public static SimpleDateFormat getDateFormat(){
        return dateFormat;
    }
}
