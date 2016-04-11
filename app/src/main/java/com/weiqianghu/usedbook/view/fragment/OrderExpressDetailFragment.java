package com.weiqianghu.usedbook.view.fragment;


import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.AddressBean;
import com.weiqianghu.usedbook.model.entity.BookBean;
import com.weiqianghu.usedbook.model.entity.BookImgsBean;
import com.weiqianghu.usedbook.model.entity.BookModel;
import com.weiqianghu.usedbook.model.entity.OrderBean;
import com.weiqianghu.usedbook.model.entity.OrderModel;
import com.weiqianghu.usedbook.presenter.QueryAddressPresenter;
import com.weiqianghu.usedbook.presenter.UpdatePresenter;
import com.weiqianghu.usedbook.util.CallBackHandler;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.util.FragmentUtil;
import com.weiqianghu.usedbook.util.OrderStateUtil;
import com.weiqianghu.usedbook.view.common.BaseFragment;
import com.weiqianghu.usedbook.view.view.IQueryView;
import com.weiqianghu.usedbook.view.view.IUpdateView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderExpressDetailFragment extends BaseFragment implements IQueryView, IUpdateView {
    public static final String TAG = OrderExpressDetailFragment.class.getSimpleName();

    private ImageView mTopBarLeftBtn;
    private TextView mTopBarText;

    private OrderModel mOrderModel = new OrderModel();
    private OrderBean mOrderBean = new OrderBean();

    private SimpleDraweeView mBookImgIv;
    private TextView mBookNameTv;
    private TextView mBookAuthorTv;
    private TextView mPercentDescribeTv;
    private TextView mOrderStateTv;
    private TextView mBookPriceTv;
    private TextView mAmountTv;

    private TextView mAddressNameTv;
    private TextView mAddressMobileTv;
    private TextView mAddressTv;

    private TextView mTimeTv;

    private QueryAddressPresenter mQueryAddressPresenter;

    private CardView mBookCv;
    private FragmentManager mFragmentManager;
    private UpdatePresenter<OrderBean> mUpdatePresenter;

    private Button mSubmitBtn;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order_express_detail;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initData();
        initView(savedInstanceState);
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mOrderModel = bundle.getParcelable(Constant.DATA);
            mOrderBean = mOrderModel.getOrderBean();
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTopBarLeftBtn = (ImageView) mRootView.findViewById(R.id.top_bar_left_button);
        mTopBarLeftBtn.setImageResource(R.mipmap.back);
        mTopBarLeftBtn.setOnClickListener(new Click());

        mTopBarText = (TextView) mRootView.findViewById(R.id.tv_top_bar_text);
        mTopBarText.setText(R.string.order_detail);

        mBookImgIv = (SimpleDraweeView) mRootView.findViewById(R.id.iv_book_img);
        mBookNameTv = (TextView) mRootView.findViewById(R.id.tv_book_name);
        mBookAuthorTv = (TextView) mRootView.findViewById(R.id.tv_book_author);
        mPercentDescribeTv = (TextView) mRootView.findViewById(R.id.tv_percent_describe);
        mOrderStateTv = (TextView) mRootView.findViewById(R.id.tv_order_state);
        mBookPriceTv = (TextView) mRootView.findViewById(R.id.tv_price);
        mAmountTv = (TextView) mRootView.findViewById(R.id.tv_amount);

        BookBean book = mOrderBean.getBook();
        List<BookImgsBean> bookImgs = mOrderModel.getBookImgs();

        if (bookImgs != null && bookImgs.size() > 0) {
            Uri uri = Uri.parse(bookImgs.get(0).getImg());
            mBookImgIv.setImageURI(uri);
        } else {
            Uri uri = Uri.parse("res://com.weiqianghu.usedbook/" + R.mipmap.upload_img);
            mBookImgIv.setImageURI(uri);
        }
        if (book != null) {
            mBookNameTv.setText(book.getBookName());
            mBookAuthorTv.setText(book.getAuthor());
            mBookPriceTv.setText(book.getPercentDescribe());
            mOrderStateTv.setText(OrderStateUtil.getStrByOrderState(mOrderBean.getOrderState()));
            mBookPriceTv.setText(String.valueOf(book.getPrice() + "￥"));
            mAmountTv.setText(String.valueOf("X" + mOrderBean.getAmount()));
            mPercentDescribeTv.setText(book.getPercentDescribe());
        }

        mAddressNameTv = (TextView) mRootView.findViewById(R.id.tv_name);
        mAddressMobileTv = (TextView) mRootView.findViewById(R.id.tv_mobile_no);
        mAddressTv = (TextView) mRootView.findViewById(R.id.tv_address);

        mQueryAddressPresenter = new QueryAddressPresenter(this, queryAddressHandler);
        mQueryAddressPresenter.QueryAddress(getActivity(), mOrderBean.getAddress().getObjectId());

        mTimeTv = (TextView) mRootView.findViewById(R.id.tv_time);
        mTimeTv.setText(mOrderBean.getCreatedAt().substring(0, 10));

        mBookCv = (CardView) mRootView.findViewById(R.id.book);
        mBookCv.setOnClickListener(new Click());

        mSubmitBtn = (Button) mRootView.findViewById(R.id.btn_submit);
        mSubmitBtn.setOnClickListener(new Click());
        mUpdatePresenter = new UpdatePresenter<OrderBean>(this, updateOrderHandler);
    }

    CallBackHandler queryAddressHandler = new CallBackHandler() {
        @Override
        public void handleSuccessMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    Bundle bundle = msg.getData();
                    AddressBean address = bundle.getParcelable(Constant.DATA);

                    mAddressNameTv.setText(address.getName());
                    mAddressMobileTv.setText(address.getMobileNo());
                    String addressStr = new StringBuffer().append(address.getProvince()).
                            append(address.getCity()).
                            append(address.getCounty()).
                            append(address.getDetailAddress()).
                            toString();
                    mAddressTv.setText(addressStr);
            }
        }

        @Override
        public void handleFailureMessage(String msg) {
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
    };

    private class Click implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.top_bar_left_button:
                    getActivity().onBackPressed();
                    break;
                case R.id.book:
                    gotoBookDetail();
                    break;
                case R.id.btn_submit:
                    confirmReceipt();
                    break;
            }
        }
    }

    void gotoBookDetail() {

        if (mFragmentManager == null) {
            mFragmentManager = getActivity().getSupportFragmentManager();
        }
        Fragment fragment = mFragmentManager.findFragmentByTag(BookDetailFragment.TAG);

        BookModel bookModel = new BookModel();
        bookModel.setBook(mOrderBean.getBook());
        bookModel.setBookImgs(mOrderModel.getBookImgs());

        if (fragment == null) {
            fragment = BookDetailFragment.getInstance();

            Bundle args = new Bundle();
            args.putParcelable(Constant.BOOK, bookModel);
            fragment.setArguments(args);
        } else {
            Bundle args = fragment.getArguments();
            args.putParcelable(Constant.BOOK, bookModel);
        }

        Fragment form = mFragmentManager.findFragmentByTag(OrderExpressDetailFragment.TAG);

        FragmentUtil.switchContentAddToBackStack(form, fragment, R.id.main_container, mFragmentManager, BookDetailFragment.TAG);
    }

    private void confirmReceipt() {
        mOrderBean.setOrderState(Constant.EVALUATE);
        mUpdatePresenter.update(getActivity(), mOrderBean, mOrderBean.getObjectId());
    }

    CallBackHandler updateOrderHandler = new CallBackHandler() {
        @Override
        public void handleSuccessMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    Toast.makeText(getActivity(), "订单处理成功", Toast.LENGTH_SHORT).show();
                    mSubmitBtn.setClickable(false);
            }
        }

        @Override
        public void handleFailureMessage(String msg) {
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
    };

}
