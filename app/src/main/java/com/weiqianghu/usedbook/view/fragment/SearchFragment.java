package com.weiqianghu.usedbook.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.util.FragmentUtil;
import com.weiqianghu.usedbook.view.common.BaseFragment;
import com.weiqianghu.usedbook.view.customview.ClearEditText;
import com.weiqianghu.usedbook.util.ArrayUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class SearchFragment extends BaseFragment {

    public static final String TAG = SearchFragment.class.getSimpleName();

    private Button mSearchBtn;
    private Button mClearSearchHistoryBtn;
    private ClearEditText mSearchEt;
    private ListView mSearchHistoryLv;

    private List<String> data;

    private ArrayAdapter mAdapter;
    private FragmentManager mFragmentManager;

    public static SearchFragment getInstance() {
        SearchFragment fragment = new SearchFragment();

        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView(savedInstanceState);
    }

    protected void initView(Bundle savedInstanceState) {
        mSearchBtn = (Button) mRootView.findViewById(R.id.btn_search);
        mClearSearchHistoryBtn = (Button) mRootView.findViewById(R.id.btn_clear_search_history);
        mSearchEt = (ClearEditText) mRootView.findViewById(R.id.et_search);
        mSearchHistoryLv = (ListView) mRootView.findViewById(R.id.lv_search_history);
        if (null != (data = new ArrayUtil().ArrayToList(getHistory().toArray()))) {
            mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_search_history, data);
            mSearchHistoryLv.setAdapter(mAdapter);
        }
        mSearchHistoryLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toSearch(data.get(position));
            }
        });

        mSearchBtn.setOnClickListener(new ClickListener());
        mClearSearchHistoryBtn.setOnClickListener(new ClickListener());
    }

    class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_search:
                    searchButtonClick();
                    break;
                case R.id.btn_clear_search_history:
                    clearSearchHistory();
                    break;
            }
        }
    }

    private void searchButtonClick() {
        String searchKey = mSearchEt.getText().toString().trim();
        if (searchKey == null || "".equals(searchKey)) {
            return;
        }
        mSearchEt.setText("");

        SharedPreferences sp = getActivity().getSharedPreferences("search_history", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        Set<String> searchHistorySet = sp.getStringSet("search_history", null);
        if (searchHistorySet == null) {
            searchHistorySet = new HashSet<>();
        }

        data.add(searchKey);
        mAdapter.notifyDataSetChanged();

        searchHistorySet.add(searchKey);

        editor.clear();
        editor.putStringSet("search_history", searchHistorySet);
        editor.apply();

        toSearch(searchKey);
    }

    private void toSearch(String searchPar) {
        if (mFragmentManager == null) {
            mFragmentManager = getActivity().getSupportFragmentManager();
        }
        Fragment to = mFragmentManager.findFragmentByTag(SearchResultFragment.TAG);
        if (to == null) {
            to = new SearchResultFragment();
        }
        Fragment from = mFragmentManager.findFragmentByTag(SearchFragment.TAG);

        Bundle bundle = new Bundle();
        bundle.putString(Constant.DATA, searchPar);
        to.setArguments(bundle);

        FragmentUtil.switchContentAddToBackStack(from, to, R.id.main_container, mFragmentManager, SearchResultFragment.TAG);
    }

    private Set<String> getHistory() {
        SharedPreferences sp = getActivity().getSharedPreferences("search_history", Context.MODE_PRIVATE);
        Set<String> result = sp.getStringSet("search_history", new HashSet<String>());
        return result;
    }

    private void clearSearchHistory() {
        SharedPreferences sp = getActivity().getSharedPreferences("search_history", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();

        data.clear();
        mAdapter.notifyDataSetChanged();
    }

}
