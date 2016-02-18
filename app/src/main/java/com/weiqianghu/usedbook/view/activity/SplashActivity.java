package com.weiqianghu.usedbook.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.view.common.BaseActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler x = new Handler();
        x.postDelayed(new splashHandler(), 3000);
    }

    class splashHandler implements Runnable {

        public void run() {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
