package com.weiqianghu.usedbook.view.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.presenter.adapter.FragmentViewPagerAdapter;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.view.fragment.OrderDeliverFragment;
import com.weiqianghu.usedbook.view.fragment.OrderEvaluateFragment;
import com.weiqianghu.usedbook.view.fragment.OrderExpressFragment;
import com.weiqianghu.usedbook.view.fragment.OrderFinishFragment;
import com.weiqianghu.usedbook.view.fragment.OrderPayFragment;

import java.util.ArrayList;
import java.util.List;

public class OrderFormActivity extends AppCompatActivity {


    private ImageView mTopBarLeftBtn;
    private TextView mTopBarText;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private List<Fragment> mViews = new ArrayList<>();
    private FragmentViewPagerAdapter mPagerAdapter;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_form);

        initView();
    }

    private void initView() {
        mTopBarLeftBtn = (ImageView) findViewById(R.id.top_bar_left_button);
        mTopBarLeftBtn.setImageResource(R.mipmap.back);
        mTopBarLeftBtn.setOnClickListener(new Click());

        mTopBarText = (TextView) findViewById(R.id.tv_top_bar_text);
        mTopBarText.setText(R.string.order_form);

        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        Fragment mOrderPayFragment = new OrderPayFragment();
        Fragment mOrderDeliverFragment = new OrderDeliverFragment();
        Fragment mOrderExpressFragment = new OrderExpressFragment();
        Fragment mOrderEvaluateFragment = new OrderEvaluateFragment();
        Fragment mOrderFinishFragment = new OrderFinishFragment();

        mViews.add(mOrderPayFragment);
        mViews.add(mOrderDeliverFragment);
        mViews.add(mOrderExpressFragment);
        mViews.add(mOrderEvaluateFragment);
        mViews.add(mOrderFinishFragment);

        String[] mTitles = {getString(R.string.pay),
                getString(R.string.deliver), getString(R.string.express)
                , getString(R.string.evaluate), getString(R.string.finish)};


        mFragmentManager = getSupportFragmentManager();
        mPagerAdapter = new FragmentViewPagerAdapter(mFragmentManager, mViews, mTitles);

        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                setSelect(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.d("TabSelectedListener", "onTabUnselected");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d("TabSelectedListener", "onTabReselected");
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String tab = intent.getStringExtra(Constant.ORDER_TAB);
        switch (tab) {
            case Constant.PAY:
                mViewPager.setCurrentItem(0);
                break;
            case Constant.DELIVER:
                mViewPager.setCurrentItem(1);
                break;
            case Constant.EXPRESS:
                mViewPager.setCurrentItem(2);
                break;
            case Constant.EVALUATE:
                mViewPager.setCurrentItem(3);
                break;
            case Constant.FINISH:
                mViewPager.setCurrentItem(4);
                break;
        }
    }

    private class Click implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.top_bar_left_button:
                    finish();
                    break;
            }
        }
    }

    private void setSelect(int i) {
        switch (i) {
            case 0:
                mTopBarText.setText(R.string.pay);
                break;
            case 1:
                mTopBarText.setText(R.string.deliver);
                break;
            case 2:
                mTopBarText.setText(R.string.express);
                break;
            case 3:
                mTopBarText.setText(R.string.evaluate);
                break;
            case 4:
                mTopBarText.setText(R.string.finish);
                break;
        }

    }


}
