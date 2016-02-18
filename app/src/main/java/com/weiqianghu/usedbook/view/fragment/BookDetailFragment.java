package com.weiqianghu.usedbook.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.BookBean;
import com.weiqianghu.usedbook.presenter.BooksDetailPresenter;
import com.weiqianghu.usedbook.presenter.adapter.MViewPagerAdapter;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.util.FragmentUtil;
import com.weiqianghu.usedbook.view.common.BaseFragment;
import com.weiqianghu.usedbook.view.view.IBooksDetailView;

import java.util.List;


public class BookDetailFragment extends BaseFragment implements IBooksDetailView {

    public static final String TAG = BookDetailFragment.class.getSimpleName();
    private FragmentManager mFragmentManager;

    private ViewPager mBookImgVp;
    private BooksDetailPresenter mBooksDetailPresenter;
    private BookBean mBookbean;

    private ImageView mTopBarLeftBtn;
    private TextView mTopBarText;
    private TextView mPostionTv;

    private View mCommentView;
    private TextView mPriceTv;

    private ImageView mShoppingCartIV;


    public static BookDetailFragment getInstance() {
        BookDetailFragment fragment = new BookDetailFragment();

        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_book_detail;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView(savedInstanceState);
        updateView();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mBookImgVp = (ViewPager) mRootView.findViewById(R.id.vp_book_img);
        if (mBooksDetailPresenter == null) {
            mBooksDetailPresenter = new BooksDetailPresenter(getActivity(), this, mBookbean);
        }

        mTopBarLeftBtn = (ImageView) mRootView.findViewById(R.id.top_bar_left_button);
        mTopBarLeftBtn.setImageResource(R.mipmap.back);
        mTopBarLeftBtn.setOnClickListener(new Click());

        mTopBarText = (TextView) mRootView.findViewById(R.id.tv_top_bar_text);
        mTopBarText.setText(R.string.book_detail);

        mPostionTv = (TextView) mRootView.findViewById(R.id.tv_position);

        mCommentView = mRootView.findViewById(R.id.comment);
        mCommentView.setOnClickListener(new Click());

        mPriceTv = (TextView) mRootView.findViewById(R.id.tv_price);
        Bundle bundle = getArguments();
        mBookbean = (BookBean) bundle.getSerializable("books");

        mShoppingCartIV = (ImageView) mRootView.findViewById(R.id.iv_shopping_cart);
        mShoppingCartIV.setOnClickListener(new Click());
    }

    private void updateView() {
        mBooksDetailPresenter.loadBookImgs();

        mPriceTv.setText("￥" + mBookbean.getPrice());
    }

    @Override
    public void setBookImgs(final List<View> imgs) {

        MViewPagerAdapter adapter = new MViewPagerAdapter(imgs, mBookImgVp);
        mBookImgVp.setAdapter(adapter);
        mBookImgVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBookImgVp.setCurrentItem(position);
                mPostionTv.setText(position + 1 + "/" + imgs.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class Click implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.top_bar_left_button:
                    getActivity().onBackPressed();
                    break;
                case R.id.iv_shopping_cart:
                    if (mFragmentManager == null) {
                        mFragmentManager = getActivity().getSupportFragmentManager();
                    }
                    Fragment fragment = mFragmentManager.findFragmentByTag(MainLayoutFragment.TAG);
                    if (fragment == null) {
                        fragment = new MainLayoutFragment();

                        Bundle bundle = new Bundle();
                        bundle.putInt(Constant.TAB, 1);
                        fragment.setArguments(bundle);
                    } else {
                        Bundle bundle = fragment.getArguments();
                        bundle.putInt(Constant.TAB, 1);
                    }
                    FragmentUtil.switchContent(BookDetailFragment.this, fragment, R.id.main_container, mFragmentManager);
                    break;
            }
        }
    }
}
