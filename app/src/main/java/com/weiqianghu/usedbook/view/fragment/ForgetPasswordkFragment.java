package com.weiqianghu.usedbook.view.fragment;


import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.presenter.ResetPasswordPresenter;
import com.weiqianghu.usedbook.presenter.SendSmsCodePresenter;
import com.weiqianghu.usedbook.util.CallBackHandler;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.util.InputUtil;
import com.weiqianghu.usedbook.util.TimeCount;
import com.weiqianghu.usedbook.view.common.BaseFragment;
import com.weiqianghu.usedbook.view.customview.ClearEditText;
import com.weiqianghu.usedbook.view.view.IResetPasswordView;
import com.weiqianghu.usedbook.view.view.ISendSmsCodeView;

public class ForgetPasswordkFragment extends BaseFragment implements ISendSmsCodeView,IResetPasswordView {
    private TextView mTopBarText;
    private ImageView mTopBarLeftBtn;
    private TimeCount time;

    private SendSmsCodePresenter mSendSmsCodePresenter;
    private ResetPasswordPresenter mResetPasswordPresenter;

    private ClearEditText mMobileNoEt;
    private ClearEditText mMsgCodeEt;
    private ClearEditText mPasswordEt;
    private ClearEditText mEnsurePwEt;
    private Button mSubmitBtn;
    private Button mSendSmsCodeBtn;
    private ProgressBar mLoading;

    private String mobileNo;
    private String smsCode;
    private String ensurePwd;
    private String password;

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
        mTopBarText = (TextView) mRootView.findViewById(R.id.tv_top_bar_text);
        mTopBarText.setText(R.string.forget_password);

        Click click = new Click();

        mTopBarLeftBtn = (ImageView) mRootView.findViewById(R.id.top_bar_left_button);
        mTopBarLeftBtn.setImageResource(R.mipmap.back);
        mTopBarLeftBtn.setOnClickListener(click);

        mSendSmsCodePresenter = new SendSmsCodePresenter(this, sendSmsCodeHandler);
        mResetPasswordPresenter=new ResetPasswordPresenter(this,ResetPasswordHandler);

        mMobileNoEt = (ClearEditText) mRootView.findViewById(R.id.et_mobile_no);
        mMsgCodeEt = (ClearEditText) mRootView.findViewById(R.id.et_code);
        mPasswordEt = (ClearEditText) mRootView.findViewById(R.id.et_new_password);
        mEnsurePwEt = (ClearEditText) mRootView.findViewById(R.id.et_ensuer_password);
        mSubmitBtn = (Button) mRootView.findViewById(R.id.btn_submit);
        mSubmitBtn.setOnClickListener(click);
        mSendSmsCodeBtn = (Button) mRootView.findViewById(R.id.btn_send_sms_code);
        mSendSmsCodeBtn.setOnClickListener(click);

        mLoading = (ProgressBar) mRootView.findViewById(R.id.pb_loading);

        time = new TimeCount(60000, 1000, mSendSmsCodeBtn);
    }

    private class Click implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.top_bar_left_button:
                    getActivity().onBackPressed();
                    break;
                case R.id.btn_submit:
                    if (beforeSubmit()) {
                        mResetPasswordPresenter.resetPassword(getActivity(),smsCode,password);
                    }
                    break;
                case R.id.btn_send_sms_code:
                    if (beforeSendSmsCode()) {
                        mSendSmsCodeBtn.setClickable(false);
                        mSendSmsCodePresenter.sendSmsCode(getActivity(), mobileNo, "resetPwd");
                    }
                    break;
            }
        }
    }

    CallBackHandler sendSmsCodeHandler = new CallBackHandler() {
        public void handleSuccessMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    time.start();
                    break;
            }
        }

        public void handleFailureMessage(String msg) {
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            mSendSmsCodeBtn.setClickable(true);
        }
    };

    CallBackHandler ResetPasswordHandler = new CallBackHandler() {
        public void handleSuccessMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    mLoading.setVisibility(View.INVISIBLE);
                    mSubmitBtn.setClickable(true);
                    Toast.makeText(getActivity(),"密码重置成功",Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                    break;
            }
        }

        public void handleFailureMessage(String msg) {
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            mLoading.setVisibility(View.INVISIBLE);
            mSubmitBtn.setClickable(true);
        }
    };


    private boolean beforeSendSmsCode() {
        mobileNo = mMobileNoEt.getText().toString().trim();
        if (mobileNo == null || "".equals(mobileNo)) {
            Toast.makeText(getActivity(), "手机号不能为空", Toast.LENGTH_SHORT).show();
            mSubmitBtn.setClickable(true);
            return false;
        }
        if (!InputUtil.verifyMobileNo(mobileNo)) {
            Toast.makeText(getActivity(), "手机号不合法", Toast.LENGTH_SHORT).show();
            mSubmitBtn.setClickable(true);
            return false;
        }
        return true;
    }

    private boolean beforeSubmit() {
        mobileNo = mMobileNoEt.getText().toString().trim();
        smsCode = mMsgCodeEt.getText().toString().trim();
        password = mPasswordEt.getText().toString().trim();
        ensurePwd = mEnsurePwEt.getText().toString().trim();

        mSubmitBtn.setClickable(false);

        if (mobileNo == null || "".equals(mobileNo)) {
            Toast.makeText(getActivity(), "手机号不能为空", Toast.LENGTH_SHORT).show();
            mSubmitBtn.setClickable(true);
            return false;
        }
        if (!InputUtil.verifyMobileNo(mobileNo)) {
            Toast.makeText(getActivity(), "手机号不合法", Toast.LENGTH_SHORT).show();
            mSubmitBtn.setClickable(true);
            return false;
        }
        if (smsCode == null || "".equals(smsCode)) {
            Toast.makeText(getActivity(), "验证码不能为空", Toast.LENGTH_SHORT).show();
            mSubmitBtn.setClickable(true);
            return false;
        }
        if (password == null || "".equals(password)) {
            Toast.makeText(getActivity(), "密码不能为空", Toast.LENGTH_SHORT).show();
            mSubmitBtn.setClickable(true);
            return false;
        }
        if (password.length() < 6) {
            Toast.makeText(getActivity(), "密码长度太短", Toast.LENGTH_SHORT).show();
            mSubmitBtn.setClickable(true);
            return false;
        }
        if (!password.equals(ensurePwd)) {
            Toast.makeText(getActivity(), "两次输入密码不一致", Toast.LENGTH_SHORT).show();
            mSubmitBtn.setClickable(true);
            return false;
        }
        mLoading.setVisibility(View.VISIBLE);
        return true;
    }
}
