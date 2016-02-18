package com.weiqianghu.usedbook.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.presenter.IsLoginPresenter;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.util.SelectImgUtil;
import com.weiqianghu.usedbook.view.activity.OrderActivity;
import com.weiqianghu.usedbook.view.common.BaseFragment;
import com.weiqianghu.usedbook.view.customview.CircleImageView;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends BaseFragment {

    private View mSuggestToLoginLayout;
    private IsLoginPresenter mIsLoginPresenter;

    private View mUserInfo;
    private View mMessage;
    private View mPrefer;
    private View mAddress;
    private View mSetting;

    private View mPay;
    private View mDeliver;
    private View mExpress;
    private View mEvaluate;
    private View mFinish;

    private Intent intent;

    private CircleImageView mUserImgImgView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mSuggestToLoginLayout = mRootView.findViewById(R.id.ly_suggest_to_login);

        mUserInfo = mRootView.findViewById(R.id.user_info);
        mUserInfo.setOnClickListener(new Click());

        mMessage = mRootView.findViewById(R.id.message);
        mMessage.setOnClickListener(new Click());

        mPrefer = mRootView.findViewById(R.id.prefer);
        mPrefer.setOnClickListener(new Click());

        mAddress = mRootView.findViewById(R.id.address);
        mAddress.setOnClickListener(new Click());

        mSetting = mRootView.findViewById(R.id.setting);
        mSetting.setOnClickListener(new Click());

        mPay = mRootView.findViewById(R.id.pay);
        mPay.setOnClickListener(new Click());

        mDeliver = mRootView.findViewById(R.id.deliver);
        mDeliver.setOnClickListener(new Click());

        mExpress = mRootView.findViewById(R.id.express);
        mExpress.setOnClickListener(new Click());

        mEvaluate = mRootView.findViewById(R.id.evaluate);
        mEvaluate.setOnClickListener(new Click());

        mFinish = mRootView.findViewById(R.id.finish);
        mFinish.setOnClickListener(new Click());

        mIsLoginPresenter = new IsLoginPresenter();

        mUserImgImgView= (CircleImageView) mRootView.findViewById(R.id.iv_user_img);
        mUserImgImgView.setOnClickListener(new Click());
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private class Click implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if(intent==null){
                intent=new Intent(getActivity(), OrderActivity.class);
            }
            switch (v.getId()) {
                case R.id.pay:
                    intent.putExtra(Constant.ORDER_TAB, Constant.PAY);
                    startActivity(intent);
                    break;
                case R.id.deliver:
                    intent.putExtra(Constant.ORDER_TAB, Constant.DELIVER);
                    startActivity(intent);
                    break;
                case R.id.express:
                    intent.putExtra(Constant.ORDER_TAB, Constant.EXPRESS);
                    startActivity(intent);
                    break;
                case R.id.evaluate:
                    intent.putExtra(Constant.ORDER_TAB, Constant.EVALUATE);
                    startActivity(intent);
                    break;
                case R.id.finish:
                    intent.putExtra(Constant.ORDER_TAB, Constant.FINISH);
                    startActivity(intent);
                    break;
                case R.id.iv_user_img:
                    SelectImgUtil.selectImg(getActivity(), MultiImageSelectorActivity.MODE_SINGLE,1);
                    break;
            }
        }
    }

}
