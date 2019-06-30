package com.jiaoerzhang.platform.ui.activity.user.settings;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import com.jiaoerzhang.platform.R;
import com.jiaoerzhang.platform.lib_core.base.BaseActivity;
import com.jiaoerzhang.platform.ui.activity.user.DisclaimerActivity;
import com.jiaoerzhang.platform.ui.activity.user.DisclaimerAndPolicyActivity;
import com.jiaoerzhang.platform.ui.activity.user.LoginActivity;
import com.jiaoerzhang.platform.utils.DataCleanManager;

public class SettingActivity extends BaseActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.disclaimer)
    RelativeLayout disclaimerLayout;

    @BindView(R.id.clear_cache)
    RelativeLayout clear_cache;

    @BindView(R.id.cacheSize)
    TextView cacheSize;
    @BindView(R.id.updatePassLayout)
    RelativeLayout updatePassLayout;

    @BindView(R.id.Privacy_protocol)
    RelativeLayout Privacy_protocol;

    @Override
    protected void initData() {

    }



    @Override
    protected void initView() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingActivity.this.finish();
            }
        });


        disclaimerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, DisclaimerAndPolicyActivity.class);
                intent.putExtra("from","mz");
                startActivity(intent);
            }
        });

        Privacy_protocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,DisclaimerAndPolicyActivity.class);
                intent.putExtra("from","ys");
                startActivity(intent);
            }
        });




        clear_cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //清除缓存
                DataCleanManager.cleanAllCache(SettingActivity.this);
                Toast.makeText(SettingActivity.this, "clear success", Toast.LENGTH_SHORT).show();
                cacheSize.setText(DataCleanManager.getTotalCacheSize(SettingActivity.this));


            }
        });

        try {
            cacheSize.setText(DataCleanManager.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }

        updatePassLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this,ChangePWDActivity.class));
            }
        });


    }

    @Override
    protected int bindLayoutId() {
        return R.layout.activity_setting;
    }

    /**
     * 退出登录
     * @param view
     */
    public void signOut(View view) {
        SPUtils.getInstance().put("username",SPUtils.getInstance().getString("username"));
        SPUtils.getInstance().put("loginToken","");
        SPUtils.getInstance().put("card","");
        SPUtils.getInstance().put("year","");
        SPUtils.getInstance().put("month","");
        startActivity(new Intent(this, LoginActivity.class));
        ActivityUtils.finishAllActivities();//结束所有activity
    }
}
