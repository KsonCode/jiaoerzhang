package com.jiaoerzhang.platform.ui.activity.user;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import com.blankj.utilcode.util.SPUtils;
import com.jiaoerzhang.platform.R;
import com.jiaoerzhang.platform.api.UserApi;
import com.jiaoerzhang.platform.api.UserApiService;
import com.jiaoerzhang.platform.entity.user.FeedbackTypeEntity;
import com.jiaoerzhang.platform.lib_core.base.BaseActivity;
import com.jiaoerzhang.platform.lib_net.network.BaseResponse;
import com.jiaoerzhang.platform.lib_net.network.Response;
import com.jiaoerzhang.platform.utils.RetrofitUtils;
import com.jiaoerzhang.platform.widget.LoadingDialog;

import java.util.HashMap;
import java.util.List;

public class FeedbackActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.playback_problem)
    TextView playback_problem;
    @BindView(R.id.payment_problem)
    TextView payment_problem;
    @BindView(R.id.account_problem)
    TextView account_problem;
    @BindView(R.id.other_problem)
    TextView other_problem;

    @BindView(R.id.content)
    TextView content;

    @BindView(R.id.contact)
    TextView contact;

    @BindView(R.id.submit)
    ImageView submit;

    private String type = "other";

    private List<String> types;
    private LoadingDialog loadingDialog;

    @Override
    protected void initData() {
        requestProblems();

    }

    /**
     * 请求问题类型
     */
    private void requestProblems() {

        RetrofitUtils.getInstance().createService(UserApiService.class)
                .getFeedbackTypes(UserApi.FEEDBACK_TYPE_URL).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse<FeedbackTypeEntity>>() {
                    @Override
                    public void accept(BaseResponse<FeedbackTypeEntity> feedbackTypeEntity) throws Exception {

                        types = feedbackTypeEntity.getData().getType();
                        System.out.println("types:"+types);
                        fillTypes();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });

    }

    /**
     * 绘制类型
     */
    private void fillTypes() {

        type = types.get(0);
        playback_problem.setText(types.get(0));
        payment_problem.setText(types.get(1));
        account_problem.setText(types.get(2));
        other_problem.setText(types.get(3));

    }

    @Override
    protected void initView() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedbackActivity.this.finish();
            }
        });

        playback_problem.setOnClickListener(this);
        payment_problem.setOnClickListener(this);
        account_problem.setOnClickListener(this);
        other_problem.setOnClickListener(this);
        submit.setOnClickListener(this);
        loadingDialog = new LoadingDialog(this);


    }

    @Override
    protected int bindLayoutId() {
        return R.layout.activity_feedback;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.playback_problem:
                type = playback_problem.getText().toString();
                playback_problem.setBackgroundResource(R.drawable.pay_red_bg);
                playback_problem.setTextColor(ContextCompat.getColor(this, R.color.common_red_txt_color));

                account_problem.setBackgroundResource(R.drawable.pay_gray_bg);
                account_problem.setTextColor(ContextCompat.getColor(this, R.color.common_hint_txt_color));

                payment_problem.setBackgroundResource(R.drawable.pay_gray_bg);
                payment_problem.setTextColor(ContextCompat.getColor(this, R.color.common_hint_txt_color));

                other_problem.setBackgroundResource(R.drawable.pay_gray_bg);
                other_problem.setTextColor(ContextCompat.getColor(this, R.color.common_hint_txt_color));


                break;
            case R.id.account_problem:
                type = account_problem.getText().toString();
                playback_problem.setBackgroundResource(R.drawable.pay_gray_bg);
                playback_problem.setTextColor(ContextCompat.getColor(this, R.color.common_hint_txt_color));

                account_problem.setBackgroundResource(R.drawable.pay_red_bg);
                account_problem.setTextColor(ContextCompat.getColor(this, R.color.common_red_txt_color));

                payment_problem.setBackgroundResource(R.drawable.pay_gray_bg);
                payment_problem.setTextColor(ContextCompat.getColor(this, R.color.common_hint_txt_color));

                other_problem.setBackgroundResource(R.drawable.pay_gray_bg);
                other_problem.setTextColor(ContextCompat.getColor(this, R.color.common_hint_txt_color));

                break;
            case R.id.payment_problem:

                type = payment_problem.getText().toString();
                payment_problem.setBackgroundResource(R.drawable.pay_red_bg);
                payment_problem.setTextColor(ContextCompat.getColor(this, R.color.common_red_txt_color));

                account_problem.setBackgroundResource(R.drawable.pay_gray_bg);
                account_problem.setTextColor(ContextCompat.getColor(this, R.color.common_hint_txt_color));

                playback_problem.setBackgroundResource(R.drawable.pay_gray_bg);
                playback_problem.setTextColor(ContextCompat.getColor(this, R.color.common_hint_txt_color));

                other_problem.setBackgroundResource(R.drawable.pay_gray_bg);
                other_problem.setTextColor(ContextCompat.getColor(this, R.color.common_hint_txt_color));




                break;
            case R.id.other_problem:
                type = other_problem.getText().toString();
                other_problem.setBackgroundResource(R.drawable.pay_red_bg);
                other_problem.setTextColor(ContextCompat.getColor(this, R.color.common_red_txt_color));

                payment_problem.setBackgroundResource(R.drawable.pay_gray_bg);
                payment_problem.setTextColor(ContextCompat.getColor(this, R.color.common_hint_txt_color));

                playback_problem.setBackgroundResource(R.drawable.pay_gray_bg);
                playback_problem.setTextColor(ContextCompat.getColor(this, R.color.common_hint_txt_color));

                account_problem.setBackgroundResource(R.drawable.pay_gray_bg);
                account_problem.setTextColor(ContextCompat.getColor(this, R.color.common_hint_txt_color));



                break;
            case R.id.submit:
                feedbackProblem(type);
                break;
        }
    }

    /**
     * 提交反馈
     */
    private void feedbackProblem(String type) {

        HashMap<String,String> params = new HashMap<>();
        if (content.getText().toString().length()==0){
            showToast(getString(R.string.feedback_info));
            return;
        }
        if (contact.getText().toString().length()!=10){
            showToast(getString(R.string.contact_number));
            return;
        }
        loadingDialog.show();
        params.put("content",content.getText().toString());
        params.put("type",type);
        params.put("username", SPUtils.getInstance().getString("username"));
        params.put("contact",contact.getText().toString());

        RetrofitUtils.getInstance().createService(UserApiService.class)
                .feedbackProblem(UserApi.FEEDBACK_URL,params).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response>() {
                    @Override
                    public void accept(Response response) throws Exception {

                        showToast(response.getMessage());

                        loadingDialog.dismiss();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        loadingDialog.dismiss();
                    }
                });


    }
}
