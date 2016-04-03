package com.weiqianghu.usedbook.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.BookBean;
import com.weiqianghu.usedbook.model.entity.BookModel;
import com.weiqianghu.usedbook.model.entity.PreferBean;
import com.weiqianghu.usedbook.model.entity.ShoppingCartBean;
import com.weiqianghu.usedbook.model.entity.UserBean;
import com.weiqianghu.usedbook.presenter.AddPreferPresenter;
import com.weiqianghu.usedbook.presenter.BooksDetailPresenter;
import com.weiqianghu.usedbook.presenter.AddShoppingCartPresenter;
import com.weiqianghu.usedbook.presenter.QueryPreferPresenter;
import com.weiqianghu.usedbook.presenter.QueryShoppingCartPresenter;
import com.weiqianghu.usedbook.presenter.QueryUserPresenter;
import com.weiqianghu.usedbook.presenter.adapter.MViewPagerAdapter;
import com.weiqianghu.usedbook.util.CallBackHandler;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.util.FragmentUtil;
import com.weiqianghu.usedbook.view.activity.ChatActivity;
import com.weiqianghu.usedbook.view.activity.CommentListActivity;
import com.weiqianghu.usedbook.view.common.BaseFragment;
import com.weiqianghu.usedbook.view.view.IBooksDetailView;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;


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

    private AddShoppingCartPresenter mAddShoppingCartPresenter;
    private Button mAddShoppingCartBtn;
    private QueryShoppingCartPresenter mQueryShoppingCartPresenter;

    private QueryPreferPresenter mQueryPreferPresenter;
    private AddPreferPresenter mAddPreferPresenter;
    private Button mAddPreferBtn;

    private Button mEnterShopBtn;
    private Button mChatBtn;

    private QueryUserPresenter mQueryUserPresenter;


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

        Click click = new Click();

        mBookImgVp = (ViewPager) mRootView.findViewById(R.id.vp_book_img);
        if (mBooksDetailPresenter == null) {
            mBooksDetailPresenter = new BooksDetailPresenter(getActivity(), this, mBookModel.getBookImgs());
        }

        mTopBarLeftBtn = (ImageView) mRootView.findViewById(R.id.top_bar_left_button);
        mTopBarLeftBtn.setImageResource(R.mipmap.back);
        mTopBarLeftBtn.setOnClickListener(click);

        mTopBarText = (TextView) mRootView.findViewById(R.id.tv_top_bar_text);
        mTopBarText.setText(R.string.book_detail);

        mPostionTv = (TextView) mRootView.findViewById(R.id.tv_position);

        mCommentView = mRootView.findViewById(R.id.comment);
        mCommentView.setOnClickListener(click);

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
        mShoppingCartIV.setOnClickListener(click);

        mAddShoppingCartPresenter = new AddShoppingCartPresenter(addShoppingCartHanler);
        mAddShoppingCartBtn = (Button) mRootView.findViewById(R.id.btn_shopping_cart);
        mAddShoppingCartBtn.setOnClickListener(click);
        mQueryShoppingCartPresenter = new QueryShoppingCartPresenter(queryShoppingCartHanler);

        mQueryPreferPresenter = new QueryPreferPresenter(queryPreferHanler);
        mAddPreferBtn = (Button) mRootView.findViewById(R.id.btn_prefer);
        mAddPreferBtn.setOnClickListener(click);
        mAddPreferPresenter = new AddPreferPresenter(addPreferHanler);

        mEnterShopBtn = (Button) mRootView.findViewById(R.id.btn_enter_shop);
        mEnterShopBtn.setOnClickListener(click);

        mChatBtn = (Button) mRootView.findViewById(R.id.btn_chat);
        mChatBtn.setOnClickListener(click);

        mQueryUserPresenter = new QueryUserPresenter(queryUserHanler);
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
                    Toast.makeText(getActivity(), "快去购物车结算吧！", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_shopping_cart:
                    checkAddShoppingCart();
                    break;
                case R.id.btn_prefer:
                    checkAddPrefer();
                    break;
                case R.id.btn_enter_shop:
                    gotoShop();
                    break;
                case R.id.comment:
                    gotoCommentList();
                    break;
                case R.id.btn_chat:
                    gotoChat();
                    break;
            }
        }
    }

    private void gotoChat() {
        if (BmobUser.getCurrentUser(getActivity()) == null) {
            Toast.makeText(getActivity(), "您还没登陆，请登录后再试", Toast.LENGTH_SHORT).show();
            return;
        }

        UserBean user = BmobUser.getCurrentUser(getActivity(), UserBean.class);
        BmobIM.connect(user.getObjectId(), new ConnectListener() {
            @Override
            public void done(String uid, BmobException e) {
                if (e == null) {
                    Log.i("im", "connect success");
                } else {
                    Log.e("im", e.getErrorCode() + "/" + e.getMessage());
                }
            }
        });

        mQueryUserPresenter.queryUser(getActivity(), mBookModel.getBook().getShop());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BmobIM.getInstance().disConnect();
    }

    CallBackHandler queryUserHanler = new CallBackHandler() {
        public void handleSuccessMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    Bundle bundle = msg.getData();
                    if (bundle != null) {
                        List<UserBean> users = bundle.getParcelableArrayList(Constant.LIST);
                        if (users != null && users.size() > 0) {
                            final UserBean user = users.get(0);

                            BmobIMUserInfo info = new BmobIMUserInfo();
                            info.setUserId(user.getObjectId());
                            info.setName(user.getUsername());
                            info.setAvatar(user.getImg());

                            BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
                                @Override
                                public void done(BmobIMConversation c, BmobException e) {
                                    if (e == null) {
                                        Intent intent = new Intent(getActivity(), ChatActivity.class);

                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("c", c);
                                        bundle.putParcelable(Constant.DATA, user);
                                        intent.putExtra("c", bundle);

                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getActivity(), e.getMessage() + "(" + e.getErrorCode() + ")", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                    mAddShoppingCartBtn.setClickable(true);
                    break;
            }
        }

        public void handleFailureMessage(String msg) {
            mAddShoppingCartBtn.setClickable(true);
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            mAddShoppingCartBtn.setClickable(true);
        }
    };

    private void gotoCommentList() {
        Intent intent = new Intent(getActivity(), CommentListActivity.class);
        Bundle bundle = new Bundle();

        BookBean book = mBookModel.getBook();
        book.setObjectIdStr(book.getObjectId());
        bundle.putParcelable(Constant.DATA, book);

        intent.putExtra(Constant.DATA, bundle);
        startActivity(intent);
    }

    private void checkAddShoppingCart() {

        if (mBookModel.getBook().getStock() > 0) {

            mAddShoppingCartBtn.setClickable(false);
            ShoppingCartBean shoppingCartBean = new ShoppingCartBean();
            BookBean book = mBookModel.getBook();
            UserBean user = BmobUser.getCurrentUser(getActivity(), UserBean.class);

            shoppingCartBean.setUser(user);
            shoppingCartBean.setBook(book);

            mQueryShoppingCartPresenter.queryShoppingCart(getActivity(), shoppingCartBean);
        } else {
            Toast.makeText(getActivity(), R.string.stock_not_enough, Toast.LENGTH_SHORT).show();
        }
    }

    private void addShoppingCart() {
        ShoppingCartBean shoppingCartBean = new ShoppingCartBean();
        BookBean book = mBookModel.getBook();
        UserBean user = BmobUser.getCurrentUser(getActivity(), UserBean.class);

        shoppingCartBean.setUser(user);
        shoppingCartBean.setBook(book);
        shoppingCartBean.setNumber(1);
        shoppingCartBean.setSubtotal(book.getPrice());
        shoppingCartBean.setOrder(false);
        shoppingCartBean.setPrice(book.getPrice());
        shoppingCartBean.setChecked(false);
        shoppingCartBean.setShop(book.getShop());

        mAddShoppingCartPresenter.addShoppingCart(getActivity(), shoppingCartBean);
    }

    CallBackHandler addShoppingCartHanler = new CallBackHandler() {
        public void handleSuccessMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    mAddShoppingCartBtn.setClickable(true);
                    Toast.makeText(getActivity(), "添加成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        public void handleFailureMessage(String msg) {
            mAddShoppingCartBtn.setClickable(true);
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
    };

    CallBackHandler queryShoppingCartHanler = new CallBackHandler() {
        public void handleSuccessMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    Bundle bundle = msg.getData();
                    if (bundle != null) {
                        int exist = bundle.getInt(Constant.EXIST);
                        if (Constant.TRUE == exist) {
                            Toast.makeText(getActivity(), "已经在购物车中", Toast.LENGTH_SHORT).show();
                        } else {
                            addShoppingCart();
                        }
                    }
                    mAddShoppingCartBtn.setClickable(true);
                    break;
            }
        }

        public void handleFailureMessage(String msg) {
            mAddShoppingCartBtn.setClickable(true);
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            mAddShoppingCartBtn.setClickable(true);
        }
    };

    CallBackHandler queryPreferHanler = new CallBackHandler() {
        public void handleSuccessMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    Bundle bundle = msg.getData();
                    if (bundle != null) {
                        int exist = bundle.getInt(Constant.EXIST);
                        if (Constant.TRUE == exist) {
                            Toast.makeText(getActivity(), "已在收藏夹中", Toast.LENGTH_SHORT).show();
                        } else {
                            addPrefer();
                        }
                    }
                    mAddPreferBtn.setClickable(true);
                    break;
            }
        }

        public void handleFailureMessage(String msg) {
            mAddShoppingCartBtn.setClickable(true);
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            mAddPreferBtn.setClickable(true);
        }
    };

    private void addPrefer() {
        PreferBean preferBean = new PreferBean();
        BookBean book = mBookModel.getBook();
        UserBean user = BmobUser.getCurrentUser(getActivity(), UserBean.class);

        preferBean.setUser(user);
        preferBean.setBook(book);
        preferBean.setDelete(false);

        mAddPreferPresenter.addPrefer(getActivity(), preferBean);
    }

    private void checkAddPrefer() {
        mAddPreferBtn.setClickable(false);
        PreferBean preferBean = new PreferBean();
        BookBean book = mBookModel.getBook();
        UserBean user = BmobUser.getCurrentUser(getActivity(), UserBean.class);

        preferBean.setUser(user);
        preferBean.setBook(book);

        mQueryPreferPresenter.queryPrefer(getActivity(), preferBean);
    }

    CallBackHandler addPreferHanler = new CallBackHandler() {
        public void handleSuccessMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    mAddPreferBtn.setClickable(true);
                    Toast.makeText(getActivity(), "添加成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        public void handleFailureMessage(String msg) {
            mAddPreferBtn.setClickable(true);
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
    };

    private void gotoShop() {
        if (mFragmentManager == null) {
            mFragmentManager = getActivity().getSupportFragmentManager();
        }
        Fragment fragment = mFragmentManager.findFragmentByTag(ShopFragment.TAG);
        if (null == fragment) {
            fragment = new ShopFragment();

            Bundle bundle = new Bundle();
            bundle.putParcelable(Constant.DATA, mBookModel.getBook().getShop());
            fragment.setArguments(bundle);
        } else {
            Bundle args = fragment.getArguments();
            args.putParcelable(Constant.DATA, mBookModel.getBook().getShop());
        }
        Fragment from = mFragmentManager.findFragmentByTag(BookDetailFragment.TAG);
        FragmentUtil.switchContentAddToBackStack(from, fragment, R.id.main_container, mFragmentManager, ShopFragment.TAG);
    }

}
