package com.dmall.blas.collect.util;

import com.dmall.blas.collect.core.MyDataCacher;
import com.dmall.tool.basic.toolkit.BasicToolkit;
import com.dmall.tool.nb.web.SpringAppContextAware;
import com.google.common.base.Strings;

import java.util.List;

/**
 * Created by lrkin on 2017/5/18.
 */
public class GatherToolkit {
    private static MyDataCacher myDataCacher = null;

    public static MyDataCacher dataCacher() {
        if (GatherToolkit.myDataCacher == null) {
            GatherToolkit.myDataCacher = SpringAppContextAware.getSpringBean(MyDataCacher.class);
        }

        return GatherToolkit.myDataCacher;
    }

    public static boolean isNullOrEmpty(String value) {
        if (value == null) {
            return true;
        }

        String trimValue = value.trim();
        if (trimValue.length() == 0) {
            return true;
        }

        if (trimValue.equalsIgnoreCase("null")) {
            return true;
        }

        if (trimValue.equalsIgnoreCase("(null)")) {
            return true;
        }

        if (trimValue.equalsIgnoreCase("undefine")) {
            return true;
        }

        return false;
    }

    public static String formatEmptyStr(String v) {
        if (GatherToolkit.isNullOrEmpty(v)) {
            return "";
        } else {
            return v;
        }
    }

    public static String joinStr(String sepStr, List<String> members) {
        if (members == null || members.isEmpty()) {
            return "";
        }

        boolean fistTime = true;
        StringBuilder sb = new StringBuilder();
        for (String tmp : members) {
            if (!fistTime) {
                sb.append(sepStr);
            } else {
                fistTime = false;
            }

            if (tmp == null) {
                sb.append("");
            } else {
                sb.append(tmp);
            }
        }

        return sb.toString();
    }

    public static String replaceAll(String originalStr, String replaceStr, String newStr) {
        if (Strings.isNullOrEmpty(originalStr)) {
            return "";
        }

        if (Strings.isNullOrEmpty(replaceStr)) {
            return "";
        }

        if (newStr == null) {
            newStr = "";
        }

        int lastIndex = 0;
        StringBuilder sb = new StringBuilder();
        while (true) {
            int currentIndex = originalStr.indexOf(replaceStr, lastIndex);
            if (currentIndex < 0) {
                sb.append(originalStr.substring(lastIndex));
                break;
            } else {
                sb.append(originalStr.substring(lastIndex, currentIndex));
                sb.append(newStr);
                lastIndex = currentIndex + replaceStr.length();
            }
        }

        return sb.toString();
    }
}
