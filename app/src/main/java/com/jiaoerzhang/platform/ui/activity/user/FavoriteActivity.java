package com.jiaoerzhang.platform.ui.activity.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import com.blankj.utilcode.util.SPUtils;
import com.jiaoerzhang.platform.R;
import com.jiaoerzhang.platform.adapter.user.CollectionAdapter;
import com.jiaoerzhang.platform.api.UserApi;
import com.jiaoerzhang.platform.api.UserApiService;
import com.jiaoerzhang.platform.entity.album.AlbumEntity;
import com.jiaoerzhang.platform.entity.user.CollectionEntity;
import com.jiaoerzhang.platform.entity.user.UserEntity;
import com.jiaoerzhang.platform.lib_core.base.BaseActivity;
import com.jiaoerzhang.platform.lib_net.network.BaseResponse;
import com.jiaoerzhang.platform.utils.RetrofitUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FavoriteActivity extends BaseActivity {

    private int page = 1;
    private int pageSize = 10;

    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.favoriteRv)
    RecyclerView favoriteRv;

    private List<AlbumEntity.Content> TITLES =new ArrayList<AlbumEntity.Content>();

    @Override
    protected void initData() {

        HashMap<String,String> params = new HashMap<String,String>();
        params.put("username", SPUtils.getInstance().getString("username"));
        params.put("type", "1");
        params.put("page", page+"");
        params.put("pageSize", pageSize+"");


        RetrofitUtils.getInstance().createService(UserApiService.class)
                .collections(UserApi.COLLECTION_URL,params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse<CollectionEntity>>() {
                    @Override
                    public void accept(BaseResponse<CollectionEntity> albumEntity) throws Exception {

                        if ("200".equals(albumEntity.getStatus())){
                            fillDatas(albumEntity);
                        }else{
                            showToast(albumEntity.getMessage());
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });

    }

    /**
     * 添加数据
     * @param
     */
    private void fillDatas(BaseResponse<CollectionEntity> collectionEntity) {
//        TITLES = albumEntity.getData().getContent();
//        AlbumEntity.Content content = new AlbumEntity.Content("",1,1"all");
//         content: StyleEntity.Content = StyleEntity.Content("", 1, 1, "all")
//        TITLES!!.add(0, content)
//
//        var adapter = MyPagerAdapter(activity!!.supportFragmentManager)
//        pager.adapter = adapter
//        tabs.setViewPager(pager)
//        pager.currentItem = 0
//
//        tabs.setOnTabReselectedListener {
//
//        }


//
        favoriteRv.setLayoutManager(new LinearLayoutManager(this));
        CollectionAdapter collectionAdapter = new CollectionAdapter(this,R.layout.favorite_item_layout,collectionEntity.getData().getContent());

        favoriteRv.setAdapter(collectionAdapter);
    }

    @Override
    protected void initView() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavoriteActivity.this.finish();
            }
        });

    }

    @Override
    protected int bindLayoutId() {
        return R.layout.activity_favorite;
    }
}
