package com.weiqianghu.usedbook.view.customview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by weiqianghu on 2016/3/21.
 */
public class EmptyViewHelper {
    private ListView mListView;
    private EmptyRecyclerView mEmptyRecyclerView;
    private View mEmptyView;
    private Context mContext;

    public EmptyViewHelper(ListView listView, int emptyViewRes) {
        mListView = listView;
        mContext = listView.getContext();
        initEmptyView(emptyViewRes, mListView);
    }

    public EmptyViewHelper(EmptyRecyclerView emptyRecyclerView, int emptyViewRes) {
        mEmptyRecyclerView = emptyRecyclerView;
        mContext = emptyRecyclerView.getContext();
        initEmptyView(emptyViewRes, mEmptyRecyclerView);
    }

    private void initEmptyView(int emptyViewRes, ListView listView) {
        mEmptyView = View.inflate(mContext, emptyViewRes, null);
        ((ViewGroup) mListView.getParent()).addView(mEmptyView);
        mListView.setEmptyView(mEmptyView);
    }

    private void initEmptyView(int emptyViewRes, EmptyRecyclerView emptyRecyclerView) {
        mEmptyView = View.inflate(mContext, emptyViewRes, null);
        ((ViewGroup) mListView.getParent()).addView(mEmptyView);
        mListView.setEmptyView(mEmptyView);
    }
}
