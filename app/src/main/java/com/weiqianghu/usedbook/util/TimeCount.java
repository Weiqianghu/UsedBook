package com.weiqianghu.usedbook.util;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.Button;

import com.weiqianghu.usedbook.R;

/**
 * Created by weiqianghu on 2016/2/22.
 */
public class TimeCount extends CountDownTimer {

    private Button button;
    public TimeCount(long millisInFuture, long countDownInterval, Button button) {
        super(millisInFuture, countDownInterval);
        this.button=button;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        button.setBackgroundColor(Color.parseColor("#7F7F7F"));
        button.setClickable(false);
        button.setText("("+millisUntilFinished / 1000 +") 秒后可重新发送");
    }

    @Override
    public void onFinish() {
        button.setText("重新获取验证码");
        button.setClickable(true);
        button.setBackgroundColor(Color.parseColor("#FF7F27"));
    }
}
