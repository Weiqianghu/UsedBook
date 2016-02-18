package com.weiqianghu.usedbook.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by 胡伟强 on 2016/2/5.
 */
public class SelectImgUtil {

    public static void selectImg(Activity context,int mode,int max) {
        Intent intent = new Intent(context, MultiImageSelectorActivity.class);

        // 是否显示调用相机拍照
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);

        if(mode==MultiImageSelectorActivity.MODE_SINGLE) {
            // 最大图片选择数量
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
        }else {
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, max);
        }

        // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, mode);

        context.startActivityForResult(intent, Constant.REQUEST_IMAGE);
    }
}
