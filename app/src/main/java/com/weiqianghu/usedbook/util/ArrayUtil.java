package com.weiqianghu.usedbook.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 胡伟强 on 2016/1/25.
 */
public class ArrayUtil<T> {

    public  List<T> ArrayToList(T [] array){
        List<T> list=new ArrayList<>();
        if(array==null){
            return list;
        }
        for(int i=0,length=array.length;i<length;i++){
            list.add(array[i]);
        }
        return list;
    }


}
