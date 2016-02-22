package com.weiqianghu.usedbook.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by weiqianghu on 2016/2/20.
 */
public class InputUtil {
    public static boolean verifyMobileNo(String mobileNo){
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobileNo.trim());
        return m.matches();
    }

    public static boolean verifyEmail(String email){
        boolean isExist = false;

        Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}");
        Matcher m = p.matcher(email);
        boolean b = m.matches();
        if(b) {
            isExist=true;
        } else {
        }
        return isExist;
    }
}
