package com.weiqianghu.usedbook.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.UserBean;
import com.weiqianghu.usedbook.presenter.EditUserPresenter;
import com.weiqianghu.usedbook.presenter.UploadFileByPathPresenter;
import com.weiqianghu.usedbook.util.CallBackHandler;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.util.FileUtil;
import com.weiqianghu.usedbook.util.ImgUtil;
import com.weiqianghu.usedbook.view.common.BaseActivity;
import com.weiqianghu.usedbook.view.fragment.MainLayoutFragment;
import com.weiqianghu.usedbook.view.view.IEditUserView;
import com.weiqianghu.usedbook.view.view.IUploadFileByPathView;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.update.BmobUpdateAgent;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class MainActivity extends BaseActivity implements IUploadFileByPathView, IEditUserView {


    private FragmentManager mFragmentManager;

    private SimpleDraweeView mUserImgView;

    private UploadFileByPathPresenter mUploadFileByPathPresenter;
    private List<String> path;

    private EditUserPresenter mEditUserPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }
        Fragment fragment = mFragmentManager.findFragmentByTag(MainLayoutFragment.TAG);
        if (fragment == null) {
            fragment = new MainLayoutFragment();

            Bundle bundle = new Bundle();
            bundle.putInt(Constant.TAB, 0);
            fragment.setArguments(bundle);
        } else {
            Bundle bundle = fragment.getArguments();
            bundle.putInt(Constant.TAB, 0);
        }


        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.replace(R.id.main_container, fragment, MainLayoutFragment.TAG);
        ft.commit();

        BmobUpdateAgent.update(this);

        mUploadFileByPathPresenter = new UploadFileByPathPresenter(this, uploadFileHandler);
        mEditUserPresenter = new EditUserPresenter(this, editUserHanler);
    }

    public void gotoLogin(View view) {
        Intent intent = new Intent(this, LoginAndRegisterActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                // 获取返回的图片列表
                path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                if (path.size() > 0) {
                    if (mUserImgView == null) {
                        mUserImgView = (SimpleDraweeView) findViewById(R.id.iv_user_img);
                    }
                    String smallImgPath = ImgUtil.getSmallImgPath(path.get(0), mUserImgView.getWidth(), mUserImgView.getHeight());
                    updateImg(smallImgPath);
                    mUploadFileByPathPresenter.uploadFileByPath(MainActivity.this, smallImgPath);
                }
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
                    UserBean currentUser = BmobUser.getCurrentUser(MainActivity.this, UserBean.class);

                    UserBean userBean = new UserBean();
                    userBean.setImg(fileUrl);
                    userBean.setSex(currentUser.isSex());
                    userBean.setAge(currentUser.getAge());

                    mEditUserPresenter.updateUser(MainActivity.this, userBean);
                    break;
            }
        }

        public void handleFailureMessage(String msg) {
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    };

    private void updateImg(String path) {
        Uri uri = FileUtil.getUriByPath(path);
        if (uri != null) {
            mUserImgView.setImageURI(uri);
        }
    }

    CallBackHandler editUserHanler = new CallBackHandler() {
        public void handleSuccessMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    break;
            }
        }

        public void handleFailureMessage(String msg) {
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    };
}
