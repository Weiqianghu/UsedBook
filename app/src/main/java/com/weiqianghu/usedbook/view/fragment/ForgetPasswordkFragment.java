package com.weiqianghu.usedbook.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.view.common.BaseFragment;

public class ForgetPasswordkFragment extends BaseFragment {
    private TextView mTopBarText;
    private ImageView mTopBarLeftBtn;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_forget_passwordk;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTopBarText= (TextView) mRootView.findViewById(R.id.tv_top_bar_text);
        mTopBarText.setText(R.string.forget_password);

        mTopBarLeftBtn= (ImageView) mRootView.findViewById(R.id.top_bar_left_button);
        mTopBarLeftBtn.setImageResource(R.mipmap.back);
        mTopBarLeftBtn.setOnClickListener(new Click());
    }

    private class Click implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.top_bar_left_button:
                    getActivity().onBackPressed();
                    break;
            }
        }
    }

}
