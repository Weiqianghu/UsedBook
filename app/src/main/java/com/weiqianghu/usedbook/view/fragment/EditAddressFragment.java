package com.weiqianghu.usedbook.view.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.AddressBean;
import com.weiqianghu.usedbook.presenter.DeletePresenter;
import com.weiqianghu.usedbook.presenter.UpdateBatchPresenter;
import com.weiqianghu.usedbook.presenter.UpdatePresenter;
import com.weiqianghu.usedbook.util.CallBackHandler;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.view.common.BaseFragment;
import com.weiqianghu.usedbook.view.customview.ClearEditText;
import com.weiqianghu.usedbook.view.view.IDeleteView;
import com.weiqianghu.usedbook.view.view.IUpdateBatchView;
import com.weiqianghu.usedbook.view.view.IUpdateView;

import java.util.List;

public class EditAddressFragment extends BaseFragment implements IUpdateBatchView, IUpdateView, IDeleteView {
    public static final String TAG = EditAddressFragment.class.getSimpleName();

    private TextView mTvTopBarText;
    private ImageView mIvTopBarLeftBtn;

    private TextView mRegionTv;
    private ClearEditText mDetailAddressEt;
    private ClearEditText mZipCodeEt;
    private ClearEditText mNameEt;
    private ClearEditText mMobileNoEt;
    private Button mSubmitBtn;
    private CheckBox mIsDefaultCb;
    private ImageButton mDeleteIb;

    private ProgressBar mLoading;
    private AddressBean address;
    private String detailAddress;
    private String zipCode;
    private String name;
    private String mobileNo;
    private boolean isDefault;

    private List<AddressBean> addressList;

    private UpdateBatchPresenter mUpdateBatchPresenter;
    private UpdatePresenter<AddressBean> mUpdatePresenter;
    private DeletePresenter<AddressBean> mDeletePresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_edit_address;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView(savedInstanceState);

        initdata();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvTopBarText = (TextView) mRootView.findViewById(R.id.tv_top_bar_text);
        mTvTopBarText.setText(R.string.edit_address);

        Click click = new Click();

        mIvTopBarLeftBtn = (ImageView) mRootView.findViewById(R.id.top_bar_left_button);
        mIvTopBarLeftBtn.setImageResource(R.mipmap.back);
        mIvTopBarLeftBtn.setOnClickListener(click);

        mRegionTv = (TextView) mRootView.findViewById(R.id.tv_region);
        mDetailAddressEt = (ClearEditText) mRootView.findViewById(R.id.et_detail_address);
        mNameEt = (ClearEditText) mRootView.findViewById(R.id.et_name);
        mZipCodeEt = (ClearEditText) mRootView.findViewById(R.id.et_zip_code);
        mMobileNoEt = (ClearEditText) mRootView.findViewById(R.id.et_mobile_no);
        mSubmitBtn = (Button) mRootView.findViewById(R.id.btn_submit);
        mSubmitBtn.setOnClickListener(click);
        mIsDefaultCb = (CheckBox) mRootView.findViewById(R.id.cb_is_default);
        mDeleteIb = (ImageButton) mRootView.findViewById(R.id.ib_delete);
        mDeleteIb.setOnClickListener(click);

        mLoading = (ProgressBar) mRootView.findViewById(R.id.pb_loading);

        mUpdateBatchPresenter = new UpdateBatchPresenter(this, resetAddressHanler);
        mUpdatePresenter = new UpdatePresenter<>(this, editAddressHanler);
        mDeletePresenter = new DeletePresenter<>(this, deleteAddressHanler);
    }

    CallBackHandler resetAddressHanler = new CallBackHandler() {
        public void handleSuccessMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    setAddressData();
                    mUpdatePresenter.update(getActivity(), address, address.getObjectId());
                    break;
            }
        }

        public void handleFailureMessage(String msg) {
            mLoading.setVisibility(View.INVISIBLE);
            mSubmitBtn.setClickable(true);
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
    };

    CallBackHandler deleteAddressHanler = new CallBackHandler() {
        public void handleSuccessMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    getActivity().onBackPressed();
                    Toast.makeText(getActivity(), "删除收货地址成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        public void handleFailureMessage(String msg) {
            mLoading.setVisibility(View.INVISIBLE);
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
    };

    CallBackHandler editAddressHanler = new CallBackHandler() {
        public void handleSuccessMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    getActivity().onBackPressed();
                    mLoading.setVisibility(View.INVISIBLE);
                    mSubmitBtn.setClickable(true);
                    Toast.makeText(getActivity(), "收货地址更新成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        public void handleFailureMessage(String msg) {
            mLoading.setVisibility(View.INVISIBLE);
            mSubmitBtn.setClickable(true);
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
                case R.id.btn_submit:
                    editAddress();
                    break;
                case R.id.ib_delete:
                    deleteAddress();
                    break;

            }
        }
    }

    void initdata() {
        Bundle bundle = getArguments();
        address = bundle.getParcelable(Constant.ADDRESS);

        addressList = bundle.getParcelableArrayList(Constant.PARCEABLE);

        if (address != null) {
            mRegionTv.setText(address.getProvince() + address.getCity() + address.getCounty());
            mDetailAddressEt.setText(address.getDetailAddress());
            mZipCodeEt.setText(address.getZipCode());
            mNameEt.setText(address.getName());
            mMobileNoEt.setText(address.getMobileNo());
            mIsDefaultCb.setChecked(address.isDefault());
        }
    }

    void editAddress() {
        if (!beforeSubmit()) {
            return;
        } else if (!isDataChanged()) {
            getActivity().onBackPressed();
        } else {
            if (isNeedResetAddress()) {
                setAddressData();
                resetAddressIsDefault();
                mUpdateBatchPresenter.resetAddress(getActivity(), addressList);
            } else {
                setAddressData();
                mUpdatePresenter.update(getActivity(), address, address.getObjectId());
            }
        }
    }

    boolean isDataChanged() {
        if (!detailAddress.equals(address.getDetailAddress()) || !zipCode.equals(address.getZipCode())
                || !name.equals(address.getName()) || !mobileNo.equals(address.getMobileNo())
                || isDefault != address.isDefault()) {
            return true;
        }
        return false;
    }

    boolean isNeedResetAddress() {
        if (isDefault != address.isDefault()) {
            return true;
        }
        return false;
    }

    void setAddressData() {
        address.setDetailAddress(detailAddress);
        address.setZipCode(zipCode);
        address.setName(name);
        address.setMobileNo(mobileNo);
        address.setDefault(isDefault);
    }

    boolean beforeSubmit() {
        getNewestData();
        if (detailAddress == null || "".equals(detailAddress)) {
            Toast.makeText(getActivity(), "详细地址不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (zipCode == null || "".equals(zipCode)) {
            Toast.makeText(getActivity(), "邮编不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (name == null || "".equals(name)) {
            Toast.makeText(getActivity(), "收货人姓名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mobileNo == null || "".equals(mobileNo)) {
            Toast.makeText(getActivity(), "收货人联系电话不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        isDefault = mIsDefaultCb.isChecked();
        return true;
    }

    void getNewestData() {
        detailAddress = mDetailAddressEt.getText().toString().trim();
        zipCode = mZipCodeEt.getText().toString().trim();
        name = mNameEt.getText().toString().trim();
        mobileNo = mMobileNoEt.getText().toString().trim();
    }

    void deleteAddress() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dialog);
        builder.setTitle("确定删除这条记录");
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setPositiveButton(R.string.positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                address.setDelete(true);
                mDeletePresenter.delete(getActivity(), address, address.getObjectId());
                mLoading.setVisibility(View.VISIBLE);
            }
        });
        builder.setNegativeButton(R.string.negative, null);
        builder.show();
    }

    void resetAddressIsDefault() {
        if (addressList == null) {
            return;
        }
        for (int i = 0, length = addressList.size(); i < length; i++) {
            addressList.get(i).setDefault(false);
        }
    }
}
