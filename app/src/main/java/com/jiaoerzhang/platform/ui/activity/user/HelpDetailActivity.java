package com.jiaoerzhang.platform.ui.activity.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import butterknife.BindView;
import com.jiaoerzhang.platform.R;
import com.jiaoerzhang.platform.lib_core.base.BaseActivity;

public class HelpDetailActivity extends BaseActivity {


    @BindView(R.id.back)
    ImageView back;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpDetailActivity.this.finish();
            }
        });

    }

    @Override
    protected int bindLayoutId() {
        return R.layout.activity_help_detail;
    }
}
