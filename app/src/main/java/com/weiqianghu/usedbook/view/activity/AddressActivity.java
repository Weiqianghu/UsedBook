package com.weiqianghu.usedbook.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.view.fragment.AddressListFragment;

public class AddressActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;
    private Fragment mAddressListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }
        mAddressListFragment = mFragmentManager.findFragmentByTag(AddressListFragment.TAG);
        if (mAddressListFragment == null) {
            mAddressListFragment = new AddressListFragment();
        }
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.replace(R.id.address_container, mAddressListFragment, AddressListFragment.TAG);
        ft.commit();
    }
}
