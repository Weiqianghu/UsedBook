package com.weiqianghu.usedbook.util;


import android.net.Uri;
import android.os.Environment;

import com.weiqianghu.usedbook.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

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

    public static String getStrFromRaw(InputStream inputStream){
        String result=null;
        try {
            InputStreamReader inputReader = new InputStreamReader( inputStream);
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            result = "";
            while((line = bufReader.readLine()) != null)
                result += line;
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
