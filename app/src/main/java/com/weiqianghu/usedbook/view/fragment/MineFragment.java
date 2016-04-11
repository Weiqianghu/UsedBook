package com.weiqianghu.usedbook.view.fragment;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.UserBean;
import com.weiqianghu.usedbook.presenter.EditUserPresenter;
import com.weiqianghu.usedbook.presenter.IsLoginPresenter;
import com.weiqianghu.usedbook.presenter.UploadFileByPathPresenter;
import com.weiqianghu.usedbook.util.CallBackHandler;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.util.FileUtil;
import com.weiqianghu.usedbook.util.FragmentUtil;
import com.weiqianghu.usedbook.util.ImgUtil;
import com.weiqianghu.usedbook.util.SelectImgUtil;
import com.weiqianghu.usedbook.util.ThreadPool;
import com.weiqianghu.usedbook.view.activity.AddressActivity;
import com.weiqianghu.usedbook.view.activity.EditUserInfoActivity;
import com.weiqianghu.usedbook.view.activity.MessageListActivity;
import com.weiqianghu.usedbook.view.activity.OrderFormActivity;
import com.weiqianghu.usedbook.view.activity.SettingsActivity;
import com.weiqianghu.usedbook.view.common.BaseFragment;
import com.weiqianghu.usedbook.view.view.IEditUserView;
import com.weiqianghu.usedbook.view.view.IUploadFileByPathView;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class MineFragment extends BaseFragment implements IUploadFileByPathView, IEditUserView {

    private View mSuggestToLoginLayout;
    private IsLoginPresenter mIsLoginPresenter;

    private View mMessage;
    private View mPrefer;
    private View mAddress;

    private View mPay;
    private View mDeliver;
    private View mExpress;
    private View mEvaluate;
    private View mFinish;

    private View mSeetings;
    private View mEditUserInfo;

    private Intent intent;

    private SimpleDraweeView mUserImgImgView;

    private FragmentManager fragmentManager;

    private TextView mUsernameTv;

    private String username;
    private String userImg;
    private UserBean currentUser;

    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    private boolean isFirstIn = true;
    private Context mContext;

    private UploadFileByPathPresenter mUploadFileByPathPresenter;
    private List<String> path;

    private EditUserPresenter mEditUserPresenter;

    @Override
    protected int getLayoutId() {
        Fresco.initialize(getActivity());
        return R.layout.fragment_mine;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        if (isFirstIn) {
            initView(savedInstanceState);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateView();
                }
            }, 500);
            isFirstIn = false;
        }

    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        mSuggestToLoginLayout = mRootView.findViewById(R.id.ly_suggest_to_login);

        Click click = new Click();

        mMessage = mRootView.findViewById(R.id.message);
        mMessage.setOnClickListener(click);

        mPrefer = mRootView.findViewById(R.id.prefer);
        mPrefer.setOnClickListener(click);

        mAddress = mRootView.findViewById(R.id.address);
        mAddress.setOnClickListener(click);

        mPay = mRootView.findViewById(R.id.pay);
        mPay.setOnClickListener(click);

        mDeliver = mRootView.findViewById(R.id.deliver);
        mDeliver.setOnClickListener(click);

        mExpress = mRootView.findViewById(R.id.express);
        mExpress.setOnClickListener(click);

        mEvaluate = mRootView.findViewById(R.id.evaluate);
        mEvaluate.setOnClickListener(click);

        mFinish = mRootView.findViewById(R.id.finish);
        mFinish.setOnClickListener(click);

        mIsLoginPresenter = new IsLoginPresenter();

        mUserImgImgView = (SimpleDraweeView) mRootView.findViewById(R.id.iv_user_img);
        mUserImgImgView.setOnClickListener(click);

        mSeetings = mRootView.findViewById(R.id.setting);
        mSeetings.setOnClickListener(click);

        mEditUserInfo = mRootView.findViewById(R.id.user_info);
        mEditUserInfo.setOnClickListener(click);

        mUsernameTv = (TextView) mRootView.findViewById(R.id.tv_username);

        mContext = getActivity();
        mUploadFileByPathPresenter = new UploadFileByPathPresenter(this, uploadFileHandler);
        mEditUserPresenter = new EditUserPresenter(this, editUserHanler);
    }

    private void updateView() {
        currentUser = BmobUser.getCurrentUser(getActivity(), UserBean.class);
        if (currentUser != null) {
            String userImg = currentUser.getImg();
            username = currentUser.getUsername();
            mUsernameTv.setText(username);

            if (userImg != null && !"".equals(userImg)) {
                mUserImgImgView.setImageURI(Uri.parse(userImg));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                return mIsLoginPresenter.isLogin(getActivity());
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);

                if (aBoolean) {
                    mSuggestToLoginLayout.setVisibility(View.VISIBLE);
                } else {
                    mSuggestToLoginLayout.setVisibility(View.INVISIBLE);
                    updateView();
                }
            }
        }.executeOnExecutor(ThreadPool.getThreadPool());
    }

    private class Click implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (intent == null) {
                intent = new Intent(getActivity(), OrderFormActivity.class);
            }
            switch (v.getId()) {
                case R.id.pay:
                    gotoOrder(Constant.PAY);
                    break;
                case R.id.deliver:
                    gotoOrder(Constant.DELIVER);
                    break;
                case R.id.express:
                    gotoOrder(Constant.EXPRESS);
                    break;
                case R.id.evaluate:
                    gotoOrder(Constant.EVALUATE);
                    break;
                case R.id.finish:
                    gotoOrder(Constant.FINISH);
                    break;
                case R.id.iv_user_img:
                    SelectImgUtil.selectImg(MineFragment.this, MultiImageSelectorActivity.MODE_SINGLE, 1);
                    break;
                case R.id.setting:
                    gotoSeetings();
                    break;
                case R.id.user_info:
                    gotoEditUserInfo();
                    break;
                case R.id.address:
                    gotoAddress();
                    break;
                case R.id.prefer:
                    gotoPrefer();
                    break;
                case R.id.message:
                    gotoMessageList();
                    break;
            }
        }
    }

    private void gotoMessageList() {
        Intent intent = new Intent(getActivity(), MessageListActivity.class);
        startActivity(intent);
    }

    private void gotoPrefer() {
        if (mFragmentManager == null) {
            mFragmentManager = getActivity().getSupportFragmentManager();
        }
        mFragment = mFragmentManager.findFragmentByTag(PreferFragment.TAG);
        if (mFragment == null) {
            mFragment = new PreferFragment();
        }

        Fragment from = mFragmentManager.findFragmentByTag(MainLayoutFragment.TAG);
        FragmentUtil.switchContentAddToBackStack(from, mFragment, R.id.main_container, mFragmentManager, PreferFragment.TAG);
    }

    void gotoSeetings() {
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent);
    }

    void gotoEditUserInfo() {
        Intent intent = new Intent(getActivity(), EditUserInfoActivity.class);
        startActivity(intent);
    }

    private void gotoAddress() {
        Intent intent = new Intent(getActivity(), AddressActivity.class);
        startActivity(intent);
    }

    private void gotoOrder(String tab) {
        if (mFragmentManager == null) {
            mFragmentManager = getActivity().getSupportFragmentManager();
        }
        mFragment = mFragmentManager.findFragmentByTag(OrderFormFragment.TAG);
        if (mFragment == null) {
            mFragment = new OrderFormFragment();

            Bundle args = new Bundle();
            args.putString(Constant.ORDER_TAB, tab);
            mFragment.setArguments(args);
        } else {
            Bundle args = mFragment.getArguments();
            args.putString(Constant.ORDER_TAB, tab);
        }

        Fragment from = mFragmentManager.findFragmentByTag(MainLayoutFragment.TAG);
        FragmentUtil.switchContentAddToBackStack(from, mFragment, R.id.main_container, mFragmentManager, OrderFormFragment.TAG);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_IMAGE) {
            if (resultCode == getActivity().RESULT_OK) {
                // 获取返回的图片列表
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                if (path.size() > 0) {
                    if (mUserImgImgView == null) {
                        mUserImgImgView = (SimpleDraweeView) mRootView.findViewById(R.id.iv_user_img);
                    }
                    String smallImgPath = ImgUtil.getSmallImgPath(path.get(0), mUserImgImgView.getWidth(), mUserImgImgView.getHeight());
                    updateImg(smallImgPath);
                    mUploadFileByPathPresenter.uploadFileByPath(mContext, smallImgPath);
                }
            }
        }
    }

    private void updateImg(String smallImgPath) {
        if (smallImgPath != null) {
            Uri uri = FileUtil.getUriByPath(smallImgPath);
            if (uri != null) {
                mUserImgImgView.setImageURI(uri);
            }
        }
    }

    CallBackHandler uploadFileHandler = new CallBackHandler() {
        public void handleSuccessMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    Bundle bundle = msg.getData();
                    BmobFile file = (BmobFile) bundle.getSerializable(Constant.FILE);
                    String fileUrl = file.getUrl();

                    UserBean userBean = new UserBean();
                    userBean.setImg(fileUrl);
                    userBean.setSex(currentUser.isSex());
                    userBean.setAge(currentUser.getAge());
                    userBean.setShop(currentUser.isShop());

                    mEditUserPresenter.updateUser(mContext, userBean);
                    break;
            }
        }

        public void handleFailureMessage(String msg) {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        }
    };


    CallBackHandler editUserHanler = new CallBackHandler() {
        public void handleFailureMessage(String msg) {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        }
    };

}
