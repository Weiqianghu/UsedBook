package com.weiqianghu.usedbook.view.activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.view.fragment.LoginFragment;
import com.weiqianghu.usedbook.view.fragment.MainLayoutFragment;

public class LoginAndRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_register);

        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.login_and_register_container,new LoginFragment());
        ft.commit();
    }
}
