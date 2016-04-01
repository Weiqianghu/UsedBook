package com.weiqianghu.usedbook.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hedgehog.ratingbar.RatingBar;
import com.tb.emoji.Emoji;
import com.tb.emoji.EmojiUtil;
import com.tb.emoji.FaceFragment;
import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.BookBean;
import com.weiqianghu.usedbook.model.entity.BookImgsBean;
import com.weiqianghu.usedbook.model.entity.CommentBean;
import com.weiqianghu.usedbook.model.entity.OrderBean;
import com.weiqianghu.usedbook.model.entity.OrderModel;
import com.weiqianghu.usedbook.model.entity.UserBean;
import com.weiqianghu.usedbook.presenter.QueryCommentPresenter;
import com.weiqianghu.usedbook.presenter.SavePresenter;
import com.weiqianghu.usedbook.presenter.UpdatePresenter;
import com.weiqianghu.usedbook.util.CallBackHandler;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.view.view.ISaveView;
import com.weiqianghu.usedbook.view.view.IUpdateView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

public class CommentActivity extends AppCompatActivity implements FaceFragment.OnEmojiClickListener, ISaveView, IUpdateView {

    private TextView mTvTopBarText;
    private ImageView mIvTopBarLeftBtn;

    private FragmentTransaction transaction;
    private FaceFragment faceFragment;
    private ImageView mEmoji;

    private EditText mCommentContentEt;

    private boolean isSoftInputVisble = false;

    private OrderModel mOrderModel = new OrderModel();

    private Button mSubmitBtn;

    private int mRating = 5;
    private SavePresenter mSavePresenter;
    private QueryCommentPresenter mQueryCommentPresenter;
    CommentBean comment = new CommentBean();
    private UpdatePresenter<OrderBean> mUpdatePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        initTopBar();
        initData();
        initView();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra(Constant.DATA);
            if (bundle != null) {
                mOrderModel = bundle.getParcelable(Constant.DATA);
            }
        }
    }

    private void initView() {
        RatingBar mRatingBar = (RatingBar) findViewById(R.id.ratingbar);
        mRatingBar.setStar(5);
        mRatingBar.setOnRatingChangeListener(
                new RatingBar.OnRatingChangeListener() {
                    @Override
                    public void onRatingChange(int RatingCount) {
                        mRating = RatingCount;
                    }
                }
        );

        Click click = new Click();

        faceFragment = FaceFragment.Instance();
        mEmoji = (ImageView) findViewById(R.id.emoji);
        mEmoji.setOnClickListener(click);

        mCommentContentEt = (EditText) findViewById(R.id.et_comment_content);
        mCommentContentEt.setOnClickListener(click);

        final View activityRootView = findViewById(R.id.activityRoot);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...
                    isSoftInputVisble = true;
                } else {
                    isSoftInputVisble = false;
                }
            }
        });

        SimpleDraweeView bookIv = (SimpleDraweeView) findViewById(R.id.iv_book_img);
        List<BookImgsBean> bookImgs = mOrderModel.getBookImgs();
        if (bookImgs != null && bookImgs.size() > 0) {
            Uri uri = Uri.parse(bookImgs.get(0).getImg());
            bookIv.setImageURI(uri);
        }
        TextView bookNameTv = (TextView) findViewById(R.id.tv_book_name);
        TextView bookAuthorTv = (TextView) findViewById(R.id.tv_book_author);
        TextView bookPercentDescrbeTv = (TextView) findViewById(R.id.tv_percent_describe);
        TextView bookPriceTv = (TextView) findViewById(R.id.tv_price);
        TextView bookAmountTv = (TextView) findViewById(R.id.tv_amount);

        OrderBean orderBean = mOrderModel.getOrderBean();
        BookBean book = null;
        if (orderBean != null) {
            book = orderBean.getBook();
        }
        if (book != null) {
            bookNameTv.setText(book.getBookName());
            bookAuthorTv.setText(book.getAuthor());
            bookPercentDescrbeTv.setText(book.getPercentDescribe());
            bookPriceTv.setText(String.valueOf(book.getPrice() + "￥"));
            bookAmountTv.setText(String.valueOf("X" + orderBean.getAmount()));

        }

        mSubmitBtn = (Button) findViewById(R.id.btn_submit);
        mSubmitBtn.setOnClickListener(click);

    }

    private void initTopBar() {
        mTvTopBarText = (TextView) findViewById(R.id.tv_top_bar_text);
        mTvTopBarText.setText(R.string.publish_comment);

        Click click = new Click();
        mIvTopBarLeftBtn = (ImageView) findViewById(R.id.top_bar_left_button);
        mIvTopBarLeftBtn.setImageResource(R.mipmap.back);
        mIvTopBarLeftBtn.setOnClickListener(click);
    }

    private void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isSoftInputVisble) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onEmojiDelete() {
        int index = mCommentContentEt.getSelectionStart();
        int length = EmojiUtil.deleteEmoji(mCommentContentEt, index);
        String str = mCommentContentEt.getText().toString();
        EmojiUtil.displayTextView(mCommentContentEt, str, CommentActivity.this);
        mCommentContentEt.setSelection(index - length);
    }

    @Override
    public void onEmojiClick(Emoji emoji) {
        if (emoji != null) {
            StringBuffer editString = new StringBuffer(mCommentContentEt.getText());
            int index = mCommentContentEt.getSelectionStart();
            if (index < 0) {
                editString.append(emoji.getContent());
            } else {
                editString.insert(index, emoji.getContent());
            }

            EmojiUtil.displayTextView(mCommentContentEt, editString.toString(), CommentActivity.this);
            mCommentContentEt.setSelection(index + emoji.getContent().length());
        }
    }

    private class Click implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.top_bar_left_button:
                    onBackPressed();
                    break;
                case R.id.emoji:
                    selectEmoji();
                    break;
                case R.id.et_comment_content:
                    if (faceFragment.isAdded() && !faceFragment.isHidden()) {
                        hideEmoji();
                    }
                    break;
                case R.id.btn_submit:
                    publishComment();
                    break;
            }
        }
    }

    private void publishComment() {
        String commentContent = mCommentContentEt.getText().toString().trim();
        if (TextUtils.isEmpty(commentContent)) {
            Toast.makeText(CommentActivity.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        UserBean user = BmobUser.getCurrentUser(CommentActivity.this, UserBean.class);
        if (user == null || mOrderModel == null) {
            return;
        }
        OrderBean order = mOrderModel.getOrderBean();
        if (order == null) {
            return;
        }
        order.setObjectId(order.getObjectIdStr());
        BookBean book = mOrderModel.getOrderBean().getBook();
        if (book == null) {
            return;
        }
        book.setObjectId(book.getObjectIdStr());
        mSubmitBtn.setClickable(false);
        comment.setUser(user);
        comment.setOrder(order);
        comment.setBook(book);
        comment.setGrade(mRating);
        comment.setContent(commentContent);
        if (mSavePresenter == null) {
            mSavePresenter = new SavePresenter(this, publishCommentHandler);
        }
        if (mQueryCommentPresenter == null) {
            mQueryCommentPresenter = new QueryCommentPresenter(queryCommentHanler);
        }
        mQueryCommentPresenter.queryComment(CommentActivity.this, comment);
    }

    CallBackHandler queryCommentHanler = new CallBackHandler() {
        public void handleSuccessMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    Bundle bundle = msg.getData();
                    if (bundle != null) {
                        int exist = bundle.getInt(Constant.EXIST);
                        if (Constant.TRUE == exist) {
                            Toast.makeText(CommentActivity.this, "您已经对此条订单做过评论了，不能重复评论", Toast.LENGTH_SHORT).show();
                        } else {
                            mSavePresenter.save(CommentActivity.this, comment);
                        }
                    }
                    mSubmitBtn.setClickable(true);
                    break;
            }
        }

        public void handleFailureMessage(String msg) {
            Toast.makeText(CommentActivity.this, msg, Toast.LENGTH_SHORT).show();
            mSubmitBtn.setClickable(true);
        }
    };

    CallBackHandler publishCommentHandler = new CallBackHandler() {
        @Override
        public void handleSuccessMessage(Message msg) {
            super.handleSuccessMessage(msg);
            switch (msg.what) {
                case Constant.SUCCESS:
                    if (mUpdatePresenter == null) {
                        mUpdatePresenter = new UpdatePresenter<OrderBean>(CommentActivity.this, updateOrderHandler);
                    }

                    OrderBean order = mOrderModel.getOrderBean();
                    order.setOrderState(Constant.FINISH);
                    order.getShop().setObjectId(order.getShop().getObjectIdStr());
                    order.getAddress().setObjectId(order.getAddress().getObjectIdStr());

                    mUpdatePresenter.update(CommentActivity.this, order, order.getObjectId());
            }
        }

        @Override
        public void handleFailureMessage(String msg) {
            super.handleFailureMessage(msg);
            Toast.makeText(CommentActivity.this, msg, Toast.LENGTH_SHORT).show();
            mSubmitBtn.setClickable(true);
        }
    };

    CallBackHandler updateOrderHandler = new CallBackHandler() {
        @Override
        public void handleSuccessMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    Toast.makeText(CommentActivity.this, "评论提交成功", Toast.LENGTH_SHORT).show();
                    mSubmitBtn.setClickable(false);
                    onBackPressed();
            }
        }

        @Override
        public void handleFailureMessage(String msg) {
            Toast.makeText(CommentActivity.this, msg, Toast.LENGTH_SHORT).show();
            mSubmitBtn.setClickable(true);
        }
    };

    public void selectEmoji() {
        transaction = getSupportFragmentManager()
                .beginTransaction();
        if (!faceFragment.isAdded()) {
            transaction.add(R.id.emoji_container, faceFragment).commit();
            mEmoji.setImageResource(R.mipmap.emoji_open);
            hideInput();
        } else if (faceFragment.isHidden()) {
            transaction.show(faceFragment).commit();
            mEmoji.setImageResource(R.mipmap.emoji_open);
            hideInput();
        } else {
            transaction.hide(faceFragment).commit();
            mEmoji.setImageResource(R.mipmap.emoji);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (faceFragment.isAdded() && !faceFragment.isHidden()) {
                hideEmoji();
            } else {
                onBackPressed();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void hideEmoji() {
        transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.hide(faceFragment).commit();
        mEmoji.setImageResource(R.mipmap.emoji);
    }

}
