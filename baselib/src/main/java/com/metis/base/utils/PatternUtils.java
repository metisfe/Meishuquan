package com.metis.base.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Beak on 2015/10/21.
 */
public class PatternUtils {
    public static final String REX_UID_CHECKING = "[\\?|\\&]uid=\\d+";

    public static final Pattern PATTERN_UID_CHECKING = Pattern.compile(REX_UID_CHECKING);

    public static String deleteUidInfosInUrl (String url) {
        if (url == null) {
            return "";
        }
        Matcher matcher = PATTERN_UID_CHECKING.matcher(url);
        if (matcher.find()) {
            String group = matcher.group();
            if (group.contains("?")) {
                url = url.replaceAll(REX_UID_CHECKING, "?shabi=you");
            } else {
                url = url.replaceAll(REX_UID_CHECKING, "&shabi=you");
            }
        }
        return url;
    }
}
