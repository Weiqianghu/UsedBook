package com.weiqianghu.usedbook.view.fragment;


import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.AddressBean;
import com.weiqianghu.usedbook.model.entity.CityModel;
import com.weiqianghu.usedbook.model.entity.UserBean;
import com.weiqianghu.usedbook.model.inf.ISaveModel;
import com.weiqianghu.usedbook.presenter.SavePresenter;
import com.weiqianghu.usedbook.presenter.adapter.CommonAdapter;
import com.weiqianghu.usedbook.util.CallBackHandler;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.util.InputUtil;
import com.weiqianghu.usedbook.view.ISaveView;
import com.weiqianghu.usedbook.view.ViewHolder;
import com.weiqianghu.usedbook.view.common.BaseFragment;
import com.weiqianghu.usedbook.view.customview.ClearEditText;

import cn.bmob.v3.BmobUser;


public class AddressSubmitFragment extends BaseFragment implements ISaveView {

    public static final String TAG = AddressSubmitFragment.class.getSimpleName();

    private TextView mTvTopBarText;
    private ImageView mIvTopBarLeftBtn;
    private String provinceName;
    private String cityName;
    private String countyName;

    private TextView mRegionTv;
    private ClearEditText mDetailAddressEt;
    private ClearEditText mZipCodeEt;
    private ClearEditText mNameEt;
    private ClearEditText mMobileNoEt;
    private Button mSubmitBtn;

    private String detailAddress;
    private String zipCode;
    private String name;
    private String mobileNo;

    private AddressBean address;

    private SavePresenter mSavePresenter;

    private ProgressBar mLoading;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_address_submit;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView(savedInstanceState);
        initdata();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvTopBarText = (TextView) mRootView.findViewById(R.id.tv_top_bar_text);
        mTvTopBarText.setText(R.string.add_new_address);

        mIvTopBarLeftBtn = (ImageView) mRootView.findViewById(R.id.top_bar_left_button);
        mIvTopBarLeftBtn.setImageResource(R.mipmap.back);
        mIvTopBarLeftBtn.setOnClickListener(new Click());

        mRegionTv = (TextView) mRootView.findViewById(R.id.tv_region);
        mDetailAddressEt = (ClearEditText) mRootView.findViewById(R.id.et_detail_address);
        mNameEt = (ClearEditText) mRootView.findViewById(R.id.et_name);
        mZipCodeEt = (ClearEditText) mRootView.findViewById(R.id.et_zip_code);
        mMobileNoEt = (ClearEditText) mRootView.findViewById(R.id.et_mobile_no);
        mSubmitBtn = (Button) mRootView.findViewById(R.id.btn_submit);

        mSubmitBtn.setOnClickListener(new Click());

        mSavePresenter=new SavePresenter(this,addAddressHandler);

        mLoading = (ProgressBar) mRootView.findViewById(R.id.pb_loading);
    }

    CallBackHandler addAddressHandler =new CallBackHandler(){
        public  void handleSuccessMessage(Message msg){
            switch (msg.what) {
                case Constant.SUCCESS:
                    mLoading.setVisibility(View.INVISIBLE);
                    getActivity().onBackPressed();
                    getActivity().onBackPressed();
                    getActivity().onBackPressed();
                    getActivity().onBackPressed();
                    break;
            }
        }

        public void handleFailureMessage(String msg){
            mLoading.setVisibility(View.INVISIBLE);
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            mSubmitBtn.setClickable(true);
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
                    if(beforeSubmit()){
                        mSavePresenter.save(getActivity(),address);
                        mLoading.setVisibility(View.VISIBLE);
                    }else {
                        mSubmitBtn.setClickable(true);
                    }
            }
        }
    }

    void initdata() {
        Bundle bundle = getArguments();
        provinceName = bundle.getString(Constant.PROVINCE);
        cityName = bundle.getString(Constant.CITY);
        countyName = bundle.getString(Constant.COUNTY);

        mRegionTv.setText(provinceName + cityName + countyName);
    }

    boolean beforeSubmit() {

        mSubmitBtn.setClickable(false);

        getdata();

        if(detailAddress==null || "".equals(detailAddress)){
            Toast.makeText(getActivity(),"详细地址不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(zipCode==null || "".equals(zipCode)){
            Toast.makeText(getActivity(),"邮编不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(name==null || "".equals(name)){
            Toast.makeText(getActivity(),"收货人姓名不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mobileNo==null || "".equals(mobileNo)){
            Toast.makeText(getActivity(),"收货人联系电话不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!InputUtil.verifyMobileNo(mobileNo)){
            Toast.makeText(getActivity(),"收货人联系电话可能不正确，请核对后重新输入",Toast.LENGTH_SHORT).show();
            return false;
        }

        address=new AddressBean();
        address.setProvince(provinceName);
        address.setCity(cityName);
        address.setCounty(countyName);
        address.setName(name);
        address.setZipCode(zipCode);
        address.setMobileNo(mobileNo);
        address.setDefault(false);
        address.setDetailAddress(detailAddress);

        UserBean userBean= BmobUser.getCurrentUser(getActivity(),UserBean.class);
        address.setUser(userBean);

        return true;
    }

    void getdata(){
        detailAddress = mDetailAddressEt.getText().toString().trim();
        zipCode = mZipCodeEt.getText().toString().trim();
        name = mNameEt.getText().toString().trim();
        mobileNo = mMobileNoEt.getText().toString().trim();
    }

}
