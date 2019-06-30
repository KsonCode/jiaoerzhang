package com.jiaoerzhang.platform.ui.activity.user;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.jiaoerzhang.platform.R;
import com.jiaoerzhang.platform.lib_core.base.BaseActivity;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

public class ContactActivity extends BaseActivity {

    RxPermissions rxPermissions = new RxPermissions(this); // where this is an Activity or Fragment instance

    @BindView(R.id.tel1)
    TextView tel1;
    @BindView(R.id.tel2)
    TextView tel2;
    @BindView(R.id.tel3)
    TextView tel3;
    @BindView(R.id.back)
    ImageView back;


    @Override
    protected void initData() {

    }

    protected void initView() {
        tel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhone(tel1.getText().toString());
            }
        });
        tel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhone(tel2.getText().toString());
            }
        });
        tel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhone(tel3.getText().toString());
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactActivity.this.finish();
            }
        });
    }

    @Override
    protected int bindLayoutId() {
        return R.layout.activity_contact;
    }


    /**
     * 拨打电话（直接拨打电话）
     * @param phoneNum 电话号码
     */
    public void callPhone(final String phoneNum){

        // Must be done during an initialization phase like onCreate
        rxPermissions
                .request(Manifest.permission.CALL_PHONE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {

                        if (aBoolean){
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            Uri data = Uri.parse("tel:" + phoneNum);
                            intent.setData(data);
                            startActivity(intent);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });

    }
}
