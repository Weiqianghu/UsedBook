package com.weiqianghu.usedbook.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.CityModel;
import com.weiqianghu.usedbook.model.entity.ProvinceModel;
import com.weiqianghu.usedbook.presenter.adapter.CommonAdapter;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.util.FragmentUtil;
import com.weiqianghu.usedbook.view.ViewHolder;
import com.weiqianghu.usedbook.view.common.BaseFragment;

import java.util.List;


public class CityAddressFragment extends BaseFragment {

    public static final String TAG=CityAddressFragment.class.getSimpleName();

    private TextView mTvTopBarText;
    private ImageView mIvTopBarLeftBtn;

    private ProvinceModel province;
    private String provinceName;
    private List<CityModel> citys;

    private TextView mProvinceTv;
    private ListView mCityLv;

    private FragmentManager mFragmentManager;
    private Fragment mCountyAddressFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_city_address;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView(savedInstanceState);
        initdata();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvTopBarText= (TextView) mRootView.findViewById(R.id.tv_top_bar_text);
        mTvTopBarText.setText(R.string.add_new_address);

        mIvTopBarLeftBtn= (ImageView) mRootView.findViewById(R.id.top_bar_left_button);
        mIvTopBarLeftBtn.setImageResource(R.mipmap.back);
        mIvTopBarLeftBtn.setOnClickListener(new Click());

        mProvinceTv= (TextView) mRootView.findViewById(R.id.tv_province);
        mCityLv= (ListView) mRootView.findViewById(R.id.lv_city);
        mCityLv.setOnItemClickListener(itemClick);

    }

    void initdata(){
        Bundle bundle=getArguments();
        province= (ProvinceModel) bundle.getSerializable(Constant.PROVINCE);
        provinceName=province.getName();
        citys=province.getCity();

        mProvinceTv.setText(provinceName);
        mCityLv.setAdapter(new CommonAdapter<CityModel>(getActivity(),citys,R.layout.item_address_name) {
            @Override
            public void convert(ViewHolder helper, CityModel item) {
                helper.setText(R.id.tv_name,item.getName());
            }
        });
    }

    private class Click implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case  R.id.top_bar_left_button:
                    getActivity().onBackPressed();
                    break;
            }
        }
    }

    AdapterView.OnItemClickListener itemClick=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectCounty(position);
        }
    };

    private void selectCounty(int position) {
        if(mFragmentManager==null){
            mFragmentManager=getActivity().getSupportFragmentManager();
        }
        mCountyAddressFragment =mFragmentManager.findFragmentByTag(CountyAddressFragment.TAG);
        if(mCountyAddressFragment ==null){
            mCountyAddressFragment =new CountyAddressFragment();
        }

        Bundle bundle=new Bundle();
        bundle.putSerializable(Constant.CITY,citys.get(position));
        bundle.putString(Constant.PROVINCE,provinceName);
        mCountyAddressFragment.setArguments(bundle);

        Fragment from=mFragmentManager.findFragmentByTag(CityAddressFragment.TAG);
        FragmentUtil.switchContentAddToBackStack(from, mCountyAddressFragment,R.id.address_container,mFragmentManager,CountyAddressFragment.TAG);
    }

}
