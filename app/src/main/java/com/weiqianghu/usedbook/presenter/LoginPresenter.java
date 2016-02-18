package com.weiqianghu.usedbook.presenter;

import com.weiqianghu.usedbook.model.impl.LoginModel;
import com.weiqianghu.usedbook.model.inf.ILoginModel;
import com.weiqianghu.usedbook.view.view.ILoginView;

/**
 * Created by 胡伟强 on 2016/1/27.
 */
public class LoginPresenter {
    private ILoginView mLoginView;
    private ILoginModel mLoginModel;

    public LoginPresenter(ILoginView iLoginView){
        this.mLoginView=iLoginView;
        mLoginModel=new LoginModel();
    }

    public void Login(String username,String password){
       boolean isLoginSuccessed= mLoginModel.login(username,password);
        mLoginView.login(isLoginSuccessed);
    }
}
