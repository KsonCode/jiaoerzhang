package com.jiaoerzhang.platform.ui.activity.user;

import android.content.Intent;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.library.flowlayout.FlowLayoutManager;
import com.jiaoerzhang.platform.R;
import com.jiaoerzhang.platform.adapter.user.AccountProblemAdapter;
import com.jiaoerzhang.platform.api.UserApi;
import com.jiaoerzhang.platform.api.UserApiService;
import com.jiaoerzhang.platform.entity.user.ProblemEntity;
import com.jiaoerzhang.platform.lib_core.base.BaseActivity;
import com.jiaoerzhang.platform.lib_net.network.BaseResponse;
import com.jiaoerzhang.platform.utils.RetrofitUtils;
import com.jiaoerzhang.platform.widget.LoadingDialog;

import java.util.HashMap;

public class HelpActivity extends BaseActivity {

    private int page = 1;
    private int pageSize = 10;

    private AccountProblemAdapter accountProblemAdapter;

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.otherRv)
    RecyclerView otherRv;

    @BindView(R.id.telphone)
    TextView telphoneTv;

    @BindView(R.id.feedback)
    TextView feedbackTv;

    private LoadingDialog loadingDialog;

    @Override
    protected void initData() {

        getProblems();

    }

    /**
     * 获取问题
     */
    private void getProblems() {
        loadingDialog.show();
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page + "");
        params.put("type", "0" + "");//other
        params.put("pageSize", pageSize + "");
        RetrofitUtils.getInstance().createService(UserApiService.class)
                .getProblems(UserApi.ISSUES_ALL_URL, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse<ProblemEntity>>() {
                    @Override
                    public void accept(BaseResponse<ProblemEntity> problemEntity) throws Exception {


                        if ("200".equals(problemEntity.getStatus())) {
                            fillDatas(problemEntity);

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
     * 绘制数据
     *
     * @param problemEntity
     */
    private void fillDatas(final BaseResponse<ProblemEntity> problemEntity) {


        accountProblemAdapter = new AccountProblemAdapter(this, R.layout.problem_item_layout, problemEntity.getData().getContent());


        otherRv.setLayoutManager(new FlowLayoutManager());
        otherRv.setAdapter(accountProblemAdapter);
        accountProblemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                showToast(problemEntity.getData().getContent().get(position).getContent()+"");
                startActivity(new Intent(HelpActivity.this,HelpDetailActivity.class));
            }
        });
    }

    @Override
    protected void initView() {
        loadingDialog = new LoadingDialog(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpActivity.this.finish();
            }
        });

        telphoneTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              startActivity(new Intent(HelpActivity.this,ContactActivity.class));

            }
        });

        feedbackTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HelpActivity.this, FeedbackActivity.class));
            }
        });

    }

    @Override
    protected int bindLayoutId() {
        return R.layout.activity_help;
    }


}
