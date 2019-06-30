package com.jiaoerzhang.platform.lib_net.network;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.blankj.utilcode.util.Utils;
import com.github.ybq.android.spinkit.SpinKitView;
import com.jiaoerzhang.platform.lib_net.NetConstants;
import com.jiaoerzhang.platform.lib_net.R;
import com.jiaoerzhang.platform.lib_net.network.rx.RxManager;
import com.jiaoerzhang.platform.lib_net.network.rx.exception.ApiException;

import java.lang.ref.SoftReference;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 */
public abstract class BaseObserver<T> implements Observer<T> {


    /* 软引用回调接口*/
    private SpinKitView spinKitView;
//    protected RxManager rxManager;

    //    public BaseObserver(RxManager rxManager) {
//        this.rxManager = rxManager;
//    }
    public BaseObserver() {
        initProgressDialog();
    }

    private void initProgressDialog() {

        View view = View.inflate(Utils.getApp(), R.layout.spinkit, null);
        spinKitView = view.findViewById(R.id.spin_kit);

    }

    /**
     * 显示加载框
     */
    private void showProgressDialog() {
        spinKitView.setVisibility(View.VISIBLE);
    }


    /**
     * 隐藏
     */
    private void dismissProgressDialog() {
        spinKitView.setVisibility(View.VISIBLE);
    }


    @Override
    public void onSubscribe(Disposable d) {
//        rxManager.add(d);
        showProgressDialog();
    }

    @Override
    public void onComplete() {

        dismissProgressDialog();

    }

    @Override
    public void onError(Throwable e) {
        dismissProgressDialog();
        //统一处理错误
        String msg = ApiException.handlerException(e).getMsg();
        int errorCode = ApiException.handlerException(e).getErrorCode();
        if (msg.length() > 64) {
            msg = msg.substring(0, 64);
        }

        if (errorCode == NetConstants.EXPIRED_TOKEN) {
            //跳转至登录页面
           /* Intent intent = new Intent(NetApp.getAppContext(), LoginActivity.class);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
            NetApp.getAppContext().startActivity(intent);*/
        }
        onError(errorCode, msg);
    }

    /**
     * 返回错误字符串
     *
     * @param msg
     */
    public abstract void onError(int errorCode, String msg);

    @Override
    public abstract void onNext(T t);

}
