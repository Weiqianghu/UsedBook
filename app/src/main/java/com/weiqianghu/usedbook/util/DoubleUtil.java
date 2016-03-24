package com.weiqianghu.usedbook.util;

/**
 * Created by weiqianghu on 2016/3/24.
 */
public class DoubleUtil {
    public static double subDouble(Double number) {
        return Math.round(number * 10) / 10.0;
    }
}
