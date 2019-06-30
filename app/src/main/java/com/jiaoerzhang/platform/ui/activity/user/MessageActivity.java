package com.jiaoerzhang.platform.ui.activity.user;

import android.content.Intent;
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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.library.flowlayout.FlowLayoutManager;
import com.jiaoerzhang.platform.R;
import com.jiaoerzhang.platform.adapter.user.AccountProblemAdapter;
import com.jiaoerzhang.platform.adapter.user.MessagesAdapter;
import com.jiaoerzhang.platform.api.UserApi;
import com.jiaoerzhang.platform.api.UserApiService;
import com.jiaoerzhang.platform.entity.user.MessageEntity;
import com.jiaoerzhang.platform.entity.user.ProblemEntity;
import com.jiaoerzhang.platform.lib_core.base.BaseActivity;
import com.jiaoerzhang.platform.lib_net.network.BaseResponse;
import com.jiaoerzhang.platform.utils.RetrofitUtils;
import com.jiaoerzhang.platform.widget.LoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends BaseActivity {
    private int page = 1;
    private int pageSize = 5;
    private MessagesAdapter messagesAdapter;

    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.messageRv)
    RecyclerView messageRv;

    private LoadingDialog loadingDialog;

    private List<MessageEntity.Content> list = new ArrayList<>();

    @Override
    protected void initData() {

        requestSystem();
        requestPlatform();



    }

    /**
     * 获取平台消息
     */
    private void requestPlatform() {
        loadingDialog.show();
        HashMap<String, String> params = new HashMap<>();
        params.put("page",page+"");
        params.put("pageSize", pageSize + "");
        RetrofitUtils.getInstance().createService(UserApiService.class)
                .getSysMessages(UserApi.PLATFORM_MESSAGE_URL, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse<MessageEntity>>() {
                    @Override
                    public void accept(BaseResponse<MessageEntity> problemEntity) throws Exception {

                        if ("200".equals(problemEntity.getStatus())) {
                            list.addAll(problemEntity.getData().getContent());
                            requestSystem();


                        }else{
                            showToast(problemEntity.getMessage());
                        }

                        loadingDialog.dismiss();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        loadingDialog.dismiss();
                    }
                });
    }

//    private void fillPlatformDatas(BaseResponse<MessageEntity> problemEntity) {
//
//        platformMessagesAdapter = new MessagesAdapter(this, R.layout.message_item_layout, problemEntity.getData().getContent());
//
//
//        platfromMessageRv.setLayoutManager(new LinearLayoutManager(this));
//        platfromMessageRv.setAdapter(platformMessagesAdapter);
//        platformMessagesAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//            }
//        });
//    }

    /**
     * 获取系统消息
     */
    private void requestSystem() {
        loadingDialog.show();
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page + "");
        params.put("username", SPUtils.getInstance().getString("username") + "");
        params.put("pageSize", pageSize + "");
        RetrofitUtils.getInstance().createService(UserApiService.class)
                .getSysMessages(UserApi.SYS_MESSAGE_URL, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse<MessageEntity>>() {
                    @Override
                    public void accept(BaseResponse<MessageEntity> problemEntity) throws Exception {

                        if ("200".equals(problemEntity.getStatus())) {
                            list.addAll(problemEntity.getData().getContent());
                            fillDatas();


                        }else{
                            showToast(problemEntity.getMessage());
                        }
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
     * 获取系统消息
     * @param
     */
    private void fillDatas() {

        messagesAdapter = new MessagesAdapter(this, R.layout.message_item_layout, list);


        messageRv.setLayoutManager(new LinearLayoutManager(this));
        messageRv.setAdapter(messagesAdapter);
        messagesAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });
    }

    @Override
    protected void initView() {
        loadingDialog = new LoadingDialog(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageActivity.this.finish();
            }
        });
    }

    @Override
    protected int bindLayoutId() {
        return R.layout.activity_message;
    }
}
