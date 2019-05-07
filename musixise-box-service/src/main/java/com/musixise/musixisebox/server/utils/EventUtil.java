package com.musixise.musixisebox.server.utils;

import org.apache.commons.lang.StringUtils;

public class EventUtil {

    public static Boolean isMotherEvent(String title) {
        if (StringUtils.isNotBlank(title)) {
            return  title.contains("#母亲节");
        } else {
            return false;
        }
    }
}
