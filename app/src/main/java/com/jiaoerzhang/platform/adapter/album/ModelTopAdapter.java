package com.jiaoerzhang.platform.adapter.album;import android.content.Context;import android.content.Intent;import android.graphics.drawable.Drawable;import android.net.Uri;import android.support.annotation.NonNull;import android.support.v7.widget.RecyclerView;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.TextView;import com.blankj.utilcode.util.SPUtils;import com.blankj.utilcode.util.ToastUtils;import com.facebook.drawee.view.SimpleDraweeView;import com.library.flowlayout.FlowLayoutManager;import com.jiaoerzhang.platform.R;import com.jiaoerzhang.platform.api.UserApi;import com.jiaoerzhang.platform.api.UserApiService;import com.jiaoerzhang.platform.entity.album.ModelEntity;import com.jiaoerzhang.platform.entity.user.CollectionEntity;import com.jiaoerzhang.platform.lib_net.network.BaseResponse;import com.jiaoerzhang.platform.ui.activity.search.SearchHistoryActivity;import com.jiaoerzhang.platform.utils.RetrofitUtils;import java.util.ArrayList;import java.util.HashMap;import java.util.List;import io.reactivex.android.schedulers.AndroidSchedulers;import io.reactivex.functions.Consumer;import io.reactivex.schedulers.Schedulers;import static com.blankj.utilcode.util.ActivityUtils.startActivity;public class ModelTopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {    private Context context;    private List<ModelEntity.Content> all;    private List<ModelEntity.Content> topList = new ArrayList<>();    private List<ModelEntity.Content> normalList = new ArrayList<>();    private LayoutInflater inflater;    private static final int NORMAL = 0;//精品    private static final int TOP = 1;//轮播图    public ModelTopAdapter(Context context, List<ModelEntity.Content> list) {        this.context = context;        this.all = list;        inflater = LayoutInflater.from(context);        for (ModelEntity.Content content : all) {            for (int i = 0; i < all.size(); i++) {                if (i < 3) {                    topList.add(all.get(i));                } else {                    normalList.add(all.get(i));                }            }        }    }    @NonNull    @Override    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {        RecyclerView.ViewHolder holder;        if (viewType == NORMAL) {            View view = inflater.inflate(R.layout.model_item_layout, viewGroup, false);            holder = new MyViewHolder1(view);        } else if (viewType == TOP) {            View view = inflater.inflate(R.layout.model_topitem_layout, viewGroup, false);            holder = new MyViewHolder2(view);        } else {            holder = null;        }        return holder;    }    @Override    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {        if (NORMAL == viewHolder.getItemViewType()) {            final MyViewHolder1 myViewHolder1 = (MyViewHolder1) viewHolder;            final ModelEntity.Content content1 = normalList.get(i - 3);            Uri uri = Uri.parse(content1.getModelUrl());            myViewHolder1.simpleDraweeView.setImageURI(uri);            myViewHolder1.nameTv.setText(content1.getModelName());//            myViewHolder1.followNumTv.setText(content.getUcollectNum() + "");            myViewHolder1.followNumTv.setText("");            if ("true".equals(content1.isNot())) {                setDrawable(myViewHolder1.followNumTv,true);            } else {                setDrawable(myViewHolder1.followNumTv, false);            }            myViewHolder1.followNumTv.setOnClickListener(new View.OnClickListener() {                @Override                public void onClick(View v) {                    HashMap<String, String> params = new HashMap<String, String>();                    params.put("username", SPUtils.getInstance().getString("username"));                    params.put("modelId", content1.getId()+"");                    if (content1.isNot().equals("false")){                        RetrofitUtils.getInstance().createService(UserApiService.class)                                .follow(UserApi.FOLLOW_URL, params)                                .subscribeOn(Schedulers.io())                                .observeOn(AndroidSchedulers.mainThread())                                .subscribe(new Consumer<BaseResponse<CollectionEntity>>() {                                    @Override                                    public void accept(BaseResponse<CollectionEntity> collection) throws Exception {                                        if ("200".equals(collection.getStatus())) {                                            ToastUtils.showShort(collection.getMessage());                                            content1.setNot("true");                                            notifyDataSetChanged();                                            setDrawable(myViewHolder1.followNumTv, true);                                        } else {                                            ToastUtils.showShort(collection.getMessage());                                        }                                    }                                }, new Consumer<Throwable>() {                                    @Override                                    public void accept(Throwable throwable) throws Exception {                                    }                                });                    }else{                        RetrofitUtils.getInstance().createService(UserApiService.class)                                .unfollow(UserApi.UNFOLLOW_URL, params)                                .subscribeOn(Schedulers.io())                                .observeOn(AndroidSchedulers.mainThread())                                .subscribe(new Consumer<BaseResponse<CollectionEntity>>() {                                    @Override                                    public void accept(BaseResponse<CollectionEntity> collection) throws Exception {                                        if ("200".equals(collection.getStatus())) {                                            ToastUtils.showShort(collection.getMessage());                                            content1.setNot("false");                                            notifyDataSetChanged();                                            setDrawable(myViewHolder1.followNumTv, true);                                        } else {                                            ToastUtils.showShort(collection.getMessage());                                        }                                    }                                }, new Consumer<Throwable>() {                                    @Override                                    public void accept(Throwable throwable) throws Exception {                                    }                                });                    }                }            });            myViewHolder1.simpleDraweeView.setOnClickListener(new View.OnClickListener() {                @Override                public void onClick(View v) {                    Intent intent = new Intent(context, SearchHistoryActivity.class);                    intent.putExtra("keyword", content1.getModelName());                    startActivity(intent);                }            });        } else if (TOP == viewHolder.getItemViewType()) {            final MyViewHolder2 myViewHolder2 = (MyViewHolder2) viewHolder;            final ModelEntity.Content content = topList.get(i);            myViewHolder2.num.setText(i + 1 + "");            Uri uri = Uri.parse(content.getModelUrl());            myViewHolder2.simpleDraweeView.setImageURI(uri);//            myViewHolder2.followNumTv.setText(content.getUcollectNum() + "");            myViewHolder2.followNumTv.setText("");            myViewHolder2.nameTv.setText(content.getModelName());            if ("true".equals(content.isNot())) {                setDrawable(myViewHolder2.followNumTv,true);            } else {                setDrawable(myViewHolder2.followNumTv, false);            }            myViewHolder2.followNumTv.setOnClickListener(new View.OnClickListener() {                @Override                public void onClick(View v) {                    HashMap<String, String> params = new HashMap<String, String>();                    params.put("username", SPUtils.getInstance().getString("username"));                    params.put("modelId", content.getId()+"");                    if (content.isNot().equals("false")){                        RetrofitUtils.getInstance().createService(UserApiService.class)                                .follow(UserApi.FOLLOW_URL, params)                                .subscribeOn(Schedulers.io())                                .observeOn(AndroidSchedulers.mainThread())                                .subscribe(new Consumer<BaseResponse<CollectionEntity>>() {                                    @Override                                    public void accept(BaseResponse<CollectionEntity> collection) throws Exception {                                        if ("200".equals(collection.getStatus())) {                                            ToastUtils.showShort(collection.getMessage());                                            content.setNot("true");                                            notifyDataSetChanged();                                            setDrawable(myViewHolder2.followNumTv, true);                                        } else {                                            ToastUtils.showShort(collection.getMessage());                                        }                                    }                                }, new Consumer<Throwable>() {                                    @Override                                    public void accept(Throwable throwable) throws Exception {                                    }                                });                    }else{                        RetrofitUtils.getInstance().createService(UserApiService.class)                                .unfollow(UserApi.UNFOLLOW_URL, params)                                .subscribeOn(Schedulers.io())                                .observeOn(AndroidSchedulers.mainThread())                                .subscribe(new Consumer<BaseResponse<CollectionEntity>>() {                                    @Override                                    public void accept(BaseResponse<CollectionEntity> collection) throws Exception {                                        if ("200".equals(collection.getStatus())) {                                            ToastUtils.showShort(collection.getMessage());                                            content.setNot("false");                                            notifyDataSetChanged();                                            setDrawable(myViewHolder2.followNumTv, true);                                        } else {                                            ToastUtils.showShort(collection.getMessage());                                        }                                    }                                }, new Consumer<Throwable>() {                                    @Override                                    public void accept(Throwable throwable) throws Exception {                                    }                                });                    }//                    follow(content.getId() + "", myViewHolder2.followNumTv);                }            });            myViewHolder2.simpleDraweeView.setOnClickListener(new View.OnClickListener() {                @Override                public void onClick(View v) {                    Intent intent = new Intent(context, SearchHistoryActivity.class);                    intent.putExtra("keyword", content.getModelName());                    startActivity(intent);                }            });//            System.out.println("ssssssss====" + all.get(i).getStyleLists());            ModleTagAdapter modleTagAdapter = new ModleTagAdapter(context, R.layout.search_history_item_layout, content.getStyleLists());            myViewHolder2.tagRv.setLayoutManager(new FlowLayoutManager());            myViewHolder2.tagRv.setAdapter(modleTagAdapter);        }    }    private void setDrawable(TextView followNumTv, boolean b) {        Drawable drawable = null;        if (b){           drawable = context.getResources().getDrawable(R.mipmap.follow_icon);        }else{            drawable = context.getResources().getDrawable(R.mipmap.unfollow_icon);        }        //一定要加这行！！！！！！！！！！！        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());        followNumTv.setCompoundDrawables(null, null, drawable, null);    }    public void refresh(List<ModelEntity.Content> list) {        if (list.size() == 0) {            ToastUtils.showShort(R.string.no_more_data);            return;        }        all.addAll(list);        notifyDataSetChanged();    }    private void follow(String modelId, final TextView tv) {    }    @Override    public int getItemCount() {        return all.size();    }    @Override    public int getItemViewType(int position) {        if (getItemCount() > 3) {            if (position < 3) {                return TOP;            } else {                return NORMAL;            }        } else {            return TOP;        }    }    @Override    public long getItemId(int position) {        return super.getItemId(position);    }    class MyViewHolder1 extends RecyclerView.ViewHolder {        private SimpleDraweeView simpleDraweeView;        private TextView nameTv;        private TextView followNumTv;        public MyViewHolder1(@NonNull View itemView) {            super(itemView);            simpleDraweeView = itemView.findViewById(R.id.topIcon);            nameTv = itemView.findViewById(R.id.topNameTv);            followNumTv = itemView.findViewById(R.id.followingNum);        }    }    class MyViewHolder2 extends RecyclerView.ViewHolder {        private SimpleDraweeView simpleDraweeView;        private TextView nameTv;        private TextView num;        private TextView followNumTv;        private RecyclerView tagRv;        public MyViewHolder2(@NonNull View itemView) {            super(itemView);            simpleDraweeView = itemView.findViewById(R.id.topIcon);            nameTv = itemView.findViewById(R.id.topNameTv);            num = itemView.findViewById(R.id.num);            followNumTv = itemView.findViewById(R.id.followingNum);            tagRv = itemView.findViewById(R.id.tagRv);        }    }}