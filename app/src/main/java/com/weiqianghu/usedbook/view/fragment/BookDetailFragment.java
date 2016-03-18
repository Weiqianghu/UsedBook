package com.weiqianghu.usedbook.view.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.BookBean;
import com.weiqianghu.usedbook.model.entity.BookImgsBean;
import com.weiqianghu.usedbook.model.entity.BookModel;
import com.weiqianghu.usedbook.presenter.BooksDetailPresenter;
import com.weiqianghu.usedbook.presenter.adapter.MViewPagerAdapter;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.util.FragmentUtil;
import com.weiqianghu.usedbook.view.common.BaseFragment;
import com.weiqianghu.usedbook.view.view.IBooksDetailView;

import java.util.ArrayList;
import java.util.List;


public class BookDetailFragment extends BaseFragment implements IBooksDetailView {

    public static final String TAG = BookDetailFragment.class.getSimpleName();
    private FragmentManager mFragmentManager;

    private ViewPager mBookImgVp;
    private BooksDetailPresenter mBooksDetailPresenter;
    private BookModel mBookModel;

    private ImageView mTopBarLeftBtn;
    private TextView mTopBarText;
    private TextView mPostionTv;

    private View mCommentView;
    private TextView mPriceTv;
    private TextView mBookNameTv;
    private TextView mBookPercentDescribeTv;
    private TextView mBookStockTv;
    private TextView mBookAuthorTv;
    private TextView mBookPressTv;
    private TextView mBookIsbnTv;
    private TextView mBookSalesVolumeTv;
    private TextView mBookCategoryTv;

    private ImageView mShoppingCartIV;


    public static BookDetailFragment getInstance() {
        BookDetailFragment fragment = new BookDetailFragment();

        return fragment;
    }


    @Override
    protected int getLayoutId() {
        Fresco.initialize(getActivity());
        return R.layout.fragment_book_detail;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initData();
        initView(savedInstanceState);
        updateView(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mBookImgVp = (ViewPager) mRootView.findViewById(R.id.vp_book_img);
        if (mBooksDetailPresenter == null) {
            mBooksDetailPresenter = new BooksDetailPresenter(getActivity(), this, mBookModel.getBookImgs());
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
        mBookNameTv = (TextView) mRootView.findViewById(R.id.tv_book_name);
        mBookPercentDescribeTv = (TextView) mRootView.findViewById(R.id.tv_percent);
        mBookStockTv = (TextView) mRootView.findViewById(R.id.tv_stock);
        mBookAuthorTv = (TextView) mRootView.findViewById(R.id.tv_author);
        mBookPressTv = (TextView) mRootView.findViewById(R.id.tv_press);
        mBookIsbnTv = (TextView) mRootView.findViewById(R.id.tv_isbn);
        mBookSalesVolumeTv = (TextView) mRootView.findViewById(R.id.tv_sales_volume);
        mBookCategoryTv = (TextView) mRootView.findViewById(R.id.tv_sales_category);


        mShoppingCartIV = (ImageView) mRootView.findViewById(R.id.iv_shopping_cart);
        mShoppingCartIV.setOnClickListener(new Click());
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mBookModel = bundle.getParcelable(Constant.BOOK);
        } else {
            mBookModel = new BookModel();
        }
    }

    private void updateView(Bundle savedInstanceState) {
        mBooksDetailPresenter.loadBookImgs();

        BookBean book = mBookModel.getBook();
        //setBookImgs(loadBookImgs(savedInstanceState, mBookModel.getBookImgs()));

        mBookNameTv.setText(book.getBookName());
        mPriceTv.setText("￥" + book.getPrice());
        mBookPercentDescribeTv.setText(book.getPercentDescribe());
        mBookStockTv.setText(String.valueOf(book.getStock()));
        mBookAuthorTv.setText(book.getAuthor());
        mBookPressTv.setText(book.getPress());
        mBookIsbnTv.setText(book.getIsbn());
        mBookSalesVolumeTv.setText(String.valueOf(book.getSalesVolume()));
        mBookCategoryTv.setText(book.getCategory());
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


    /*public List<View> loadBookImgs(Bundle savedInstanceState, List<BookImgsBean> imgs) {
        LayoutInflater inflater = getLayoutInflater(savedInstanceState);
        List<View> views = new ArrayList<>(3);

        if (imgs != null) {
            for (int i = 0, length = imgs.size(); i < length; i++) {
                SimpleDraweeView img = (SimpleDraweeView) inflater.inflate(R.layout.item_book_detail_img, null);
                Uri uri = Uri.parse(imgs.get(i).getImg());
                img.setImageURI(uri);
                views.add(img);
            }
        }

        for (int i = 0, length = views.size(); i < 3 - length; i++) {
            SimpleDraweeView img = (SimpleDraweeView) inflater.inflate(R.layout.item_book_detail_img, null);
            Uri uri = Uri.parse("res://com.weiqianghu.usedbook_shop/" + R.mipmap.upload_img);
            img.setImageURI(uri);
            views.add(img);
        }

        return views;
    }*/
}
