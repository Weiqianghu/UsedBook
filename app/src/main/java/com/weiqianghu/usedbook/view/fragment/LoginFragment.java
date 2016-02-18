package com.weiqianghu.usedbook.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.presenter.LoginPresenter;
import com.weiqianghu.usedbook.view.common.BaseFragment;
import com.weiqianghu.usedbook.view.view.ILoginView;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment implements ILoginView{

    private TextView mTvTopBarText;
    private ImageView mIvTopBarLeftBtn;
    private TextView mGotoRegisterTV;
    private TextView mForgetPasswordTV;
    private Button mLoginBtn;

    private FragmentManager mFragmentManager;

    private LoginPresenter mLoginPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView(savedInstanceState);
    }

    protected void initView(Bundle savedInstanceState) {
        mTvTopBarText= (TextView) mRootView.findViewById(R.id.tv_top_bar_text);
        mTvTopBarText.setText(R.string.login);

        mIvTopBarLeftBtn= (ImageView) mRootView.findViewById(R.id.top_bar_left_button);
        mIvTopBarLeftBtn.setImageResource(R.mipmap.back);
        mIvTopBarLeftBtn.setOnClickListener(new Click());

        mLoginBtn= (Button) mRootView.findViewById(R.id.btn_login);
        mLoginBtn.setOnClickListener(new Click());

        mGotoRegisterTV= (TextView) mRootView.findViewById(R.id.tv_goto_register);
        mGotoRegisterTV.setOnClickListener(new Click());

        mForgetPasswordTV= (TextView) mRootView.findViewById(R.id.tv_forget_password);
        mForgetPasswordTV.setOnClickListener(new Click());
    }

    @Override
    public void login(boolean isLoginSuccessed) {
        if(isLoginSuccessed){
            getActivity().onBackPressed();
        }
    }



    private class Click implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case  R.id.top_bar_left_button:
                    getActivity().onBackPressed();
                    break;
                case R.id.tv_goto_register:
                    gotoRegister();
                    break;
                case R.id.btn_login:
                    if(mLoginPresenter==null){
                        mLoginPresenter=new LoginPresenter(LoginFragment.this);
                    }
                    mLoginPresenter.Login("10123522","930409");
                    break;
                case R.id.tv_forget_password:
                    forgetPassword();
                    break;
            }
        }
    }

    private void forgetPassword() {
        if(mFragmentManager==null){
            mFragmentManager=getActivity().getSupportFragmentManager();
        }
        FragmentTransaction ft=mFragmentManager.beginTransaction();

        ft.setCustomAnimations(
                R.anim.push_left_in,
                R.anim.push_left_out,
                R.anim.push_right_in,
                R.anim.push_right_out);

        ft.replace(R.id.login_and_register_container,new ForgetPasswordkFragment());
        ft.addToBackStack(null);
        ft.commit();
    }

    private void gotoRegister(){
        if(mFragmentManager==null){
            mFragmentManager=getActivity().getSupportFragmentManager();
        }
        FragmentTransaction ft=mFragmentManager.beginTransaction();

        ft.setCustomAnimations(
                R.anim.push_left_in,
                R.anim.push_left_out,
                R.anim.push_right_in,
                R.anim.push_right_out);

        ft.replace(R.id.login_and_register_container,new RegisterFragment());
        ft.addToBackStack(null);
        ft.commit();
    }

}
