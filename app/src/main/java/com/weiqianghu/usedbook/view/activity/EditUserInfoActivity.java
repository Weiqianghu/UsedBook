package com.weiqianghu.usedbook.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.UserBean;
import com.weiqianghu.usedbook.presenter.EditUserPresenter;
import com.weiqianghu.usedbook.util.CallBackHandler;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.util.FragmentUtil;
import com.weiqianghu.usedbook.view.fragment.UpdatePwdFragment;
import com.weiqianghu.usedbook.view.view.IEditUserView;
import com.weiqianghu.usedbook.view.view.ILoginView;

import cn.bmob.v3.BmobUser;

public class EditUserInfoActivity extends AppCompatActivity implements IEditUserView, ILoginView {

    private TextView mTvTopBarText;
    private ImageView mIvTopBarLeftBtn;

    private TextView mMobileNoTv;
    private TextView mUsernameTv;
    private TextView mSexTv;
    private TextView mAgeTv;

    private String username = "";
    private String sex = "";
    private int age = 0;
    private String mobileNo = "";

    private View mUsernameEdit;
    private View mSexEdit;
    private View mAgeEdit;

    private ProgressBar mLoading;

    private EditUserPresenter mEditUserPresenter;
    private UserBean currentUser;

    private View mUpdatePwdBtn;

    private FragmentManager fragmentManager;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);

        initView(savedInstanceState);
    }

    protected void initView(Bundle savedInstanceState) {
        mTvTopBarText = (TextView) findViewById(R.id.tv_top_bar_text);
        mTvTopBarText.setText(R.string.user_info);

        Click click = new Click();
        mIvTopBarLeftBtn = (ImageView) findViewById(R.id.top_bar_left_button);
        mIvTopBarLeftBtn.setImageResource(R.mipmap.back);
        mIvTopBarLeftBtn.setOnClickListener(click);
        currentUser = BmobUser.getCurrentUser(EditUserInfoActivity.this, UserBean.class);
        if (currentUser != null) {
            username = currentUser.getUsername();
            sex = currentUser.getSexStr();
            age = currentUser.getAge();
            mobileNo = currentUser.getMobilePhoneNumber();

        }

        mMobileNoTv = (TextView) findViewById(R.id.tv_mobile_no);
        mSexTv = (TextView) findViewById(R.id.tv_sex);
        mAgeTv = (TextView) findViewById(R.id.tv_age);
        mUsernameTv = (TextView) findViewById(R.id.tv_username);

        updateView();

        mUsernameEdit = findViewById(R.id.update_username);
        mUsernameEdit.setOnClickListener(click);
        mSexEdit = findViewById(R.id.update_sex);
        mSexEdit.setOnClickListener(click);
        mAgeEdit = findViewById(R.id.update_age);
        mAgeEdit.setOnClickListener(click);

        mLoading = (ProgressBar) findViewById(R.id.pb_loading);

        mEditUserPresenter = new EditUserPresenter(this, editUserHanler);

        mUpdatePwdBtn = findViewById(R.id.update_pwd);
        mUpdatePwdBtn.setOnClickListener(click);
    }

    private void updateView() {
        if (currentUser != null) {
            username = currentUser.getUsername();
            sex = currentUser.getSexStr();
            age = currentUser.getAge();
            mobileNo = currentUser.getMobilePhoneNumber();

        }

        mMobileNoTv.setText(mobileNo);
        mSexTv.setText(sex);
        mAgeTv.setText(String.valueOf(age));
        mUsernameTv.setText(username);
    }

    private class Click implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.top_bar_left_button:
                    finish();
                    break;
                case R.id.update_username:
                    editUserName(v);
                    break;
                case R.id.update_sex:
                    editSex(v);
                    break;
                case R.id.update_age:
                    editAge(v);
                    break;
                case R.id.update_pwd:
                    updatePwd(v);
                    break;
            }
        }
    }

    public void updatePwd(View view) {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }

        fragment = fragmentManager.findFragmentByTag(UpdatePwdFragment.TAG);
        if (fragment == null) {
            fragment = new UpdatePwdFragment();
        }
        FragmentUtil.addContent(R.id.update_pwd_container, fragment, fragmentManager, UpdatePwdFragment.TAG);
    }

    public void editUserName(View view) {
        final EditText temp = new EditText(this);
        temp.setText(username);
        InputFilter[] filters = {new InputFilter.LengthFilter(10)};
        temp.setFilters(filters);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("修改用户名");
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setView(temp);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (temp.getText().toString().length() > 1 && !temp.getText().toString().trim().equals(username)) {
                    currentUser.setUsername(temp.getText().toString());
                    mLoading.setVisibility(View.VISIBLE);
                    mEditUserPresenter.updateUser(EditUserInfoActivity.this, currentUser);
                }
            }
        }).setNegativeButton("取消", null).show();
    }

    public void editSex(View view) {
        final ChoiceOnClickListener choiceListener = new ChoiceOnClickListener();
        final int choice = (currentUser.isSex()) ? 0 : 1;
        final String[] sex = {"男", "女"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("修改性别");
        builder.setSingleChoiceItems(sex, choice, choiceListener);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int choiceWhich = choiceListener.getWhich();
                boolean sexBoole = choiceWhich == 0 ? true : false;
                if (sexBoole != currentUser.isSex()) {
                    UserBean user = new UserBean();
                    user.setObjectId(currentUser.getObjectId());
                    user.setSex(sexBoole);
                    user.setAge(currentUser.getAge());
                    user.setShop(currentUser.isShop());
                    currentUser.setSex(sexBoole);
                    mLoading.setVisibility(View.VISIBLE);
                    mEditUserPresenter.updateUser(EditUserInfoActivity.this, user);
                }
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private class ChoiceOnClickListener implements DialogInterface.OnClickListener {

        private int which = 0;

        @Override
        public void onClick(DialogInterface dialogInterface, int which) {
            this.which = which;
        }

        public int getWhich() {
            return which;
        }
    }

    public void editAge(View view) {
        final EditText temp = new EditText(this);
        temp.setInputType(InputType.TYPE_CLASS_NUMBER);
        temp.setText(String.valueOf(currentUser.getAge()));
        InputFilter[] filters = {new InputFilter.LengthFilter(3)};
        temp.setFilters(filters);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("修改年龄");
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setView(temp);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String txt = temp.getText().toString().trim();
                if (TextUtils.isEmpty(txt)) {
                    return;
                }
                int age = Integer.valueOf(txt);
                if (age > 0 && age < 150 && age != currentUser.getAge()) {
                    UserBean user = new UserBean();
                    user.setObjectId(currentUser.getObjectId());
                    user.setAge(Integer.valueOf(temp.getText().toString()));
                    user.setSex(currentUser.isSex());
                    user.setShop(currentUser.isShop());
                    currentUser.setAge(Integer.valueOf(temp.getText().toString()));
                    mLoading.setVisibility(View.VISIBLE);
                    mEditUserPresenter.updateUser(EditUserInfoActivity.this, user);
                } else {
                    Toast.makeText(EditUserInfoActivity.this, "输入不合法", Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("取消", null).show();
    }

    CallBackHandler editUserHanler = new CallBackHandler() {
        public void handleSuccessMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    mLoading.setVisibility(View.INVISIBLE);
                    updateView();
                    Toast.makeText(EditUserInfoActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        public void handleFailureMessage(String msg) {
            mLoading.setVisibility(View.INVISIBLE);
            Toast.makeText(EditUserInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    };

}
