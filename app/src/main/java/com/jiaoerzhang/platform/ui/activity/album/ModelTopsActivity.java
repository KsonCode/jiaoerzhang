package com.jiaoerzhang.platform.ui.activity.album;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.SPUtils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.jiaoerzhang.platform.R;
import com.jiaoerzhang.platform.adapter.album.ModelTopAdapter;
import com.jiaoerzhang.platform.api.AlbumApi;
import com.jiaoerzhang.platform.entity.album.ModelEntity;
import com.jiaoerzhang.platform.lib_core.base.BaseActivity;
import com.jiaoerzhang.platform.lib_net.network.http.HttpRequestPresenter;
import com.jiaoerzhang.platform.lib_net.network.http.ModelCallback;
import com.jiaoerzhang.platform.widget.LoadingDialog;

import java.util.HashMap;

import butterknife.BindView;

public class ModelTopsActivity extends BaseActivity implements View.OnClickListener , XRecyclerView.LoadingListener {

    private int page = 1;
    private int pageSize = 2000;
    private LoadingDialog loadingDialog;

    @BindView(R.id.modelRv)
    XRecyclerView modelRv;
    @BindView(R.id.back)
    ImageView back;
    private ModelTopAdapter modelTopAdapter;

    @Override
    protected void initData() {

        requestData();

    }

    @Override
    protected void initView() {
        loadingDialog = new LoadingDialog(this);
        back.setOnClickListener(this);
        modelRv.setLoadingMoreEnabled(true);


        modelRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }

    @Override
    protected int bindLayoutId() {
        return R.layout.activity_model_tops;
    }

    private void requestData() {
        HashMap<String,String> params = new HashMap<String, String>();
        params.put("telPhone", SPUtils.getInstance().getString("username"));
        params.put("page", page+"");
        params.put("pageSize", pageSize+"");
//      loadingDialog!!.show()
        loadingDialog.show();

        HttpRequestPresenter.getInstance().post(AlbumApi.MODEL_ALL_URL, params, new ModelCallback<ModelEntity>(ModelEntity.class) {
            @Override
            public void onErrorMsg(int code, String msg) {
                if (loadingDialog.isShowing())
                loadingDialog.dismiss();

            }

            @Override
            public void onSuccess(ModelEntity modelEntity) {
                modelRv.setLoadingListener(ModelTopsActivity.this);
                if (page==1){
                    modelTopAdapter = new ModelTopAdapter(ModelTopsActivity.this, modelEntity.getContent());

                    modelRv.setAdapter(modelTopAdapter);
                    modelRv.refreshComplete();
                }else{
                    modelTopAdapter.refresh(modelEntity.getContent());
                    modelRv.loadMoreComplete();
                }
                if (loadingDialog.isShowing())
                loadingDialog.dismiss();
            }

            @Override
            public void onSuccessMsg(String status, String message) {

            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
        }
    }

    @Override
    public void onRefresh() {

        page = 1;
        requestData();
    }

    @Override
    public void onLoadMore() {
        page++;
        requestData();


    }
}
