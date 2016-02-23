package com.weiqianghu.usedbook.view.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.ProvinceModel;
import com.weiqianghu.usedbook.presenter.adapter.CommonAdapter;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.util.FileUtil;
import com.weiqianghu.usedbook.util.FragmentUtil;
import com.weiqianghu.usedbook.util.ThreadPool;
import com.weiqianghu.usedbook.view.ViewHolder;
import com.weiqianghu.usedbook.view.common.BaseFragment;

import java.lang.reflect.Type;
import java.util.List;


public class ProvinceAddressFragment extends BaseFragment {

    public static final String TAG=ProvinceAddressFragment.class.getSimpleName();

    private TextView mTvTopBarText;
    private ImageView mIvTopBarLeftBtn;

    private List<ProvinceModel> provinceModelList=null;

    private ListView mProvinceLv;
    private CommonAdapter<ProvinceModel> mAdapter;

    private FragmentManager mFragmentManager;
    private Fragment mCityAddressFragment;

    private ProgressBar mLoading;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_province_address;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvTopBarText= (TextView) mRootView.findViewById(R.id.tv_top_bar_text);
        mTvTopBarText.setText(R.string.add_new_address);

        mIvTopBarLeftBtn= (ImageView) mRootView.findViewById(R.id.top_bar_left_button);
        mIvTopBarLeftBtn.setImageResource(R.mipmap.back);
        mIvTopBarLeftBtn.setOnClickListener(new Click());

        new GetDataAsyncTask().executeOnExecutor(ThreadPool.getThreadPool());

        mProvinceLv= (ListView) mRootView.findViewById(R.id.lv_province);
        mProvinceLv.setOnItemClickListener(itemClick);

        mLoading = (ProgressBar) mRootView.findViewById(R.id.pb_loading);
        mLoading.setVisibility(View.VISIBLE);
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

    class GetDataAsyncTask extends AsyncTask<Void,Void,List<ProvinceModel>>{


        @Override
        protected List<ProvinceModel> doInBackground(Void... params) {
            return getProvincesByJSON();
        }

        @Override
        protected void onPostExecute(List<ProvinceModel> provinceModels) {
            mLoading.setVisibility(View.GONE);
            provinceModelList=provinceModels;
            super.onPostExecute(provinceModels);
            mProvinceLv.setAdapter(mAdapter=new CommonAdapter<ProvinceModel>(getActivity(),provinceModels,R.layout.item_address_name) {
                @Override
                public void convert(ViewHolder helper, ProvinceModel item) {
                    helper.setText(R.id.tv_name,item.getName());
                }
            });
        }
    }

    AdapterView.OnItemClickListener itemClick=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectCity(position);
        }
    };

    private void selectCity(int position) {
        if(mFragmentManager==null){
            mFragmentManager=getActivity().getSupportFragmentManager();
        }
        mCityAddressFragment=mFragmentManager.findFragmentByTag(CityAddressFragment.TAG);
        if(mCityAddressFragment==null){
            mCityAddressFragment=new CityAddressFragment();
        }

        Bundle bundle=new Bundle();
        bundle.putSerializable(Constant.PROVINCE,provinceModelList.get(position));
        mCityAddressFragment.setArguments(bundle);

        Fragment from=mFragmentManager.findFragmentByTag(ProvinceAddressFragment.TAG);
        FragmentUtil.switchContentAddToBackStack(from,mCityAddressFragment,R.id.address_container,mFragmentManager,CityAddressFragment.TAG);
    }

    private List<ProvinceModel> getProvincesByJSON(){
        List<ProvinceModel> provinceModelList;
        ProvinceModel province=new ProvinceModel();
        Gson g=new Gson();
        Type lt=new TypeToken<List<ProvinceModel>>(){}.getType();

        String json= FileUtil.getStrFromRaw(getResources().openRawResource(R.raw.address));
        provinceModelList=g.fromJson(json,lt);
        return provinceModelList;
    }

}
