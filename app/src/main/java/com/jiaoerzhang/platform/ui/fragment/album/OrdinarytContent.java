package com.jiaoerzhang.platform.ui.fragment.album;import android.os.Bundle;import android.support.annotation.Nullable;import android.support.v4.app.Fragment;import android.support.v7.widget.LinearLayoutManager;import android.text.TextUtils;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import com.blankj.utilcode.util.SPUtils;import com.google.gson.Gson;import com.jcodecraeer.xrecyclerview.XRecyclerView;import com.jiaoerzhang.platform.R;import com.jiaoerzhang.platform.adapter.album.AlbumAdapter;import com.jiaoerzhang.platform.adapter.album.OrdinaryAlbumAdapter;import com.jiaoerzhang.platform.api.AlbumApi;import com.jiaoerzhang.platform.entity.album.AlbumEntity;import com.jiaoerzhang.platform.entity.album.SearchAlbumEntity;import com.jiaoerzhang.platform.lib_net.network.http.HttpRequestPresenter;import com.jiaoerzhang.platform.lib_net.network.http.ModelCallback;import com.jiaoerzhang.platform.widget.LoadingDialog;import java.util.HashMap;public class OrdinarytContent extends Fragment {    private XRecyclerView albumRv;    private int page = 1;    private int pageSize = 10;    OrdinaryAlbumAdapter albumItemAdapter = null;    public static Fragment getInstance(Bundle bundle) {        OrdinarytContent fragment = new OrdinarytContent();        fragment.setArguments(bundle);        return fragment;    }    @Override    public void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);    }    @Nullable    @Override    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {        return inflater.inflate(R.layout.fragment_albums_layout, container, false);    }    @Override    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {        super.onViewCreated(view, savedInstanceState);        initView(view);    }    private void initView(View view) {        albumRv = view.findViewById(R.id.albumRv);        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());        albumRv.setLayoutManager(linearLayoutManager);        albumRv.setLoadingMoreEnabled(true);//        TextView tv = (TextView) view.findViewById(R.id.tv_id);//        tv.setText(getArguments().getString("title"));        final HashMap<String, String> params = new HashMap<>();//        final SearchAlbumEntity searchAlbumEntity = (SearchAlbumEntity) getArguments().getSerializable("album");        System.out.println("style====2" + getArguments().getString("code"));        params.put("page", page + "");        params.put("pageSize", pageSize + "");        params.put("telPhone", SPUtils.getInstance().getString("username"));//5个        if (!TextUtils.isEmpty(getArguments().getString("code"))){            params.put("style", getArguments().getString("code") + "");        }        params.put("clarity", 1+ "");        final SearchAlbumEntity searchAlbumEntity = new SearchAlbumEntity();        if (!TextUtils.isEmpty(getArguments().getString("code"))){            searchAlbumEntity.style = getArguments().getString("code");        }        searchAlbumEntity.clarity = String.valueOf(1);        final String key  = new Gson().toJson(searchAlbumEntity);        System.out.println("key:"+key);        requestData(key, params);        albumRv.setLoadingListener(new XRecyclerView.LoadingListener() {            @Override            public void onRefresh() {                page = 1;                params.put("page", page + "");                params.put("pageSize", pageSize + "");                if (!TextUtils.isEmpty(getArguments().getString("code"))){                    params.put("style", getArguments().getString("code") + "");                }                params.put("clarity", "1" + "");                requestData(key, params);            }            @Override            public void onLoadMore() {                page++;                params.put("page", page + "");                params.put("pageSize", pageSize + "");                if (!TextUtils.isEmpty(getArguments().getString("code"))){                    params.put("style", getArguments().getString("code") + "");                }                params.put("clarity", "1" + "");                requestData(key, params);            }        });    }    private void requestData(String key, HashMap<String, String> params) {//        loadingDialog.show();        HttpRequestPresenter.getInstance().jsonPostData(AlbumApi.SEARCH_ALBUM_URL,params, key,                new ModelCallback<AlbumEntity>(AlbumEntity.class) {                    @Override                    public void onErrorMsg(int code, String msg) {//                        loadingDialog.dismiss();                    }                    @Override                    public void onSuccess(AlbumEntity albumEntity) {                        System.out.println("size=======" + albumEntity.getContent().size());                        if (page == 1) {                            albumItemAdapter = new OrdinaryAlbumAdapter(getActivity(), albumEntity.getContent());                            albumRv.setAdapter(albumItemAdapter);                            albumRv.refreshComplete();                        } else {                            albumItemAdapter.refreshData(albumEntity.getContent());                            albumRv.loadMoreComplete();                        }                    }                    @Override                    public void onSuccessMsg(String status, String message) {//                        loadingDialog.dismiss();                    }                });    }}