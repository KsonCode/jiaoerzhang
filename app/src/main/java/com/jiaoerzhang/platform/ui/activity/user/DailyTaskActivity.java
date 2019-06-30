package com.jiaoerzhang.platform.ui.activity.user;

import android.content.Intent;

import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;

import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;

import com.facebook.share.widget.ShareDialog;
import com.jiaoerzhang.platform.R;
import com.jiaoerzhang.platform.api.UserApi;
import com.jiaoerzhang.platform.api.UserApiService;
import com.jiaoerzhang.platform.entity.user.ShareInfoEntity;
import com.jiaoerzhang.platform.lib_core.base.BaseActivity;
import com.jiaoerzhang.platform.lib_net.network.BaseResponse;
import com.jiaoerzhang.platform.lib_net.network.Response;
import com.jiaoerzhang.platform.utils.AlbumRetrofitUtils;
import com.jiaoerzhang.platform.utils.RetrofitUtils;

import java.util.HashMap;
import java.util.List;


public class DailyTaskActivity extends BaseActivity {

    private CallbackManager callbackManager;
    private ShareDialog shareDialog;

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.check_in)
    Button check_in;
//    @BindView(R.id.share)
//    Button share;

    @Override
    protected void initData() {


//        getShareInfo();

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        // this part is optional
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {

            @Override
            public void onSuccess(Sharer.Result result) {
                //分享成功的回调，在这里做一些自己的逻辑处理
                ToastUtils.showShort(R.string.share_success);
                shareCoin();
            }

            @Override
            public void onCancel() {
                ToastUtils.showShort(R.string.share_cancel);
            }

            @Override
            public void onError(FacebookException error) {
                ToastUtils.showShort(error.toString());
            }
        });


    }

    private  ShareInfoEntity shareInfoEntity;
    private void getShareInfo() {
        AlbumRetrofitUtils.getInstance().createService(UserApiService.class)
                .shareInfo(UserApi.SHARED_INFO_URL).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse<List<ShareInfoEntity>>>() {
                    @Override
                    public void accept(BaseResponse<List<ShareInfoEntity>> listBaseResponse) throws Exception {

                        shareInfoEntity = listBaseResponse.getData().get(0);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    /**
     * 分享得金币
     */
    private void shareCoin() {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", SPUtils.getInstance().getString("username"));


        RetrofitUtils.getInstance().createService(UserApiService.class)
                .share(UserApi.SHARED_URL, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response>() {
                    @Override
                    public void accept(Response response) throws Exception {

                        showToast(response.getMessage());

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    @Override
    protected void initView() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DailyTaskActivity.this.finish();
            }
        });
        check_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DailyTaskActivity.this,CheckInActivity.class));
            }
        });

//        share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                shareTo();
//            }
//        });

    }

    /**
     * 分享到facebook
     * 若未安装facebook客户端，则会跳转到浏览器
     */
    private void shareTo() {
        System.out.println("shareDialog===="+shareDialog);
        if (shareInfoEntity==null){
            return;
        }

        //这里分享一个链接，更多分享配置参考官方介绍：https://developers.facebook.com/docs/sharing/android
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            //链接，新api
//            ShareContent shareContent = new ShareFeedContent.Builder()
//                    .setLink(shareInfoEntity.getContentUrl())
//                    .setLinkName(shareInfoEntity.getTitle())
//                    .setLinkDescription(shareInfoEntity.getDesc())
//                    .setPicture(shareInfoEntity.getImgUrl())
//                    .build();
//            shareDialog.show(shareContent);
            //链接，带引语
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(shareInfoEntity.getContentUrl()))
                    .setQuote(shareInfoEntity.getDesc())
                    .build();
            shareDialog.show(linkContent);

            //链接，旧api
//            ShareLinkContent linkContent = new ShareLinkContent.Builder()
//                    .setContentTitle(shareInfoEntity.getTitle())
//                    .setContentDescription(shareInfoEntity.getDesc())
//                    .setContentUrl(Uri.parse(shareInfoEntity.getContentUrl()))
//                    .setImageUrl(Uri.parse(shareInfoEntity.getImgUrl()))
//                    .build();
//
//            shareDialog.show(linkContent);
        }
    }

    @Override
    protected int bindLayoutId() {
        return R.layout.activity_daily_task;
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //facebook的界面回调
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
