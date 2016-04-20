package com.weiqianghu.usedbook.presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;

import com.weiqianghu.usedbook.model.entity.RecommendBean;
import com.weiqianghu.usedbook.model.entity.UserBean;
import com.weiqianghu.usedbook.model.impl.QueryModel;
import com.weiqianghu.usedbook.model.inf.IQueryModel;
import com.weiqianghu.usedbook.util.Constant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by weiqianghu on 2016/4/17.
 */
public class QueryRecommendPresenter extends CommonPresenter {
    private IQueryModel<RecommendBean> mQueryModel;

    public QueryRecommendPresenter(Handler handler) {
        super(handler);
        mQueryModel = new QueryModel<RecommendBean>();
    }

    public void queryRecommends(Context context, UserBean user, int start, int step) {
        FindListener<RecommendBean> findListener = new FindListener<RecommendBean>() {
            @Override
            public void onSuccess(List list) {
                Bundle bundle = new Bundle();

                Set<RecommendBean> recommends = new HashSet<>(list);

                List<RecommendBean> recommendBeanList = new ArrayList<>(recommends);

                bundle.putParcelableArrayList(Constant.LIST, (ArrayList<? extends Parcelable>) recommendBeanList);
                handleSuccess(bundle);
            }

            @Override
            public void onError(int i, String s) {
                handleFailureMessage(i, s);
            }
        };

        BmobQuery<RecommendBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user", user);
        query.addWhereEqualTo("isRead", false);
        query.include("book");
        query.setLimit(step);
        query.setSkip(start);
        query.order("-createdAt");
        mQueryModel.query(context, query, findListener);
    }
}
