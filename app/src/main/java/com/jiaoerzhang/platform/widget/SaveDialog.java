package com.jiaoerzhang.platform.widget;import android.app.Dialog;import android.content.Context;import android.os.Bundle;import android.support.v4.content.ContextCompat;import android.view.View;import android.view.WindowManager;import android.widget.RelativeLayout;import android.widget.TextView;import com.blankj.utilcode.util.ScreenUtils;import com.jiaoerzhang.platform.R;public class SaveDialog extends Dialog {    private TextView yesTv;    private TextView noTv;    private Context mContext;    public SaveDialog(Context context) {        super(context);        this.mContext = context;    }    public SaveDialog(Context context, String content) {        super(context, R.style.dialog);        this.mContext = context;    }    public SaveDialog(Context context, int themeResId, String content) {        super(context, themeResId);        this.mContext = context;    }    protected SaveDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {        super(context, cancelable, cancelListener);        this.mContext = context;    }    public SaveDialog setTitle(String title){        return this;    }    public SaveDialog setPositiveButton(String name){        return this;    }    public SaveDialog setNegativeButton(String name){        return this;    }    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.save_dialog_layout);        setCanceledOnTouchOutside(false);        initView();    }    private void initView(){        noTv = findViewById(R.id.NO);        yesTv = findViewById(R.id.YES);        noTv.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                dismiss();                if (onClickListener!=null)                    onClickListener.onNoClick();            }        });        yesTv.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                if (onClickListener!=null)                onClickListener.onYesClick();            }        });        getWindow().setBackgroundDrawableResource(android.R.color.transparent);        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值//        p.height = (int) (ScreenUtils.getScreenHeight() * 0.3);   //高度设置为屏幕的0.3        p.width = (int) (ScreenUtils.getScreenWidth() * 0.8);    //宽度设置为屏幕的0.5        getWindow().setAttributes(p);     //设置生效    }    private OnClickListener onClickListener;    public void setOnClickListener(OnClickListener onClickListener) {        this.onClickListener = onClickListener;    }    public interface OnClickListener{        void onYesClick();        void onNoClick();    }}