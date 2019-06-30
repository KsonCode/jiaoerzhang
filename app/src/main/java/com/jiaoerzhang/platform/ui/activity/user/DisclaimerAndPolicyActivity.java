package com.jiaoerzhang.platform.ui.activity.user;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaoerzhang.platform.R;
import com.jiaoerzhang.platform.api.ApiService;
import com.jiaoerzhang.platform.api.UserApiService;
import com.jiaoerzhang.platform.common.Constants;
import com.jiaoerzhang.platform.lib_core.base.BaseActivity;
import com.jiaoerzhang.platform.widget.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class DisclaimerAndPolicyActivity extends BaseActivity {

    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView titleView;



    @Override
    protected void initData() {


        fillData();
    }

    @Override
    protected void initView() {
        //自适应
//        webView.getSettings().setUseWideViewPort(true);
//        webView.getSettings().setLoadWithOverviewMode(true);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisclaimerAndPolicyActivity.this.finish();
            }
        });
    }

    @Override
    protected int bindLayoutId() {
        return R.layout.activity_disclaimer;
    }



    /**
     * 数据
     */
    private void fillData() {
        if (getIntent().getExtras().getString("from").equals("mz")){
            webView.loadUrl(Constants.TERMS);
        }else{
            webView.loadUrl(Constants.POLICY);
        }

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);

              titleView.setText(title);
            }
        });


    }
}
