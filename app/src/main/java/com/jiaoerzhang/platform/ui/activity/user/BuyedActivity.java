package com.jiaoerzhang.platform.ui.activity.user;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.jiaoerzhang.platform.R;
import com.jiaoerzhang.platform.adapter.album.AlbumItemAdapter;
import com.jiaoerzhang.platform.api.AlbumApiService;
import com.jiaoerzhang.platform.api.UserApi;
import com.jiaoerzhang.platform.api.UserApiService;
import com.jiaoerzhang.platform.entity.album.AlbumEntity;
import com.jiaoerzhang.platform.entity.user.CollectionEntity;
import com.jiaoerzhang.platform.entity.user.MyBuyedEntity;
import com.jiaoerzhang.platform.lib_core.base.BaseActivity;
import com.jiaoerzhang.platform.lib_net.network.BaseResponse;
import com.jiaoerzhang.platform.utils.AlbumRetrofitUtils;
import com.jiaoerzhang.platform.utils.RetrofitUtils;
import com.jiaoerzhang.platform.widget.LoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BuyedActivity extends BaseActivity implements XRecyclerView.LoadingListener {

//    private LoadingDialog loadingDialog;
    private int page = 1;
    private int pageSize = 10;

    @BindView(R.id.albumRv)
    XRecyclerView albumRv;
    @BindView(R.id.back)
    ImageView back;




    @Override
    protected void initData() {

//        loadingDialog = new LoadingDialog(this);
        requestBuyed();

    }

    @Override
    protected void initView() {
        albumRv.setLayoutManager(new LinearLayoutManager(this));
        albumRv.setLoadingMoreEnabled(true);
        albumRv.setLoadingListener(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyedActivity.this.finish();
            }
        });

    }

    @Override
    protected int bindLayoutId() {
        return R.layout.activity_buyed;
    }

    private void requestBuyed() {
//        loadingDialog.show();

        HashMap<String,String> params = new HashMap<String,String>();
        params.put("username", SPUtils.getInstance().getString("username"));
        params.put("page", page+"");
        params.put("pageSize", pageSize+"");


        RetrofitUtils.getInstance().createService(UserApiService.class)
                .getBuyed(UserApi.BUYED_URL,params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse<MyBuyedEntity>>() {
                    @Override
                    public void accept(BaseResponse<MyBuyedEntity> albumEntity) throws Exception {

                        if ("200".equals(albumEntity.getStatus())){
                            fillDatas(albumEntity);
                        }else{
                            ToastUtils.showShort(albumEntity.getMessage());
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

    private AlbumItemAdapter albumItemAdapter;
    /**
     * 填充收藏数据
     * @param albumEntity
     */
    private void fillDatas(BaseResponse<MyBuyedEntity> albumEntity) {
        final List<AlbumEntity.Content> tempList = new ArrayList<>();
        for (MyBuyedEntity.Content content : albumEntity.getData().getContent()) {

            AlbumRetrofitUtils.getInstance().createService(AlbumApiService.class)
                    .getAlbumDetail(content.getAlbumId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<BaseResponse<AlbumEntity.Content>>() {
                        @Override
                        public void accept(BaseResponse<AlbumEntity.Content> albumEntity) throws Exception {

                            if ("200".equals(albumEntity.getStatus())){
                                tempList.add(albumEntity.getData());
                            }else{
                                ToastUtils.showShort(albumEntity.getMessage());
                            }

                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {

                        }
                    });
        }
//        loadingDialog.dismiss();
        if (page==1){
            albumItemAdapter = new AlbumItemAdapter(this, tempList);

            albumRv.setAdapter(albumItemAdapter);

            albumRv.refreshComplete();
        }else{
            albumItemAdapter.refreshData(tempList);
            albumRv.loadMoreComplete();
        }


    }

    @Override
    public void onRefresh() {
        page = 1;
        requestBuyed();

    }

    @Override
    public void onLoadMore() {
        page++;
        requestBuyed();

    }
}
