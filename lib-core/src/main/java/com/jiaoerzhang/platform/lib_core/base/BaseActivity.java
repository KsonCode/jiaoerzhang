package com.jiaoerzhang.platform.lib_core.base;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.jiaoerzhang.platform.lib_core.R;
import com.jiaoerzhang.platform.lib_core.utils.AppManager;
import com.jiaoerzhang.platform.lib_core.utils.KeyboardUtil;
import com.jiaoerzhang.platform.lib_core.utils.NetBroadcastReceiver;
import com.jiaoerzhang.platform.lib_core.widget.SweetAlert.SweetAlertDialog;
import com.jiaoerzhang.platform.lib_core.widget.dialog.DialogUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

public abstract class BaseActivity extends AppCompatActivity implements NavigationCallback,  NetBroadcastReceiver.NetChangeListener{
    private static Disposable disposable;
//    public Toolbar toolbar;
    public boolean isBack;
    private boolean isFullScreen = false;//是否全屏
    private boolean isStatus = false;//是否沉浸式状态栏
    private boolean isToolBar = false;//是否显示标题栏
    private Unbinder unbinder = null;

    NetBroadcastReceiver netBroadcastReceiver;
    public static NetBroadcastReceiver.NetChangeListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppManager.getInstance().addActivity(this);
        ImmersionBar.with(this)
                .statusBarDarkFont(true)//透明状态栏和导航栏，不写默认状态栏为透明色，导航栏为黑色（设置此方法，fullScreen()方法自动为true）
                .statusBarColor(R.color.white).init();
        if (ScreenUtils.isPortrait()) {
            ScreenUtils.adaptScreen4VerticalSlide(this, 360);
        } else {
            ScreenUtils.adaptScreen4HorizontalSlide(this, 360);
        }
        super.onCreate(savedInstanceState);
        setContentView(bindLayoutId());
        unbinder = ButterKnife.bind(this);
        initView();
        initData();

        listener=this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //实例化IntentFilter对象
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            netBroadcastReceiver = new NetBroadcastReceiver();
            //注册广播接收
            registerReceiver(netBroadcastReceiver, filter);
        }
//        ToastUtils.setMsgTextSize(30);
//        ToastUtils.setGravity(Gravity.CENTER,0,0);
    }

    protected abstract void initData();

    public void setFullScreen(boolean isFullScreen){

        //全屏代码
        if (isFullScreen){
//            getWindow().setFlags();
        }
    }

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 绑定根布局
     * @return
     */
    protected abstract int bindLayoutId();

    /**
     * 网络变化
     *
     * @param status -1 无网络 0 移动 1 无线
     */
    @Override
    public void onChangeListener(int status) {
//        if (status == -1) {
//            ToastUtils.showShort("无网络");
//        } else if (0 == status) {
//            ToastUtils.showShort("移动网络");
//        } else if (1 == status) {
//            ToastUtils.showShort("无线网络");
//        }
    }


//    public BaseActivity initToolBarAsHome(String title, boolean flag, boolean isBack) {
//        this.isBack = isBack;
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//
//        if (toolbar != null) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && flag) {
//                toolbar.setElevation(9.0f);
//            }
//            setSupportActionBar(toolbar);
//            TextView toolbaTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
//            toolbaTitle.setText(title);
//        }
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(false);
//            actionBar.setDisplayShowTitleEnabled(false);
//        }
//        return this;
//    }



    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        super.onBackPressed();
        if (isBack) {
            backward();
        }
    }

    public void clickBack() {
        backward();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
//        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
//        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
//        ButterKnife.bind(this);
    }

    public void initSavedInstanceState(Bundle savedInstanceState) {
    }


//    public BaseActivity showLeftBack() {
//        TextView tv = ((TextView) toolbar.findViewById(R.id.tv_LeftBack));
//        tv.setVisibility(View.VISIBLE);
//        tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clickBack();
//            }
//        });
//        return this;
//    }

//    public BaseActivity showRightText(String text) {
//        final TextView tv = ((TextView) toolbar.findViewById(R.id.tv_RightText));
//        tv.setVisibility(View.VISIBLE);
//        tv.setText(text);
//        tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clickRightText(tv);
//            }
//        });
//        return this;
//    }

//    public BaseActivity hideRightText(String text) {
//        final TextView tv = ((TextView) toolbar.findViewById(R.id.tv_RightText));
//        tv.setVisibility(View.VISIBLE);
//        tv.setText(text);
//        tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clickRightText(tv);
//            }
//        });
//        return this;
//    }

//    public BaseActivity showRightText(String text, int color) {
//        final TextView tv = ((TextView) toolbar.findViewById(R.id.tv_RightText));
//        tv.setVisibility(View.VISIBLE);
//        tv.setTextColor(getResources().getColor(color));
//        tv.setText(text);
//        tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clickRightText(tv);
//            }
//        });
//        return this;
//    }

//    public BaseActivity showLeftImg1(int img) {
//        ImageView tv = ((ImageView) toolbar.findViewById(R.id.tv_LeftImag1));
//        tv.setVisibility(View.VISIBLE);
//        tv.setImageResource(img);
//        tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clickLeftImg();
//            }
//        });
//        return this;
//    }

//    public BaseActivity showRightImg1(int img) {
//        ImageView tv = ((ImageView) toolbar.findViewById(R.id.tv_RightImg1));
//        tv.setVisibility(View.VISIBLE);
//        tv.setImageResource(img);
//        tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clickRightImg();
//            }
//        });
//        return this;
//    }

//    public BaseActivity showRightImg2(int img) {
//        ImageView tv = ((ImageView) toolbar.findViewById(R.id.tv_RightImg2));
//        tv.setVisibility(View.VISIBLE);
//        tv.setImageResource(img);
//        tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clickRightImg2();
//            }
//        });
//        return this;
//    }

    public void clickRightImg() {
    }

    public void clickRightImg2() {
    }

    public void clickRightText(TextView tv) {
    }

    public void clickLeftImg() {
    }

    /**
     * 跳转到下一个Activity，不销毁当前Activity
     *
     * @param cls 下一个Activity的 Path
     */
    public Postcard forward(String cls) {
        KeyboardUtil.closeKeyboard(this);
        return ARouter.getInstance().build(cls).withTransition(R.anim.bga_sbl_activity_forward_enter, R.anim.bga_sbl_activity_forward_exit);
    }

    /**
     * 回到上一个Activity，并销毁当前Activity
     */
    public void backward() {
        KeyboardUtil.closeKeyboard(this);
        finish();
        executeBackwardAnim();
    }

    /**
     * 执行回到到上一个Activity的动画
     */
    public void executeBackwardAnim() {
        overridePendingTransition(R.anim.bga_sbl_activity_backward_enter, R.anim.bga_sbl_activity_backward_exit);
    }

//    /**
//     * 解决fragment onActivityResult不调用
//     *
//     * @param requestCode
//     * @param resultCode
//     * @param data
//     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        FragmentManager fm = getSupportFragmentManager();
//        //if (index != 0) {
//        if (fm.getFragments() == null) {
//            LogUtils.w("BaseActivity", "Activity result fragment fragmentIndex out of range: 0x"
//                    + Integer.toHexString(requestCode));
//            return;
//        }
//        for (int i = 0; i < fm.getFragments().size(); i++) {
//            Fragment frag = fm.getFragments().get(i);
//            if (frag == null) {
//                Log.w("BaseActivity", "Activity result no fragment exists for fragmentIndex: 0x"
//                        + Integer.toHexString(requestCode));
//            } else {
//                handleResult(frag, requestCode, resultCode, data);
//            }
//        }
//        return;
//        //}
//
//    }

    /**
     * 递归调用，对所有子Fragement生效
     *
     * @param frag
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void handleResult(Fragment frag, int requestCode, int resultCode,
                              Intent data) {
        frag.onActivityResult(requestCode, resultCode, data);
        List<Fragment> frags = frag.getChildFragmentManager().getFragments();
        if (frags != null) {
            for (Fragment f : frags) {
                if (f != null)
                    handleResult(f, requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onFound(Postcard postcard) {

    }

    @Override
    public void onLost(Postcard postcard) {

    }

    @Override
    public void onArrival(Postcard postcard) {

    }

    @Override
    public void onInterrupt(Postcard postcard) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DialogUtils.buildSweet(DialogUtils.WARNING_TYPE, "登录提示", "登录后可以为您提供更好的服务")
                        .setCancelText("再看看")
                        .setConfirmText("去登陆")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        }).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();//释放资源，回收内存，优化性能
            unbinder = null;
        }
        if (netBroadcastReceiver != null) {
            unregisterReceiver(netBroadcastReceiver);
        }
        AppManager.getInstance().killActivity(this);
        ImmersionBar.with(this).destroy();
    }

    /**
     * 无参数跳转
     * @param clz
     */
    public void startActivity(Class<? extends Activity> clz){
        startActivity(new Intent(this,clz));
    }
    /**
     * 有参数跳转
     * @param clz
     */
    public void startActivity(Class<? extends Activity> clz,Bundle bundle){
        Intent intent = new Intent(this,clz);
        intent.putExtras(bundle);
        startActivity(intent);


    }


    /**
     * 显示toast、
     * @param msg
     */
    public void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    /**
     * 显示带时间的toast
     * @param msg
     */
    public void showToast(String msg,int time){
        Toast.makeText(this, msg, time).show();
    }





}
