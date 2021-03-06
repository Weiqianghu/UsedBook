package com.weiqianghu.usedbook.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.view.common.BaseActivity;
import com.weiqianghu.usedbook.view.fragment.MainLayoutFragment;
import com.weiqianghu.usedbook.view.service.AprioriRecommendService;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.update.BmobUpdateAgent;

public class MainActivity extends BaseActivity {


    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BmobInstallation.getCurrentInstallation(this).save();
        // 启动推送服务
        BmobPush.startWork(this);
        initView();

    }

    private void initView() {
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
}
