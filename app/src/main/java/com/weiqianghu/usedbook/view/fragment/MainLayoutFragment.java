package com.weiqianghu.usedbook.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.viewbadger.BadgeView;
import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.chat_model.UserModel;
import com.weiqianghu.usedbook.model.chat_model.i.UpdateCacheListener;
import com.weiqianghu.usedbook.model.entity.UserBean;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.view.activity.MessageListActivity;
import com.weiqianghu.usedbook.view.common.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainLayoutFragment extends BaseFragment {

    public static final String TAG = MainLayoutFragment.class.getSimpleName();

    private ViewPager mViewPager;

    private RelativeLayout mTabMain;
    private RelativeLayout mTabShoppingCart;
    private RelativeLayout mTabMine;

    private ImageView mImgViewMain;
    private ImageView mImgViewShoppingCart;
    private ImageView mImgViewMine;

    private List<Fragment> mViews = new ArrayList<>();
    private FragmentPagerAdapter mPagerAdapter;

    private TextView mTopBarText;

    private boolean isFristIn = true;

    private Fragment mMainFragment;
    private Fragment mShoppingCartFragment;
    private Fragment mMineFragment;

    private ImageView mTopBarRightBtn;

    private int count = 0;
    private BadgeView badge;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_layout;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        if (isFristIn) {
            initView(savedInstanceState);
        }
        isFristIn = false;

        UserBean user = BmobUser.getCurrentUser(getActivity(), UserBean.class);
        if (user != null) {
            BmobIM.connect(user.getObjectId(), new ConnectListener() {
                @Override
                public void done(String uid, BmobException e) {
                    if (e == null) {
                    } else {
                    }
                }
            });
        }
    }

    protected void initView(Bundle savedInstanceState) {

        ClickListener click = new ClickListener();

        mViewPager = (ViewPager) mRootView.findViewById(R.id.mContener);

        mTopBarText = (TextView) mRootView.findViewById(R.id.tv_top_bar_text);

        mTabMain = (RelativeLayout) mRootView.findViewById(R.id.rl_main);
        mTabMine = (RelativeLayout) mRootView.findViewById(R.id.rl_mine);
        mTabShoppingCart = (RelativeLayout) mRootView.findViewById(R.id.rl_shopping_cart);

        mTabMain.setOnClickListener(click);
        mTabShoppingCart.setOnClickListener(click);
        mTabMine.setOnClickListener(click);

        mImgViewMain = (ImageView) mRootView.findViewById(R.id.iv_main);
        mImgViewShoppingCart = (ImageView) mRootView.findViewById(R.id.iv_shopping_cart);
        mImgViewMine = (ImageView) mRootView.findViewById(R.id.iv_mine);

        updateView();

        mTopBarRightBtn = (ImageView) mRootView.findViewById(R.id.top_bar_right_button);
        mTopBarRightBtn.setImageResource(R.mipmap.message);
        mTopBarRightBtn.setOnClickListener(click);

        mRootView.setFocusable(true);
        mRootView.setFocusableInTouchMode(true);
        mRootView.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    exit(v);
                    return true;
                }
                return false;
            }
        });

        setSelect(0);
    }

    public void updateView() {
        mMainFragment = new MainFragment();
        mShoppingCartFragment = new ShoppingCartFragment();
        mMineFragment = new MineFragment();

        mViews.add(mMainFragment);
        mViews.add(mShoppingCartFragment);
        mViews.add(mMineFragment);

        mPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            Bundle bundle = getArguments();
            int tap = bundle.getInt(Constant.TAB);
            setSelect(tap);
        } else {
            int currentItem = mViewPager.getCurrentItem();
            Bundle bundle = getArguments();
            bundle.putInt(Constant.TAB, currentItem);
        }
    }

    private void setSelect(int i) {
        resetImg();
        switch (i) {
            case 0:
                mViewPager.setCurrentItem(0);
                mImgViewMain.setImageResource(R.mipmap.main_selected);
                mTopBarText.setText(R.string.bottom_bar_left_text);
                break;
            case 1:
                mViewPager.setCurrentItem(1);
                mImgViewShoppingCart.setImageResource(R.mipmap.shopping_cart_selected);
                mTopBarText.setText(R.string.bottom_bar_center_text);
                break;
            case 2:
                mViewPager.setCurrentItem(2);
                mImgViewMine.setImageResource(R.mipmap.mine_selected);
                mTopBarText.setText(R.string.bottom_bar_right_text);
                break;
        }

    }

    private void resetImg() {
        mImgViewMain.setImageResource(R.mipmap.main);
        mImgViewShoppingCart.setImageResource(R.mipmap.shopping_cart);
        mImgViewMine.setImageResource(R.mipmap.mine);
    }

    private class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_main:
                    setSelect(0);
                    break;
                case R.id.rl_shopping_cart:
                    setSelect(1);
                    break;
                case R.id.rl_mine:
                    setSelect(2);
                    break;
                case R.id.top_bar_right_button:
                    gotoMessageList();
                    break;
            }
        }
    }

    public void exit(View v) {
        if (count < 1) {
            Toast.makeText(getActivity(), "再按一次退出", Toast.LENGTH_SHORT).show();
        }
        if (count < 2) {
            count++;
            new Thread() {
                public void run() {
                    try {
                        sleep(3000);
                        count = 0;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } else {
            getActivity().finish();
        }
    }

    private void gotoMessageList() {
        if (null == BmobUser.getCurrentUser(getActivity())) {
            Toast.makeText(getActivity(), R.string.suggest_to_login_text, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(getActivity(), MessageListActivity.class);
        startActivity(intent);
        if (badge != null && badge.isShown()
                ) {
            badge.hide();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onEventMainThread(OfflineMessageEvent event) {
        int offLineMessageCount = 0;
        Map<String, List<MessageEvent>> map = event.getEventMap();
        for (Map.Entry<String, List<MessageEvent>> entry : map.entrySet()) {
            List<MessageEvent> list = entry.getValue();
            offLineMessageCount += list.size();
        }

        badge = new BadgeView(getActivity(), mTopBarRightBtn);
        badge.setText(String.valueOf(offLineMessageCount));
        badge.show();
    }

}
