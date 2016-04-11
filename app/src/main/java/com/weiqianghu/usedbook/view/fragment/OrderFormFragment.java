package com.weiqianghu.usedbook.view.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.presenter.adapter.FragmentViewPagerAdapter;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.view.common.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFormFragment extends BaseFragment {
    public static final String TAG = OrderFormFragment.class.getSimpleName();

    private ImageView mTopBarLeftBtn;
    private TextView mTopBarText;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private List<Fragment> mViews = new ArrayList<>();
    private FragmentViewPagerAdapter mPagerAdapter;
    private FragmentManager mFragmentManager;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order_form;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTopBarLeftBtn = (ImageView) mRootView.findViewById(R.id.top_bar_left_button);
        mTopBarLeftBtn.setImageResource(R.mipmap.back);
        mTopBarLeftBtn.setOnClickListener(new Click());

        mTopBarText = (TextView) mRootView.findViewById(R.id.tv_top_bar_text);
        mTopBarText.setText(R.string.order_form);

        mTabLayout = (TabLayout) mRootView.findViewById(R.id.tablayout);
        mViewPager = (ViewPager) mRootView.findViewById(R.id.viewpager);

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


        mFragmentManager = getChildFragmentManager();
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
    public void onResume() {
        super.onResume();
        String tab = Constant.PAY;
        Bundle bundle = getArguments();
        if (bundle != null) {
            tab = bundle.getString(Constant.ORDER_TAB);
        }
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
            default:
                mViewPager.setCurrentItem(0);
                break;
        }
    }

    private class Click implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.top_bar_left_button:
                    getActivity().onBackPressed();
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
