package com.weiqianghu.usedbook.view.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.FailureMessage;
import com.weiqianghu.usedbook.presenter.RegisterPresenter;
import com.weiqianghu.usedbook.presenter.SendSmsCodePresenter;
import com.weiqianghu.usedbook.presenter.VerifySmsCodePresenter;
import com.weiqianghu.usedbook.util.CallBackHandler;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.util.InputUtil;
import com.weiqianghu.usedbook.util.TimeCount;
import com.weiqianghu.usedbook.view.common.BaseFragment;
import com.weiqianghu.usedbook.view.customview.ClearEditText;
import com.weiqianghu.usedbook.view.view.IRegisterView;
import com.weiqianghu.usedbook.view.view.ISendSmsCodeView;
import com.weiqianghu.usedbook.view.view.IVerifySmsCodeView;


public class RegisterFragment extends BaseFragment implements IRegisterView,ISendSmsCodeView,IVerifySmsCodeView {

    private TextView mTopBarText;
    private ImageView mTopBarLeftBtn;

    private RegisterPresenter mRegisterPresenter;
    private SendSmsCodePresenter mSendSmsCodePresenter;
    private VerifySmsCodePresenter mVerifySmsCodePresenter;

    private ClearEditText mMobileNoEt;
    private ClearEditText mMsgCodeEt;
    private ClearEditText mPasswordEt;
    private ClearEditText mEnsurePwEt;
    private Button mRegisterBtn;
    private Button mSendSmsCodeBtn;

    private ProgressBar mLoading;

    private String mobileNo;
    private String smsCode;
    private String ensurePwd;
    private String password;

    private TimeCount time;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Click click = new Click();

        mTopBarText = (TextView) mRootView.findViewById(R.id.tv_top_bar_text);
        mTopBarText.setText(R.string.go_to_register);

        mTopBarLeftBtn = (ImageView) mRootView.findViewById(R.id.top_bar_left_button);
        mTopBarLeftBtn.setImageResource(R.mipmap.back);
        mTopBarLeftBtn.setOnClickListener(click);

        mRegisterPresenter = new RegisterPresenter(this, registerHanler);

        mMobileNoEt = (ClearEditText) mRootView.findViewById(R.id.et_mobile_no);
        mMsgCodeEt = (ClearEditText) mRootView.findViewById(R.id.et_code);
        mPasswordEt = (ClearEditText) mRootView.findViewById(R.id.et_password);
        mEnsurePwEt = (ClearEditText) mRootView.findViewById(R.id.et_ensuer_password);
        mRegisterBtn = (Button) mRootView.findViewById(R.id.btn_submit);
        mRegisterBtn.setOnClickListener(click);

        mLoading = (ProgressBar) mRootView.findViewById(R.id.pb_loading);

        mSendSmsCodeBtn= (Button) mRootView.findViewById(R.id.btn_send_sms_code);
        mSendSmsCodeBtn.setOnClickListener(click);

        mSendSmsCodePresenter=new SendSmsCodePresenter(this,sendSmsCodeHandler);
        mVerifySmsCodePresenter=new VerifySmsCodePresenter(this,verifySmsCodeHandler);


        time = new TimeCount(60000, 1000,mSendSmsCodeBtn);
    }

    @Override
    public void register() {

    }

    CallBackHandler registerHanler=new CallBackHandler(){
        public  void handleSuccessMessage(Message msg){
            switch (msg.what) {
                case Constant.SUCCESS:
                    mLoading.setVisibility(View.INVISIBLE);
                    mRegisterBtn.setClickable(true);
                    Toast.makeText(getActivity(), "注册成功", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                    break;
            }
        }

        public void handleFailureMessage(String msg){
            mLoading.setVisibility(View.INVISIBLE);
            mRegisterBtn.setClickable(true);
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
    };

    CallBackHandler sendSmsCodeHandler=new CallBackHandler(){
        public  void handleSuccessMessage(Message msg){
            switch (msg.what) {
                case Constant.SUCCESS:
                    time.start();
                    break;
            }
        }

        public void handleFailureMessage(String msg){
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            mSendSmsCodeBtn.setClickable(true);
        }
    };


    CallBackHandler verifySmsCodeHandler=new CallBackHandler(){
        public  void handleSuccessMessage(Message msg){
            switch (msg.what) {
                case Constant.SUCCESS:
                    if(beforeRegister()) {
                        mRegisterPresenter.register(getActivity(), mobileNo, smsCode, password);
                    }
                    break;
            }
        }

        public void handleFailureMessage(String msg){
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            mLoading.setVisibility(View.INVISIBLE);
            mRegisterBtn.setClickable(true);
        }
    };



    private class Click implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.top_bar_left_button:
                    getActivity().onBackPressed();
                    break;
                case R.id.btn_submit:
                    if (beforeRegister()) {
                        mVerifySmsCodePresenter.verifySmsCode(getActivity(),mobileNo,smsCode);
                    }
                    break;
                case R.id.btn_send_sms_code:
                    if(beforeSendSmsCode()) {
                        mSendSmsCodeBtn.setClickable(false);
                        mSendSmsCodePresenter.sendSmsCode(getActivity(),mobileNo,"register");
                    }
                    break;
            }
        }
    }

    private boolean beforeSendSmsCode(){
        mobileNo = mMobileNoEt.getText().toString().trim();
        if (mobileNo == null || "".equals(mobileNo)) {
            Toast.makeText(getActivity(), "手机号不能为空", Toast.LENGTH_SHORT).show();
            mRegisterBtn.setClickable(true);
            return false;
        }
        if (!InputUtil.verifyMobileNo(mobileNo)) {
            Toast.makeText(getActivity(), "手机号不合法", Toast.LENGTH_SHORT).show();
            mRegisterBtn.setClickable(true);
            return false;
        }
        return true;
    }
    private boolean beforeRegister() {
        mobileNo = mMobileNoEt.getText().toString().trim();
        smsCode = mMsgCodeEt.getText().toString().trim();
        password = mPasswordEt.getText().toString().trim();
        ensurePwd = mEnsurePwEt.getText().toString().trim();

        mRegisterBtn.setClickable(false);

        if (mobileNo == null || "".equals(mobileNo)) {
            Toast.makeText(getActivity(), "手机号不能为空", Toast.LENGTH_SHORT).show();
            mRegisterBtn.setClickable(true);
            return false;
        }
        if (!InputUtil.verifyMobileNo(mobileNo)) {
            Toast.makeText(getActivity(), "手机号不合法", Toast.LENGTH_SHORT).show();
            mRegisterBtn.setClickable(true);
            return false;
        }
        if (smsCode == null || "".equals(smsCode)) {
            Toast.makeText(getActivity(), "验证码不能为空", Toast.LENGTH_SHORT).show();
            mRegisterBtn.setClickable(true);
            return false;
        }
        if (password == null || "".equals(password)) {
            Toast.makeText(getActivity(), "密码不能为空", Toast.LENGTH_SHORT).show();
            mRegisterBtn.setClickable(true);
            return false;
        }
        if (password.length() < 6) {
            Toast.makeText(getActivity(), "密码长度太短", Toast.LENGTH_SHORT).show();
            mRegisterBtn.setClickable(true);
            return false;
        }
        if (!password.equals(ensurePwd)) {
            Toast.makeText(getActivity(), "两次输入密码不一致", Toast.LENGTH_SHORT).show();
            mRegisterBtn.setClickable(true);
            return false;
        }
        mLoading.setVisibility(View.VISIBLE);
        return true;
    }

}
