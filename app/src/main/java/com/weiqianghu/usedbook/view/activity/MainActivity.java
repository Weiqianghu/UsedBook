package com.weiqianghu.usedbook.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.util.ImgUtil;
import com.weiqianghu.usedbook.view.common.BaseActivity;
import com.weiqianghu.usedbook.view.customview.CircleImageView;
import com.weiqianghu.usedbook.view.fragment.MainLayoutFragment;

import java.util.List;

import cn.bmob.v3.update.BmobUpdateAgent;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class MainActivity extends BaseActivity {


    private FragmentManager mFragmentManager;

    private CircleImageView mUserImgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }
        Fragment fragment = mFragmentManager.findFragmentByTag(MainLayoutFragment.TAG);
        if (fragment == null) {
            fragment = new MainLayoutFragment();

            Bundle bundle = new Bundle();
            bundle.putInt(Constant.TAB, 0);
            fragment.setArguments(bundle);
        } else {
            Bundle bundle = fragment.getArguments();
            bundle.putInt(Constant.TAB, 0);
        }


        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.replace(R.id.main_container, fragment, MainLayoutFragment.TAG);
        ft.commit();

        BmobUpdateAgent.update(this);
    }

    public void gotoLogin(View view) {
        Intent intent = new Intent(this, LoginAndRegisterActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                // 获取返回的图片列表
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                if (path.size() > 0) {
                    if (mUserImgView == null) {
                        mUserImgView = (CircleImageView) findViewById(R.id.iv_user_img);
                    }
                    Bitmap bitmap = ImgUtil.getSmallBitmap(path.get(0), mUserImgView.getWidth(), mUserImgView.getHeight());
                    mUserImgView.setImageBitmap(bitmap);
                }
            }
        }
    }
}
