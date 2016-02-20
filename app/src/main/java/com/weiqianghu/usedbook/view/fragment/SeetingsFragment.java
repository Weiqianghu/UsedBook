package com.weiqianghu.usedbook.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.presenter.LoginPresenter;
import com.weiqianghu.usedbook.view.common.BaseFragment;

import cn.bmob.v3.BmobUser;

public class SeetingsFragment extends BaseFragment {

    public static final String TAG = SeetingsFragment.class.getSimpleName();

    private TextView mTvTopBarText;
    private ImageView mIvTopBarLeftBtn;

    private Button mLogoutBtn;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_seetings;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvTopBarText= (TextView) mRootView.findViewById(R.id.tv_top_bar_text);
        mTvTopBarText.setText(R.string.settings);

        Click click=new Click();
        mIvTopBarLeftBtn= (ImageView) mRootView.findViewById(R.id.top_bar_left_button);
        mIvTopBarLeftBtn.setImageResource(R.mipmap.back);
        mIvTopBarLeftBtn.setOnClickListener(click);

        mLogoutBtn= (Button) mRootView.findViewById(R.id.btn_logout);
        mLogoutBtn.setOnClickListener(click);
    }

    private class Click implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case  R.id.top_bar_left_button:
                    getActivity().onBackPressed();
                    break;
                case  R.id.btn_logout:
                    BmobUser.logOut(getActivity());
                    getActivity().onBackPressed();
                    break;
            }
        }
    }
}
