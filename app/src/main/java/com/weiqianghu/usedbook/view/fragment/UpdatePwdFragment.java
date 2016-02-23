package com.weiqianghu.usedbook.view.fragment;


import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.presenter.UpdatePwdPresenter;
import com.weiqianghu.usedbook.util.CallBackHandler;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.view.common.BaseFragment;
import com.weiqianghu.usedbook.view.customview.ClearEditText;

import java.lang.reflect.Field;

public class UpdatePwdFragment extends BaseFragment {
    public static final String TAG = UpdatePwdPresenter.class.getSimpleName();

    private ClearEditText mOldPwddEt;
    private ClearEditText mPasswordEt;
    private ClearEditText mEnsurePwEt;
    private Button mSubmitBtn;

    private String oldPwd;
    private String ensurePwd;
    private String newPwd;

    private ProgressBar mLoading;

    private UpdatePwdPresenter mUpdatePwdPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_update_pwd;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        Click click = new Click();

        mOldPwddEt= (ClearEditText) mRootView.findViewById(R.id.et_old_pwd);
        mPasswordEt = (ClearEditText) mRootView.findViewById(R.id.et_new_password);
        mEnsurePwEt = (ClearEditText) mRootView.findViewById(R.id.et_ensuer_password);
        mSubmitBtn = (Button) mRootView.findViewById(R.id.btn_submit);
        mSubmitBtn.setOnClickListener(click);
        mLoading = (ProgressBar) mRootView.findViewById(R.id.pb_loading);

        mUpdatePwdPresenter=new UpdatePwdPresenter(updatePwdHanler);
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
                        mUpdatePwdPresenter.updatePwd(getActivity(),oldPwd, newPwd);
                    }
                    break;
            }
        }
    }

    private boolean beforeSubmit() {
        oldPwd=mOldPwddEt.getText().toString().trim();
        newPwd = mPasswordEt.getText().toString().trim();
        ensurePwd = mEnsurePwEt.getText().toString().trim();

        mSubmitBtn.setClickable(false);

        if (oldPwd == null || oldPwd.length()<6) {
            Toast.makeText(getActivity(), "密码长度太短", Toast.LENGTH_SHORT).show();
            mSubmitBtn.setClickable(true);
            return false;
        }
        if (newPwd == null || "".equals(newPwd)) {
            Toast.makeText(getActivity(), "密码不能为空", Toast.LENGTH_SHORT).show();
            mSubmitBtn.setClickable(true);
            return false;
        }
        if (newPwd.length() < 6) {
            Toast.makeText(getActivity(), "密码长度太短", Toast.LENGTH_SHORT).show();
            mSubmitBtn.setClickable(true);
            return false;
        }
        if (!newPwd.equals(ensurePwd)) {
            Toast.makeText(getActivity(), "两次输入密码不一致", Toast.LENGTH_SHORT).show();
            mSubmitBtn.setClickable(true);
            return false;
        }
        mLoading.setVisibility(View.VISIBLE);
        return true;
    }

    CallBackHandler updatePwdHanler = new CallBackHandler() {
        public void handleSuccessMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    mLoading.setVisibility(View.INVISIBLE);
                    Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_SHORT).show();
                    mSubmitBtn.setClickable(true);
                    getActivity().onBackPressed();
                    break;
            }
        }

        public void handleFailureMessage(String msg) {
            mLoading.setVisibility(View.INVISIBLE);
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            mSubmitBtn.setClickable(true);
        }
    };
}
