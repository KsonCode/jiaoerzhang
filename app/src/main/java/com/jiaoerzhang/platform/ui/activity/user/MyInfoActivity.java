package com.jiaoerzhang.platform.ui.activity.user;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.jiaoerzhang.platform.R;
import com.jiaoerzhang.platform.api.UserApi;
import com.jiaoerzhang.platform.api.UserApiService;
import com.jiaoerzhang.platform.entity.user.HeadEntity;
import com.jiaoerzhang.platform.entity.user.NickNameEntity;
import com.jiaoerzhang.platform.entity.user.RechargeCoinEntity;
import com.jiaoerzhang.platform.lib_core.base.BaseActivity;
import com.jiaoerzhang.platform.lib_net.network.BaseResponse;
import com.jiaoerzhang.platform.utils.RetrofitUtils;
import com.jiaoerzhang.platform.widget.LoadingDialog;
import com.jiaoerzhang.platform.widget.PhotoDialog;
import com.jiaoerzhang.platform.widget.SaveDialog;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class MyInfoActivity extends BaseActivity implements PhotoDialog.OnCloseListener, PhotoDialog.SelectorListener, SaveDialog.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.save)
    TextView save;

    @BindView(R.id.myinfo_icon)
    SimpleDraweeView simpleDraweeView;

    @BindView(R.id.photoFrameLayout)
    FrameLayout photoFrameLayout;

    @BindView(R.id.nameTv)
    EditText nametv;

    private String filePath = "";
    private boolean isUpdated = false;
    private boolean isPhotoUpdated = false;


    private PhotoDialog photoDialog;
    private SaveDialog saveDialog;
    private LoadingDialog loadingDialog;

    @OnClick(R.id.photo_layout)
    public void photoLayout(View view) {

        if (!photoDialog.isShowing()) {
            photoDialog.show();
        }

    }

    @OnClick(R.id.save)
    public void save(View view) {
        if (TextUtils.isEmpty(filePath)) {
            ToastUtils.showShort(R.string.file_empty);
            return;
        }

        if (TextUtils.isEmpty(nametv.getText().toString())) {
            ToastUtils.showShort(R.string.nickname_empty);
            return;
        }

        if (!saveDialog.isShowing()) {
            saveDialog.show();
        }


    }

    /**
     * 更新昵称
     */
    private void updateNickname() {


        HashMap<String, String> params = new HashMap<>();
        params.put("username", SPUtils.getInstance().getString("username"));
        params.put("nickname", nametv.getText().toString());
        RetrofitUtils.getInstance().createService(UserApiService.class)
                .updateNickName(UserApi.UPDATE_NICKNAME_URL, params).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse<NickNameEntity>>() {
                    @Override
                    public void accept(BaseResponse<NickNameEntity> response) throws Exception {

                        ToastUtils.showShort(response.getMessage());
                        isUpdated = false;
                        isPhotoUpdated = true;
                        saveDialog.dismiss();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    /**
     * 上传头像
     */
    private void uploadFile() {

        loadingDialog.show();

        File file = new File(filePath);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part MultipartFile =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);


        RetrofitUtils.getInstance().createService(UserApiService.class)
                .uploadPhoto(UserApi.UPDATE_HEAD_URL, SPUtils.getInstance().getString("username"), MultipartFile).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse<HeadEntity>>() {
                    @Override
                    public void accept(BaseResponse<HeadEntity> response) throws Exception {

                        loadingDialog.dismiss();
                        ToastUtils.showShort(response.getMessage());
                        isUpdated = false;
                        isPhotoUpdated = true;
                        saveDialog.dismiss();


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        loadingDialog.dismiss();
                    }
                });


    }

    private String nickName;

    @Override
    protected void initData() {

        nickName = getIntent().getExtras().getString("nickname");

        nametv.setText(nickName);


    }

    @Override
    protected void initView() {
        loadingDialog = new LoadingDialog(this);

        nametv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!nickName.equals(s.toString()) && s.length() != 0) {
                    isUpdated = true;
                } else {
                    isUpdated = false;
                }

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUpdated && !isPhotoUpdated) {
                    if (!saveDialog.isShowing()) {
                        saveDialog.show();
                    }
                } else {
                    MyInfoActivity.this.finish();
                }

            }
        });

        photoDialog = new PhotoDialog(this);
        photoDialog.setCancelable(true);
        photoDialog.setListener(this);
        photoDialog.setSelectorListener(this);

        saveDialog = new SaveDialog(this);
        saveDialog.setCancelable(true);
        saveDialog.setOnClickListener(this);

    }

    @Override
    protected int bindLayoutId() {
        return R.layout.activity_my_info;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    isUpdated = true;
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    System.out.println("selectList::" + selectList);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
//                    adapter.setList(selectList);
//                    adapter.notifyDataSetChanged();
                    filePath = selectList.get(0).getCompressPath();
                    System.out.println("filepath===" + filePath);
                    Uri uri = Uri.parse("file://" + filePath);
                    simpleDraweeView.setImageURI(uri);
                    if (photoDialog.isShowing()) {
                        photoDialog.dismiss();
                    }
                    photoFrameLayout.setVisibility(View.GONE);
                    break;
            }
        }
    }

    @Override
    public void onClick(Dialog dialog, boolean confirm) {

        if (confirm) {
            if (photoDialog.isPhoto) {//选择相册
                PictureSelector.create(MyInfoActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .maxSelectNum(1)
                        .compress(true)
                        .enableCrop(true)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
            } else {//拍照

                new RxPermissions(this).request(Manifest.permission.CAMERA).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {

                        if (aBoolean) {
                            PictureSelector.create(MyInfoActivity.this)
                                    .openCamera(PictureMimeType.ofImage())
                                    .enableCrop(true)
                                    .compress(true)
                                    .forResult(PictureConfig.CHOOSE_REQUEST);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
            }
        } else {
            photoDialog.dismiss();
        }


    }

    /**
     * @param isPhoto 选择图片还是牌照，暂时不用
     */
    @Override
    public void onClick(boolean isPhoto) {


    }

    /**
     * 保存yes
     */
    @Override
    public void onYesClick() {
        if (isUpdated){
            saveDialog.dismiss();
        }

        if (TextUtils.isEmpty(filePath)) {
            ToastUtils.showShort(R.string.file_empty);
            return;
        }

        if (TextUtils.isEmpty(nametv.getText().toString())) {
            ToastUtils.showShort(R.string.nickname_empty);
            return;
        }


        uploadFile();
        updateNickname();
    }

    @Override
    public void onNoClick() {

        this.finish();
    }

    @Override
    public void onBackPressed() {

        if (isUpdated && !isPhotoUpdated) {
            if (!saveDialog.isShowing()) {
                saveDialog.show();
            }
        } else {
            super.onBackPressed();
        }

    }
}
