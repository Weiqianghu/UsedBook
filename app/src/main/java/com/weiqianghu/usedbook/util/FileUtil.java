package com.weiqianghu.usedbook.util;


import android.net.Uri;
import android.os.Environment;

import java.io.File;

/**
 * Created by weiqianghu on 2016/2/23.
 */
public class FileUtil {
    public static Uri getUriByPath(String path){
        StringBuffer sb = new StringBuffer("file://");
        sb.append(path);
        return Uri.parse(sb.toString());
    }

    public static String getCachePath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }else{
            sdDir=Environment.getDownloadCacheDirectory();
        }
        return sdDir.toString();
    }
}
