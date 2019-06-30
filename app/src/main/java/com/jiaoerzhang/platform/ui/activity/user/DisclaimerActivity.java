package com.jiaoerzhang.platform.ui.activity.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaoerzhang.platform.R;
import com.jiaoerzhang.platform.api.ApiService;
import com.jiaoerzhang.platform.api.UserApiService;
import com.jiaoerzhang.platform.lib_core.base.BaseActivity;
import com.jiaoerzhang.platform.lib_net.network.Response;
import com.jiaoerzhang.platform.lib_net.network.RetrofitHelper;
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

public class DisclaimerActivity extends BaseActivity {

    private List<String> list;
    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView titleView;

    private LoadingDialog loadingDialog;


    @Override
    protected void initData() {
        if (getIntent().getExtras().getString("from").equals("mz")){
            titleView.setText("terms-of-use");
        }else{
            titleView.setText("privacy-prolicy");
        }
        getData();
    }

    @Override
    protected void initView() {
        //自适应
//        webView.getSettings().setUseWideViewPort(true);
//        webView.getSettings().setLoadWithOverviewMode(true);
        loadingDialog = new LoadingDialog(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisclaimerActivity.this.finish();
            }
        });
    }

    @Override
    protected int bindLayoutId() {
        return R.layout.activity_disclaimer;
    }

    private void getData() {

        list = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(ApiService.PRIVATE_BASE_URL)
                .build();

        loadingDialog.show();
        UserApiService userApiService = retrofit.create(UserApiService.class);
        userApiService.getPrivate().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody response) throws Exception {

                String result = response.string();

                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        list.add(jsonArray.getString(i));
                    }

                    fillData();
                }


            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

                loadingDialog.dismiss();
            }
        });
    }

    /**
     * 数据
     */
    private void fillData() {
        if (getIntent().getExtras().getString("from").equals("mz")) {
            webView.loadUrl(list.get(1));
        } else {
            webView.loadUrl(list.get(0));
        }

        loadingDialog.dismiss();

    }
}
