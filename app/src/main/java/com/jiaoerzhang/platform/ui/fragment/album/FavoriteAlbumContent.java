package com.jiaoerzhang.platform.ui.fragment.album;import android.os.Bundle;import android.support.annotation.Nullable;import android.support.v4.app.Fragment;import android.support.v7.widget.LinearLayoutManager;import android.text.TextUtils;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import com.blankj.utilcode.util.SPUtils;import com.blankj.utilcode.util.ToastUtils;import com.google.gson.Gson;import com.jcodecraeer.xrecyclerview.XRecyclerView;import com.jiaoerzhang.platform.R;import com.jiaoerzhang.platform.adapter.album.AlbumItemAdapter;import com.jiaoerzhang.platform.api.AlbumApi;import com.jiaoerzhang.platform.api.AlbumApiService;import com.jiaoerzhang.platform.api.UserApi;import com.jiaoerzhang.platform.api.UserApiService;import com.jiaoerzhang.platform.entity.album.AlbumEntity;import com.jiaoerzhang.platform.entity.album.SearchAlbumEntity;import com.jiaoerzhang.platform.entity.user.CollectionEntity;import com.jiaoerzhang.platform.lib_net.network.BaseResponse;import com.jiaoerzhang.platform.lib_net.network.http.HttpRequestPresenter;import com.jiaoerzhang.platform.lib_net.network.http.ModelCallback;import com.jiaoerzhang.platform.utils.AlbumRetrofitUtils;import com.jiaoerzhang.platform.utils.RetrofitUtils;import com.jiaoerzhang.platform.widget.LoadingDialog;import java.util.ArrayList;import java.util.HashMap;import java.util.List;import io.reactivex.android.schedulers.AndroidSchedulers;import io.reactivex.functions.Consumer;import io.reactivex.schedulers.Schedulers;public class FavoriteAlbumContent extends Fragment {    private XRecyclerView albumRv;    private int page = 1;    private int pageSize = 10000;    private AlbumItemAdapter albumItemAdapter = null;    private LoadingDialog loadingDialog;    public static Fragment getInstance(Bundle bundle) {        FavoriteAlbumContent fragment = new FavoriteAlbumContent();        fragment.setArguments(bundle);        return fragment;    }    @Override    public void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);    }    @Nullable    @Override    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {        return inflater.inflate(R.layout.fragment_albums_layout, container, false);    }    @Override    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {        super.onViewCreated(view, savedInstanceState);        initView(view);    }    private void initView(View view) {        albumRv = view.findViewById(R.id.albumRv);        loadingDialog = new LoadingDialog(getActivity());        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());        albumRv.setLayoutManager(linearLayoutManager);//        albumRv.setLoadingMoreEnabled(true);        requestFavorites();////        final HashMap<String, String> params = new HashMap<>();//        params.put("page", page + "");//        params.put("pageSize", pageSize + "");//        if (!TextUtils.isEmpty(getArguments().getString("code"))){//            params.put("style", getArguments().getString("code") + "");//        }////        params.put("clarity", 2+ "");//        final SearchAlbumEntity searchAlbumEntity = new SearchAlbumEntity();//        if (!TextUtils.isEmpty(getArguments().getString("code"))){//            searchAlbumEntity.style = getArguments().getString("code");//        }////        searchAlbumEntity.clarity = String.valueOf(2);//        final String key  = new Gson().toJson(searchAlbumEntity);//        System.out.println("key:"+key);//        requestData(key, params);        albumRv.setLoadingListener(new XRecyclerView.LoadingListener() {            @Override            public void onRefresh() {                page = 1;//                params.put("page", page + "");//                params.put("pageSize", pageSize + "");//                if (!TextUtils.isEmpty(getArguments().getString("code"))){//                    params.put("style", getArguments().getString("code") + "");//                }//                params.put("clarity", "2" + "");//                requestData(key, params);                requestFavorites();            }            @Override            public void onLoadMore() {                page++;//                params.put("page", page + "");//                params.put("pageSize", pageSize + "");//                if (!TextUtils.isEmpty(getArguments().getString("code"))){//                    params.put("style", getArguments().getString("code") + "");//                }//                params.put("clarity", "2" + "");//                requestData(key, params);                requestFavorites();            }        });    }    private void requestFavorites() {        HashMap<String,String> params = new HashMap<String,String>();        params.put("username", SPUtils.getInstance().getString("username"));        params.put("cycle", getArguments().getString("code") + "");        params.put("page", page+"");        params.put("pageSize", pageSize+"");        RetrofitUtils.getInstance().createService(UserApiService.class)                .collections(UserApi.COLLECTION_URL,params)                .subscribeOn(Schedulers.io())                .observeOn(AndroidSchedulers.mainThread())                .subscribe(new Consumer<BaseResponse<CollectionEntity>>() {                    @Override                    public void accept(BaseResponse<CollectionEntity> albumEntity) throws Exception {                        if ("200".equals(albumEntity.getStatus())){                            fillDatas(albumEntity);                        }else{                            ToastUtils.showShort(albumEntity.getMessage());                        }                    }                }, new Consumer<Throwable>() {                    @Override                    public void accept(Throwable throwable) throws Exception {                    }                });    }    List<AlbumEntity.Content> list = new ArrayList<>();    /**     * 填充收藏数据     * @param albumEntity     */    private void fillDatas(BaseResponse<CollectionEntity> albumEntity) {        for (CollectionEntity.Content content : albumEntity.getData().getContent()) {            AlbumRetrofitUtils.getInstance().createService(AlbumApiService.class)                    .getAlbumDetail(content.getAlbumPhotoId())                    .subscribeOn(Schedulers.io())                    .observeOn(AndroidSchedulers.mainThread())                    .subscribe(new Consumer<BaseResponse<AlbumEntity.Content>>() {                        @Override                        public void accept(BaseResponse<AlbumEntity.Content> albumEntity) throws Exception {                            if ("200".equals(albumEntity.getStatus())){                                list.add(albumEntity.getData());                            }else{                                ToastUtils.showShort(albumEntity.getMessage());                            }                        }                    }, new Consumer<Throwable>() {                        @Override                        public void accept(Throwable throwable) throws Exception {                        }                    });        }            albumItemAdapter = new AlbumItemAdapter(getActivity(), list);            albumRv.setAdapter(albumItemAdapter);            albumRv.refreshComplete();    }}