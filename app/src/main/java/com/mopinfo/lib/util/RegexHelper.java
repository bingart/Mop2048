package com.mopinfo.lib.util;

import com.mopinfo.lib.log.ILogger;
import com.mopinfo.lib.log.LogMannger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mop on 2016/12/11.
 */
public class RegexHelper {

    private static ILogger LOGGER = LogMannger.getInstance().getLogger(RegexHelper.class);

    public static boolean isMatched(String input, String pattern) {
        try {
            if (pattern != null && pattern.length() > 0) {
                Pattern p = Pattern.compile(pattern);
                Matcher m = p.matcher(input);
                if (m.find()) {
                    return true;
                }
            }
        } catch (Exception ex) {
            LOGGER.error("isMatched error, " + ex.getMessage());
        }

        return false;
    }

    public static String search(String input, String pattern) {
        try {
            if (pattern != null && pattern.length() > 0) {
                Pattern p = Pattern.compile(pattern);
                Matcher m = p.matcher(input);
                if (m.find()) {
                    return m.group(1);
                }
            }
        } catch (Exception ex) {
            LOGGER.error("search error, " + ex.getMessage());
        }

        return null;
    }
}
