package com.weiqianghu.usedbook.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.presenter.IsLoginPresenter;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.util.FragmentUtil;
import com.weiqianghu.usedbook.util.SelectImgUtil;
import com.weiqianghu.usedbook.view.activity.OrderActivity;
import com.weiqianghu.usedbook.view.activity.SeetingsActivity;
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

    private View mSeetings;

    private Intent intent;

    private CircleImageView mUserImgImgView;

    private FragmentManager fragmentManager;

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

        Click click=new Click();

        mUserInfo = mRootView.findViewById(R.id.user_info);
        mUserInfo.setOnClickListener(click);

        mMessage = mRootView.findViewById(R.id.message);
        mMessage.setOnClickListener(click);

        mPrefer = mRootView.findViewById(R.id.prefer);
        mPrefer.setOnClickListener(click);

        mAddress = mRootView.findViewById(R.id.address);
        mAddress.setOnClickListener(click);

        mSetting = mRootView.findViewById(R.id.setting);
        mSetting.setOnClickListener(click);

        mPay = mRootView.findViewById(R.id.pay);
        mPay.setOnClickListener(click);

        mDeliver = mRootView.findViewById(R.id.deliver);
        mDeliver.setOnClickListener(click);

        mExpress = mRootView.findViewById(R.id.express);
        mExpress.setOnClickListener(click);

        mEvaluate = mRootView.findViewById(R.id.evaluate);
        mEvaluate.setOnClickListener(click);

        mFinish = mRootView.findViewById(R.id.finish);
        mFinish.setOnClickListener(click);

        mIsLoginPresenter = new IsLoginPresenter();

        mUserImgImgView= (CircleImageView) mRootView.findViewById(R.id.iv_user_img);
        mUserImgImgView.setOnClickListener(click);

        mSeetings=mRootView.findViewById(R.id.setting);
        mSeetings.setOnClickListener(click);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("onResume","Mine onResume");
        if(mIsLoginPresenter.isLogin(getActivity())){
            mSuggestToLoginLayout.setVisibility(View.VISIBLE);
        }else {
            mSuggestToLoginLayout.setVisibility(View.INVISIBLE);
        }
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
                case R.id.setting:
                    gotoSeetings();
                    break;
            }
        }
    }

    void gotoSeetings() {
       /* if (fragmentManager == null) {
            fragmentManager = getActivity().getSupportFragmentManager();
        }

        Fragment fragment = fragmentManager.findFragmentByTag(SeetingsFragment.TAG);
        if (fragment == null) {
            fragment = new SeetingsFragment();
        }

        Fragment from=fragmentManager.findFragmentByTag(MainLayoutFragment.TAG);
        FragmentUtil.switchContentAddToBackStack(from,fragment,R.id.main_container,fragmentManager);*/

        Intent intent=new Intent(getActivity(), SeetingsActivity.class);
        startActivity(intent);
    }
}
