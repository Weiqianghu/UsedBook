package com.weiqianghu.usedbook.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.weiqianghu.usedbook.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.update.BmobUpdateAgent;

public class SeetingsActivity extends AppCompatActivity {

    private TextView mTvTopBarText;
    private ImageView mIvTopBarLeftBtn;

    private Button mLogoutBtn;

    private View mUpdateAgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seetings);

        initView(savedInstanceState);
    }

    protected void initView(Bundle savedInstanceState) {
        mTvTopBarText= (TextView) findViewById(R.id.tv_top_bar_text);
        mTvTopBarText.setText(R.string.settings);

        Click click=new Click();
        mIvTopBarLeftBtn= (ImageView)findViewById(R.id.top_bar_left_button);
        mIvTopBarLeftBtn.setImageResource(R.mipmap.back);
        mIvTopBarLeftBtn.setOnClickListener(click);

        mLogoutBtn= (Button) findViewById(R.id.btn_logout);
        mLogoutBtn.setOnClickListener(click);

        mUpdateAgent=findViewById(R.id.update_agent);
        mUpdateAgent.setOnClickListener(click);
    }

    private class Click implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case  R.id.top_bar_left_button:
                    finish();
                    break;
                case  R.id.btn_logout:
                    BmobUser.logOut(SeetingsActivity.this);
                    finish();
                    break;
                case  R.id.update_agent:
                    BmobUpdateAgent.forceUpdate(SeetingsActivity.this);
                    break;
            }
        }
    }
}
