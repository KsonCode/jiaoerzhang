package com.jiaoerzhang.platform.ui.activity.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.library.flowlayout.FlowLayoutManager;
import com.jiaoerzhang.platform.R;
import com.jiaoerzhang.platform.adapter.user.AccountProblemAdapter;
import com.jiaoerzhang.platform.adapter.user.CoinAdapter;
import com.jiaoerzhang.platform.api.UserApi;
import com.jiaoerzhang.platform.api.UserApiService;
import com.jiaoerzhang.platform.entity.user.CoinHistoryEntity;
import com.jiaoerzhang.platform.entity.user.ProblemEntity;
import com.jiaoerzhang.platform.entity.user.UserEntity;
import com.jiaoerzhang.platform.lib_core.base.BaseActivity;
import com.jiaoerzhang.platform.lib_net.network.BaseResponse;
import com.jiaoerzhang.platform.utils.RetrofitUtils;
import com.jiaoerzhang.platform.widget.LoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TransactionDetailsActivity extends BaseActivity implements XRecyclerView.LoadingListener {

    private int page = 1;
    private int pageSize = 10;

    private CoinAdapter coinAdapter;
    private HashMap<String, String> params;

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.coin)
    TextView coinTv;
    @BindView(R.id.coinRv)
    XRecyclerView coinRv;
//    private LoadingDialog loadingDialog;

    @Override
    protected void initData() {
        getUserInfo();
        requestData();

    }

    private void getUserInfo() {

        HashMap<String,String> params = new HashMap<String,String>();
        params.put("username", SPUtils.getInstance().getString("username"));


        RetrofitUtils.getInstance().createService(UserApiService.class)
                .getUserInfo(UserApi.USERINFO_URL,params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse<UserEntity>>() {
                    @Override
                    public void accept(BaseResponse<UserEntity> userEntity) throws Exception {

                        if ("200".equals(userEntity.getStatus())){
                            coinTv.setText(userEntity.getData().getLadyCoin()+"");
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });

    }

    private void requestData() {
        params = new HashMap<>();
        params.put("page", page + "");
        params.put("username", SPUtils.getInstance().getString("username") + "");
        params.put("pageSize", pageSize + "");

//        loadingDialog.show();

        RetrofitUtils.getInstance().createService(UserApiService.class)
                .getCoinHistorys(UserApi.COINHISTORY_URL, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse<CoinHistoryEntity>>() {
                    @Override
                    public void accept(BaseResponse<CoinHistoryEntity> problemEntity) throws Exception {

                        if ("200".equals(problemEntity.getStatus())) {
                            fillDatas(problemEntity);

                        } else {
                            showToast(problemEntity.getMessage());
                        }
//                        loadingDialog.dismiss();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

//                        loadingDialog.dismiss();
                    }
                });
    }

    /**
     * 适配器
     *
     * @param problemEntity
     */
    private void fillDatas(BaseResponse<CoinHistoryEntity> problemEntity) {
        if (page == 1) {
            coinRv.refreshComplete();
            coinAdapter = new CoinAdapter(this, problemEntity.getData().getContent());
            coinRv.setAdapter(coinAdapter);
        }else{
            coinAdapter.addList(problemEntity.getData().getContent());
            coinRv.loadMoreComplete();
        }


//由于没数据暂时注释
//        coinAdapter = new CoinAdapter(this, problemEntity.getData().getContent());
//        coinRv.setLayoutManager(new LinearLayoutManager(this));
//        coinRv.setAdapter(coinAdapter);
    }

    @Override
    protected void initView() {
//        loadingDialog = new LoadingDialog(this);
        coinRv.setLoadingListener(this);
        coinRv.setLayoutManager(new LinearLayoutManager(this));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransactionDetailsActivity.this.finish();
            }
        });
        coinRv.setLoadingMoreEnabled(true);
    }

    @Override
    protected int bindLayoutId() {
        return R.layout.activity_transaction_details;
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
