package com.jiaoerzhang.platform.widget;import android.app.AlertDialog;import android.content.Context;import android.os.Bundle;import com.wang.avi.AVLoadingIndicatorView;import com.jiaoerzhang.platform.R;public class LoadingDialog extends AlertDialog {    private  LoadingDialog loadingDialog;    private AVLoadingIndicatorView avi;    public  LoadingDialog(Context context) {        this(context, R.style.TransparentDialog); //设置AlertDialog背景透明    }    public LoadingDialog(Context context, int themeResId) {        super(context,themeResId);    }    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        this.setContentView(R.layout.dialog_loading);        setCancelable(false);        setCanceledOnTouchOutside(false);    }    @Override    public void show() {        super.show();        avi = (AVLoadingIndicatorView)this.findViewById(R.id.avi);        avi.show();    }    @Override    public void dismiss() {        super.dismiss();        avi = (AVLoadingIndicatorView)this.findViewById(R.id.avi);        avi.hide();    }}