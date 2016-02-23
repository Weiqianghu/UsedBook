package com.weiqianghu.usedbook.view.fragment;


import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.AddressBean;
import com.weiqianghu.usedbook.model.entity.UserBean;
import com.weiqianghu.usedbook.presenter.QueryAddressPresenter;
import com.weiqianghu.usedbook.presenter.adapter.CommonAdapter;
import com.weiqianghu.usedbook.util.CallBackHandler;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.util.FragmentUtil;
import com.weiqianghu.usedbook.view.ViewHolder;
import com.weiqianghu.usedbook.view.common.BaseFragment;
import com.weiqianghu.usedbook.view.view.IQueryView;

import org.w3c.dom.Comment;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;


public class AddressListFragment extends BaseFragment implements IQueryView{

    public static final String TAG=AddressListFragment.class.getSimpleName();

    private TextView mTvTopBarText;
    private ImageView mIvTopBarLeftBtn;
    private Button mAddNewAddressBtn;

    private FragmentManager mFragmentManager;
    private Fragment mProvinceAddressFragment;

    private QueryAddressPresenter mQueryAddressPresenter;

    private ListView mAddressLv;
    private CommonAdapter<AddressBean> mAdapter;
    private List<AddressBean> addressList;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_address_list;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvTopBarText= (TextView) mRootView.findViewById(R.id.tv_top_bar_text);
        mTvTopBarText.setText(R.string.add_new_address);

        Click click=new Click();

        mIvTopBarLeftBtn= (ImageView) mRootView.findViewById(R.id.top_bar_left_button);
        mIvTopBarLeftBtn.setImageResource(R.mipmap.back);
        mIvTopBarLeftBtn.setOnClickListener(click);

        mAddNewAddressBtn= (Button) mRootView.findViewById(R.id.btn_add_new_address);
        mAddNewAddressBtn.setOnClickListener(click);

        mQueryAddressPresenter=new QueryAddressPresenter(this, queryAddressHandler);
        mAddressLv= (ListView) mRootView.findViewById(R.id.tv_address);
    }

    @Override
    public void onResume() {
        super.onResume();
        mQueryAddressPresenter.query(getActivity());
    }

    CallBackHandler queryAddressHandler =new CallBackHandler(){
        public  void handleSuccessMessage(Message msg){
            switch (msg.what) {
                case Constant.SUCCESS:
                    //mLoading.setVisibility(View.INVISIBLE);
                    Bundle bundle=msg.getData();
                    addressList=bundle.getParcelableArrayList(Constant.PARCEABLE);
                    initAdapter();
                    mAddressLv.setAdapter(mAdapter);
                    break;
            }
        }

        public void handleFailureMessage(String msg){
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
    };

    void initAdapter(){
        mAdapter=new CommonAdapter<AddressBean>(getActivity(),addressList,R.layout.item_address){

            @Override
            public void convert(ViewHolder helper, AddressBean item) {
                StringBuffer address=new StringBuffer();
                address.append(item.getProvince());
                address.append(item.getCity());
                address.append(item.getCounty());
                address.append(item.getDetailAddress());

                helper.setText(R.id.tv_name,item.getName());
                helper.setText(R.id.tv_mobile_no,item.getMobileNo());
                helper.setText(R.id.tv_address,address.toString());
            }
        };
    }

    private class Click implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case  R.id.top_bar_left_button:
                    getActivity().onBackPressed();
                    break;
                case R.id.btn_add_new_address:
                    addNewAddress();
                    break;
            }
        }
    }

    private void addNewAddress() {
        if(mFragmentManager==null){
            mFragmentManager=getActivity().getSupportFragmentManager();
        }
        mProvinceAddressFragment=mFragmentManager.findFragmentByTag(ProvinceAddressFragment.TAG);
        if(mProvinceAddressFragment==null){
            mProvinceAddressFragment=new ProvinceAddressFragment();
        }

        Fragment from=mFragmentManager.findFragmentByTag(AddressListFragment.TAG);
        FragmentUtil.switchContentAddToBackStack(from,mProvinceAddressFragment,R.id.address_container,mFragmentManager,ProvinceAddressFragment.TAG);
    }

}
