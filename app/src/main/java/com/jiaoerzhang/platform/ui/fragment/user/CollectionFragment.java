package com.jiaoerzhang.platform.ui.fragment.user;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.jiaoerzhang.platform.R;
import com.jiaoerzhang.platform.adapter.album.AlbumItemAdapter;
import com.jiaoerzhang.platform.contract.AlbumContract;
import com.jiaoerzhang.platform.entity.album.AlbumEntity;
import com.jiaoerzhang.platform.lib_core.base.mvp.BaseMvpFragment;
import com.jiaoerzhang.platform.lib_core.base.mvp.BasePresenter;
import com.jiaoerzhang.platform.presenter.AlbumPresenter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

public class CollectionFragment extends BaseMvpFragment<AlbumContract.IAlbumModel, AlbumContract.AlbumPresenter> implements  AlbumContract.IAlbumView {
    private int page = 1;
    private int  pageSize = 5;
    private String ARG_POSITION = "position";

    private int  position = 0;
    private String style  = "{}";

    private AlbumItemAdapter albumItemAdapter= null;
    private List<AlbumEntity.Content> list = new ArrayList<>();
    private HashMap<String,String> params = new HashMap<>();


    @BindView(R.id.albumRv)
    XRecyclerView albumRv;

    public CollectionFragment newInstance(int position, String style){
        CollectionFragment f = new CollectionFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        if (TextUtils.isEmpty(style)){
            b.putString("style","{}");
        }else{
            b.putString("style","{\"style\":\"$style\"}");
        }
        f.setArguments(b);
        return f;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_albums_layout;
    }

    @Override
    protected void initViewsAndEvents(View view) {


    }

    @Override
    public void initData() {
        super.initData();
        style = getArguments().getString("style");
        params.put("page",page+"");
        params.put("pageSize",pageSize+"");
        params.put("style",style+"");
        params.put("telPhone", SPUtils.getInstance().getString("username"));//5个

        presenter.getAlbums(style,params);
        albumRv.setLoadingMoreEnabled(true);
        albumRv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page=1;
                params.put("page",page+"");
                params.put("pageSize",pageSize+"");
                params.put("style",style+"");
                presenter.getAlbums(style,params);
            }

            @Override
            public void onLoadMore() {
                page++;
                params.put("page",page+"");
                params.put("pageSize",pageSize+"");
                params.put("style",style+"");
                presenter.getAlbums(style,params);
            }
        });





    }

    @Override
    public void init() {

    }

    @Override
    protected void DetoryViewAndThing() {

    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    public void sucess(@NotNull AlbumEntity albumEntity) {
        System.out.println("size======="+albumEntity.getContent().size());

        albumEntity.getContent().size();

        albumRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        if (page==1){
            albumItemAdapter = new AlbumItemAdapter(getActivity(), albumEntity.getContent());

            albumRv.setAdapter(albumItemAdapter);

            albumRv.refreshComplete();
        }else{

            albumItemAdapter.refreshData(albumEntity.getContent());

            albumRv.loadMoreComplete();

        }
    }

    @Override
    public BasePresenter initPresenter() {
        return new AlbumPresenter();
    }
}
