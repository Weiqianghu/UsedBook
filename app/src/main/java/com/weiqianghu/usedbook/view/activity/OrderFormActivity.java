package com.weiqianghu.usedbook.view.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weiqianghu.usedbook.R;
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

    private RelativeLayout mPayTab;
    private RelativeLayout mDeliverTab;
    private RelativeLayout mExpressTab;
    private RelativeLayout mEvaluateTab;
    private RelativeLayout mFinishTab;

    private ImageView mPayImg;
    private ImageView mDeliverImg;
    private ImageView mExpressImg;
    private ImageView mEvaluateImg;
    private ImageView mFinishImg;

    private List<Fragment> mViews = new ArrayList<>();
    private FragmentPagerAdapter mPagerAdapter;

    private Fragment mPayFragment;
    private Fragment mDeliverFragment;
    private Fragment mEvaluateFragment;
    private Fragment mExpressFragment;
    private Fragment mFinishFragment;


    private ViewPager mViewPager;

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

        mPayTab = (RelativeLayout) findViewById(R.id.rl_pay);
        mPayTab.setOnClickListener(new Click());
        mPayImg = (ImageView) findViewById(R.id.iv_pay);

        mDeliverTab = (RelativeLayout) findViewById(R.id.rl_deliver);
        mDeliverTab.setOnClickListener(new Click());
        mDeliverImg = (ImageView) findViewById(R.id.iv_deliver);

        mExpressTab = (RelativeLayout) findViewById(R.id.rl_express);
        mExpressTab.setOnClickListener(new Click());
        mExpressImg = (ImageView) findViewById(R.id.iv_express);

        mEvaluateTab = (RelativeLayout) findViewById(R.id.rl_evaluate);
        mEvaluateTab.setOnClickListener(new Click());
        mEvaluateImg = (ImageView) findViewById(R.id.iv_evaluate);

        mFinishTab = (RelativeLayout) findViewById(R.id.rl_finish);
        mFinishTab.setOnClickListener(new Click());
        mFinishImg = (ImageView) findViewById(R.id.iv_finish);

        mPayFragment = new OrderPayFragment();
        mDeliverFragment = new OrderDeliverFragment();
        mExpressFragment = new OrderExpressFragment();
        mEvaluateFragment = new OrderEvaluateFragment();
        mFinishFragment = new OrderFinishFragment();

        mViews.add(mPayFragment);
        mViews.add(mDeliverFragment);
        mViews.add(mExpressFragment);
        mViews.add(mEvaluateFragment);
        mViews.add(mFinishFragment);

        mViewPager= (ViewPager) findViewById(R.id.order_container);

        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mViews.get(position);
            }

            @Override
            public int getCount() {
                return mViews.size();
            }
        };

        mViewPager.setAdapter(mPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int mCurrentItem = mViewPager.getCurrentItem();
                setSelect(mCurrentItem);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
                setSelect(0);
                break;
            case Constant.DELIVER:
                setSelect(1);
                break;
            case Constant.EXPRESS:
                setSelect(2);
                break;
            case Constant.EVALUATE:
                setSelect(3);
                break;
            case Constant.FINISH:
                setSelect(4);
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
                case R.id.rl_pay:
                    setSelect(0);
                    break;
                case R.id.rl_deliver:
                    setSelect(1);
                    break;
                case R.id.rl_express:
                    setSelect(2);
                    break;
                case R.id.rl_evaluate:
                    setSelect(3);
                    break;
                case R.id.rl_finish:
                    setSelect(4);
                    break;
            }
        }
    }

    private void setSelect(int i) {
        resetImg();
        switch (i) {
            case 0:
                mViewPager.setCurrentItem(0);
                mPayImg.setImageResource(R.mipmap.pay_selected);
                mTopBarText.setText(R.string.pay);
                break;
            case 1:
                mViewPager.setCurrentItem(1);
                mDeliverImg.setImageResource(R.mipmap.delever_selected);
                mTopBarText.setText(R.string.deliver);
                break;
            case 2:
                mViewPager.setCurrentItem(2);
                mExpressImg.setImageResource(R.mipmap.express_selected);
                mTopBarText.setText(R.string.express);
                break;
            case 3:
                mViewPager.setCurrentItem(3);
                mEvaluateImg.setImageResource(R.mipmap.evaluate_selected);
                mTopBarText.setText(R.string.evaluate);
                break;
            case 4:
                mViewPager.setCurrentItem(4);
                mFinishImg.setImageResource(R.mipmap.finish_selected);
                mTopBarText.setText(R.string.finish);
                break;
        }

    }

    private void resetImg() {
        mPayImg.setImageResource(R.mipmap.pay);
        mDeliverImg.setImageResource(R.mipmap.deliver);
        mExpressImg.setImageResource(R.mipmap.express);
        mEvaluateImg.setImageResource(R.mipmap.evaluate);
        mFinishImg.setImageResource(R.mipmap.finish);
    }

}
