package com.weiqianghu.usedbook.presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;

import com.weiqianghu.usedbook.model.entity.AddressBean;
import com.weiqianghu.usedbook.model.entity.FailureMessageModel;
import com.weiqianghu.usedbook.model.entity.UserBean;
import com.weiqianghu.usedbook.model.impl.QueryModel;
import com.weiqianghu.usedbook.model.inf.IQueryModel;
import com.weiqianghu.usedbook.util.Constant;
import com.weiqianghu.usedbook.view.view.IQueryView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by weiqianghu on 2016/2/23.
 */
public class QueryAddressPresenter {
    private IQueryModel mQueryModel;
    private IQueryView mQueryView;
    private Handler handler;

    public QueryAddressPresenter(IQueryView iQueryView, Handler handler) {
        this.mQueryView = iQueryView;
        this.handler = handler;
        mQueryModel = new QueryModel<AddressBean>();
    }

    public void query(Context context,BmobQuery<AddressBean> query) {
        UserBean userBean= BmobUser.getCurrentUser(context,UserBean.class);
        query.addWhereEqualTo("user",userBean);
        query.order("-isDefault");

        FindListener<AddressBean> findListener = new FindListener<AddressBean>() {
            @Override
            public void onSuccess(List<AddressBean> list) {
                Message message = new Message();
                message.what = Constant.SUCCESS;

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(Constant.PARCEABLE, (ArrayList<? extends Parcelable>) list);

                message.setData(bundle);
                handler.sendMessage(message);
            }

            @Override
            public void onError(int i, String s) {
                Message message = new Message();
                message.what = Constant.FAILURE;

                FailureMessageModel failureMessageModel = new FailureMessageModel();
                failureMessageModel.setMsgCode(i);
                failureMessageModel.setMsg(s);

                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.FAILURE_MESSAGE, failureMessageModel);

                message.setData(bundle);
                handler.sendMessage(message);
            }
        };

        mQueryModel.query(context,query,findListener);
    }
}
