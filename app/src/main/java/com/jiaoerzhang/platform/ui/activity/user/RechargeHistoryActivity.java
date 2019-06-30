package com.jiaoerzhang.platform.ui.activity.user;

import android.content.Intent;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.jiaoerzhang.platform.R;
import com.jiaoerzhang.platform.adapter.user.RechareItemAdapter;
import com.jiaoerzhang.platform.api.UserApi;
import com.jiaoerzhang.platform.api.UserApiService;
import com.jiaoerzhang.platform.entity.user.RechargeCoinEntity;
import com.jiaoerzhang.platform.entity.user.RechargeLogEntity;
import com.jiaoerzhang.platform.lib_core.base.BaseActivity;
import com.jiaoerzhang.platform.lib_net.network.BaseResponse;
import com.jiaoerzhang.platform.lib_net.network.Response;
import com.jiaoerzhang.platform.utils.RetrofitUtils;
import com.jiaoerzhang.platform.widget.LoadingDialog;

import java.util.HashMap;
import java.util.List;

public class RechargeHistoryActivity extends BaseActivity implements XRecyclerView.LoadingListener {


    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.coin_history)
    ImageView coin_history;
    @BindView(R.id.coinLogList)
    XRecyclerView coinListRv;
    private LoadingDialog loadingDialog;

    private HashMap<String, String> params = new HashMap<>();

    private RechareItemAdapter rechareItemAdapter;
    private int page = 1;
    private int pageSize = 10;


    @Override
    protected void initData() {

        requestCoinList();


    }

    /**
     * 充值档位
     */
    private void requestCoinList() {


        params.put("username", SPUtils.getInstance().getString("username"));
        params.put("page", page + "");
        params.put("pageSize", pageSize + "");

        loadingDialog.show();

        RetrofitUtils.getInstance().createService(UserApiService.class)
                .rechargeLog(UserApi.RECHARGE_LOG_URL, params).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse<RechargeLogEntity>>() {
                    @Override
                    public void accept(BaseResponse<RechargeLogEntity> response) throws Exception {

                        fillData(response.getData().getContent());
                        loadingDialog.dismiss();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        loadingDialog.dismiss();
                    }
                });

    }

    /**
     * 绘制列表
     *
     * @param data
     */
    private void fillData(List<RechargeLogEntity.Content> data) {

        if (page==1){
            rechareItemAdapter = new RechareItemAdapter(this, data);
            coinListRv.setAdapter(rechareItemAdapter);
            coinListRv.refreshComplete();
        }else{
            rechareItemAdapter.refreshData(data);
            coinListRv.loadMoreComplete();
        }



    }

    @Override
    protected void initView() {
        loadingDialog = new LoadingDialog(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RechargeHistoryActivity.this.finish();
            }
        });
        coin_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RechargeHistoryActivity.this, TransactionDetailsActivity.class);
                startActivity(intent);
            }
        });

        coinListRv.setLoadingListener(this);
        coinListRv.setLoadingMoreEnabled(true);

        coinListRv.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected int bindLayoutId() {
        return R.layout.activity_recharge_history;
    }


    @Override
    public void onRefresh() {
        page = 1;
        requestCoinList();

    }

    @Override
    public void onLoadMore() {
        page++;
        requestCoinList();
    }
}
