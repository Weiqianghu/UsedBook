package com.weiqianghu.usedbook.view.activity;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.AddressBean;
import com.weiqianghu.usedbook.model.entity.BookBean;
import com.weiqianghu.usedbook.model.entity.OrderBean;
import com.weiqianghu.usedbook.model.entity.ShopBean;
import com.weiqianghu.usedbook.model.entity.ShoppingCartBean;
import com.weiqianghu.usedbook.model.entity.ShoppingCartModel;
import com.weiqianghu.usedbook.model.entity.UserBean;
import com.weiqianghu.usedbook.presenter.QueryAddressPresenter;
import com.weiqianghu.usedbook.presenter.QueryBooksPresenter;
import com.weiqianghu.usedbook.presenter.SavePresenter;
import com.weiqianghu.usedbook.presenter.UpdatePresenter;
import com.weiqianghu.usedbook.presenter.adapter.OrderAdapter;
import com.weiqianghu.usedbook.util.CallBackHandler;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.util.DoubleUtil;
import com.weiqianghu.usedbook.view.view.IQueryView;
import com.weiqianghu.usedbook.view.view.ISaveView;
import com.weiqianghu.usedbook.view.view.IUpdateView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;

public class OrderActivity extends AppCompatActivity implements IQueryView, IUpdateView, ISaveView {
    List<ShoppingCartModel> mShoppingModels = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private OrderAdapter mAdapter;

    private TextView mTvTopBarText;
    private ImageView mIvTopBarLeftBtn;

    private TextView mAddressNameTv;
    private TextView mAddressMobileTv;
    private TextView mAddressDetailTv;

    private View address;
    private QueryAddressPresenter mQueryAddressPresenter;

    private double totalPrice = 0;
    private TextView mTotalPriceTv;

    private QueryBooksPresenter mQueryBooksPresenter;
    private UpdatePresenter<BookBean> mUpdatePresenter;
    private UpdatePresenter<ShoppingCartBean> mShoppingCartBeanUpdatePresenter;
    private SavePresenter mSavePresenter;
    private AddressBean mAddress;

    private Button mSubmitBtn;

    int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_order);

        initData();
        initTopBar();
        initView();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new OrderAdapter(mShoppingModels, R.layout.item_order);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        Click click = new Click();
        address = findViewById(R.id.address);
        address.setOnClickListener(click);

        mSubmitBtn = (Button) findViewById(R.id.btn_submit);
        mSubmitBtn.setOnClickListener(click);


        mAddressNameTv = (TextView) findViewById(R.id.tv_name);
        mAddressMobileTv = (TextView) findViewById(R.id.tv_mobile_no);
        mAddressDetailTv = (TextView) findViewById(R.id.tv_address);

        mQueryAddressPresenter = new QueryAddressPresenter(this, queryAddressHandler);
        mQueryAddressPresenter.queryDefaultAddress(OrderActivity.this);

        mTotalPriceTv = (TextView) findViewById(R.id.tv_total_money);
        mTotalPriceTv.setText(String.valueOf(totalPrice));

        mQueryBooksPresenter = new QueryBooksPresenter(queryBookHandler);
        mUpdatePresenter = new UpdatePresenter<>(this, updateBookHandler);
        mSavePresenter = new SavePresenter(this, saveOrderHandler);
        mShoppingCartBeanUpdatePresenter = new UpdatePresenter<>(this, updateShoppingCartHandler);
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            mShoppingModels = intent.getParcelableArrayListExtra(Constant.LIST);
        }
        for (int i = 0, length = mShoppingModels.size(); i < length; i++) {
            totalPrice += mShoppingModels.get(i).getShoppingCartBean().getSubtotal();
        }
        totalPrice = DoubleUtil.subDouble(totalPrice);
    }

    private void initTopBar() {
        mTvTopBarText = (TextView) findViewById(R.id.tv_top_bar_text);
        mTvTopBarText.setText(R.string.order_ensure);

        Click click = new Click();
        mIvTopBarLeftBtn = (ImageView) findViewById(R.id.top_bar_left_button);
        mIvTopBarLeftBtn.setImageResource(R.mipmap.back);
        mIvTopBarLeftBtn.setOnClickListener(click);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        mQueryAddressPresenter.queryDefaultAddress(OrderActivity.this);
    }

    private class Click implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.top_bar_left_button:
                    onBackPressed();
                    break;
                case R.id.address:
                    gotoAddress();
                    break;
                case R.id.btn_submit:
                    pay();
                    break;
            }
        }
    }

    private void pay() {
        if (mShoppingModels == null && mShoppingModels.size() < 0) {
            return;
        }
        for (int i = 0, length = mShoppingModels.size(); i < length; i++) {
            mQueryBooksPresenter.queryBooks(OrderActivity.this, mShoppingModels.get(i).getShoppingCartBean());
        }

    }

    private void gotoAddress() {
        Intent intent = new Intent(OrderActivity.this, AddressActivity.class);
        startActivity(intent);
    }

    CallBackHandler queryAddressHandler = new CallBackHandler() {
        public void handleSuccessMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    Bundle bundle = msg.getData();
                    List<AddressBean> adressList = bundle.getParcelableArrayList(Constant.PARCEABLE);
                    if (adressList != null && adressList.size() > 0) {
                        mAddress = adressList.get(0);
                        mAddressNameTv.setText(mAddress.getName());
                        mAddressMobileTv.setText(mAddress.getMobileNo());
                        mAddressDetailTv.setText(new StringBuffer().
                                append(mAddress.getProvince()).
                                append(mAddress.getCity()).
                                append(mAddress.getCounty()).
                                append(mAddress.getDetailAddress()).toString());
                    }
                    break;
            }
        }

        public void handleFailureMessage(String msg) {
            Toast.makeText(OrderActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    };

    CallBackHandler queryBookHandler = new CallBackHandler() {
        public void handleSuccessMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    Bundle bundle = msg.getData();
                    BookBean book = bundle.getParcelable(Constant.DATA);
                    ShoppingCartBean shoppingCartBean = bundle.getParcelable(Constant.SHOPPINGCART);

                    if (!book.isSell()) {
                        count++;
                        Toast.makeText(OrderActivity.this, book.getBookName() + "已下架！", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (book.getStock() < shoppingCartBean.getNumber()) {
                        count++;
                        Toast.makeText(OrderActivity.this, book.getBookName() + "库存不足，下单未成功！", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        book.setStock(book.getStock() - shoppingCartBean.getNumber());
                        book.setSalesVolume(book.getSalesVolume() + shoppingCartBean.getNumber());
                        mUpdatePresenter.update(OrderActivity.this, book, book.getObjectId(), shoppingCartBean);
                    }

                    break;
            }
        }

        public void handleFailureMessage(String msg) {
            Toast.makeText(OrderActivity.this, msg, Toast.LENGTH_SHORT).show();
            count++;
        }
    };

    CallBackHandler updateBookHandler = new CallBackHandler() {
        public void handleSuccessMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    Bundle bundle = msg.getData();
                    ShoppingCartBean shoppingCartBean = bundle.getParcelable(Constant.SHOPPINGCART);
                    OrderBean orderBean = new OrderBean();

                    BookBean book = shoppingCartBean.getBook();
                    book.setObjectId(book.getObjectIdStr());

                    UserBean user = BmobUser.getCurrentUser(OrderActivity.this, UserBean.class);

                    orderBean.setBook(book);
                    orderBean.setAmount(shoppingCartBean.getNumber());
                    orderBean.setOrderNo(user.getObjectId() + shoppingCartBean.getBook().getObjectIdStr() + new Date());
                    orderBean.setUser(user);
                    orderBean.setTotalPrice(shoppingCartBean.getSubtotal());
                    orderBean.setAddress(mAddress);
                    orderBean.setOrderState(Constant.DELIVER);

                    ShopBean shopBean = new ShopBean();
                    shopBean.setObjectId(shoppingCartBean.getShopObjectId());

                    orderBean.setShop(shopBean);

                    mSavePresenter.save(OrderActivity.this, orderBean, shoppingCartBean);
                    break;
            }
        }

        public void handleFailureMessage(String msg) {
            Toast.makeText(OrderActivity.this, msg, Toast.LENGTH_SHORT).show();
            count++;
        }
    };

    CallBackHandler saveOrderHandler = new CallBackHandler() {
        public void handleSuccessMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    ShoppingCartBean shoppingCartBean = new ShoppingCartBean();
                    Bundle bundle = msg.getData();
                    if (bundle != null) {
                        shoppingCartBean = bundle.getParcelable(Constant.DATA);
                    }

                    shoppingCartBean.setOrder(true);
                    mShoppingCartBeanUpdatePresenter.update(OrderActivity.this, shoppingCartBean, shoppingCartBean.getObjectIdStr());
                    break;
            }
        }

        public void handleFailureMessage(String msg) {
            Toast.makeText(OrderActivity.this, msg, Toast.LENGTH_SHORT).show();
            count++;
        }
    };

    CallBackHandler updateShoppingCartHandler = new CallBackHandler() {
        public void handleSuccessMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    count++;
                    if (count >= mShoppingModels.size()) {
                        onBackPressed();
                        count = 0;
                    }
                    break;
            }
        }

        public void handleFailureMessage(String msg) {
            Toast.makeText(OrderActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    };
}
