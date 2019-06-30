package com.jiaoerzhang.platform.ui.activity.user;

import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.model.Token;
import com.wang.avi.AVLoadingIndicatorView;
import com.jiaoerzhang.platform.R;
import com.jiaoerzhang.platform.adapter.user.RechareItemAdapter;
import com.jiaoerzhang.platform.api.UserApi;
import com.jiaoerzhang.platform.api.UserApiService;
import com.jiaoerzhang.platform.common.Constants;
import com.jiaoerzhang.platform.entity.user.CoinEntity;
import com.jiaoerzhang.platform.entity.user.PayMethod;
import com.jiaoerzhang.platform.entity.user.RechargeCoinEntity;
import com.jiaoerzhang.platform.entity.user.RechargeEntity;
import com.jiaoerzhang.platform.lib_core.base.BaseActivity;
import com.jiaoerzhang.platform.lib_net.network.BaseResponse;
import com.jiaoerzhang.platform.lib_net.network.Response;
import com.jiaoerzhang.platform.ui.activity.pay.CardPayActivity;
import com.jiaoerzhang.platform.utils.AlbumRetrofitUtils;
import com.jiaoerzhang.platform.utils.ClickProxy;
import com.jiaoerzhang.platform.utils.RetrofitUtils;
import com.jiaoerzhang.platform.utils.TimeUtils;
import com.jiaoerzhang.platform.widget.LoadingDialog;
import com.jiaoerzhang.platform.widget.RechargeDialog;
import com.jiaoerzhang.platform.widget.RechargeLoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RechargeActivity extends BaseActivity implements RechargeDialog.OnCloseListener {


    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.coin_history)
    ImageView coin_history;

    @BindView(R.id.price1)
    TextView price1;
    @BindView(R.id.price2)
    TextView price2;

    @BindView(R.id.price3)
    TextView price3;

    @BindView(R.id.lc1)
    TextView lc1;
    @BindView(R.id.lc2)
    TextView lc2;
    @BindView(R.id.lc3)
    TextView lc3;

    @BindView(R.id.price1Layout)
    LinearLayout price1Layout;
    @BindView(R.id.price2Layout)
    LinearLayout price2Layout;
    @BindView(R.id.price3Layout)
    LinearLayout price3Layout;

    private int pos;//记录选中哪项档位

    @BindView(R.id.pay)
    Button pay;
    @BindView(R.id.cancel)
    Button cancel;

    @BindView(R.id.card_num)
    EditText card_num;
    @BindView(R.id.expiry_month)
    EditText expiry_month;
    @BindView(R.id.expiry_year)
    EditText expiry_year;


    @BindView(R.id.cvv_cvc)
    EditText cvv_cvc;
    @BindView(R.id.email)
    EditText email;

    @BindView(R.id.creditCard)
    TextView creditCard;

    @BindView(R.id.tv_stripe)
    TextView stripeTv;
    @BindView(R.id.tv_masapay)
    TextView masapayTv;

    @BindView(R.id.checkbox_stripe)
    ImageView stripeIv;
    @BindView(R.id.checkbox_masapay)
    ImageView masapayIv;

    private boolean isStripe = true;

    TextView view_count;

    private LoadingDialog loadingDialog;


    @OnClick(R.id.price1Layout)
    public void layout1(View v) {
        selectCoin(0);
    }

    @OnClick(R.id.price2Layout)
    public void layout2(View v) {
        selectCoin(1);
    }

    @OnClick(R.id.price3Layout)
    public void layout3(View v) {
        selectCoin(2);
    }




    private String from = "";

    private List<PayMethod.Content> payList;

    @OnClick(R.id.stripe_layout)
    public void stripeClick(View view){
        isStripe = true;
        stripeIv.setImageResource(R.mipmap.recharge_checked_icon);
        masapayIv.setImageResource(R.mipmap.recharge_nochecked_icon);

    }
    @OnClick(R.id.masapay_layout)
    public void masapayClick(View view){
        isStripe = false;
        stripeIv.setImageResource(R.mipmap.recharge_nochecked_icon);
        masapayIv.setImageResource(R.mipmap.recharge_checked_icon);

    }


    @Override
    protected void initData() {
        if (!TextUtils.isEmpty(SPUtils.getInstance().getString("card"))) {
            card_num.setText(SPUtils.getInstance().getString("card"));
        }
        if (!TextUtils.isEmpty(SPUtils.getInstance().getString("year"))) {
            expiry_year.setText(SPUtils.getInstance().getString("year"));
        }
        if (!TextUtils.isEmpty(SPUtils.getInstance().getString("month"))) {
            expiry_month.setText(SPUtils.getInstance().getString("month"));
        }

        from = getIntent().getExtras().getString("from");
        payList = new ArrayList<>();
        requestCoinList();
        requestPaymethod();


    }

    /**
     * 支付方式
     */
    private void requestPaymethod() {
        AlbumRetrofitUtils.getInstance().createService(UserApiService.class)
                .getPaymethod(UserApi.PAY_STYLE_URL,new HashMap<String, String>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse<PayMethod>>() {
                    @Override
                    public void accept(BaseResponse<PayMethod> payMethodBaseResponse) throws Exception {

                        payList = payMethodBaseResponse.getData().getContent();
                        fillPaymethodData();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    /**
     * 填充支付方式数据
     */
    private void fillPaymethodData() {
        stripeTv.setText(payList.get(0).getPayStylePrefix());
        masapayTv.setText(payList.get(1).getPayStylePrefix());
    }

    /**
     * @param i
     */
    private void selectCoin(int i) {
        pos = i;

        switch (i) {
            case 0:
                price1Layout.setBackgroundResource(R.drawable.coin_selected_bg);
                price2Layout.setBackgroundResource(R.drawable.coin_select_normal_bg);
                price3Layout.setBackgroundResource(R.drawable.coin_select_normal_bg);
                view_count.setText(list.get(0).getNotes());
                break;
            case 1:
                price1Layout.setBackgroundResource(R.drawable.coin_select_normal_bg);
                price2Layout.setBackgroundResource(R.drawable.coin_selected_bg);
                price3Layout.setBackgroundResource(R.drawable.coin_select_normal_bg);
                view_count.setText(list.get(1).getNotes());
                break;
            case 2:
                price1Layout.setBackgroundResource(R.drawable.coin_select_normal_bg);
                price2Layout.setBackgroundResource(R.drawable.coin_select_normal_bg);
                price3Layout.setBackgroundResource(R.drawable.coin_selected_bg);
                view_count.setText(list.get(2).getNotes());
                break;
        }

    }

    /**
     * 充值档位
     */
    private void requestCoinList() {

        RetrofitUtils.getInstance().createService(UserApiService.class)
                .getRechareCoinList(UserApi.PAYGRADE_URL).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse<List<RechargeCoinEntity>>>() {
                    @Override
                    public void accept(BaseResponse<List<RechargeCoinEntity>> listBaseResponse) throws Exception {

                        fillData(listBaseResponse.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });

    }

    private List<RechargeCoinEntity> list;

    /**
     * 绘制列表
     *
     * @param data
     */
    private void fillData(List<RechargeCoinEntity> data) {

        list = data;

        price1.setText(data.get(0).getName());
        price2.setText(data.get(1).getName());
        price3.setText(data.get(2).getName());
        lc1.setText(data.get(0).getLadyCoin() + "(LC)");
        lc2.setText(data.get(1).getLadyCoin() + "(LC)");
        lc3.setText(data.get(2).getLadyCoin() + "(LC)");
        view_count.setText(data.get(0).getNotes());

    }

    @Override
    protected void initView() {
        view_count = findViewById(R.id.view_count);
        loadingDialog = new LoadingDialog(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RechargeActivity.this.finish();
            }
        });
        coin_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RechargeActivity.this, RechargeHistoryActivity.class);
                startActivity(intent);
            }
        });


        pay.setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("isstripe===="+isStripe);
                if (isStripe){
                    requestStripeToken();
                }else{
                    recharge(1,"");//maspay方式
                }

            }
        }));

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.showShort("no pay");
                RechargeActivity.this.finish();
            }
        });


        card_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
//                if (s.length()==4||s.length()==9||s.length()==14){
//                    s.append("-");
//                }

            }
        });


        stripeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStripe = true;
                stripeIv.setImageResource(R.mipmap.recharge_checked_icon);
                masapayIv.setImageResource(R.mipmap.recharge_nochecked_icon);
            }
        });

        masapayIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStripe = false;
                stripeIv.setImageResource(R.mipmap.recharge_nochecked_icon);
                masapayIv.setImageResource(R.mipmap.recharge_checked_icon);
            }
        });

//        creditCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(RechargeActivity.this, CardPayActivity.class));
//            }
//        });



    }

    /**
     * 请求stripe的token
     */
    private void requestStripeToken() {
        if (card_num.getText().toString().length() == 0) {
            ToastUtils.showShort(R.string.card_num_empty);
            return;
        }
        if (expiry_year.getText().toString().length() == 0) {
            ToastUtils.showShort(R.string.expiry_year_empty);
            return;
        }
        if (expiry_year.getText().toString().length() != 4) {
            ToastUtils.showShort(R.string.expiry_year_standard);
            return;
        }
        if (expiry_month.getText().toString().length() == 0) {
            ToastUtils.showShort(R.string.expiry_month_empty);
            return;
        }
        if (expiry_month.getText().toString().length() != 2) {
            ToastUtils.showShort(R.string.expiry_month_standard);
            return;
        }

        if (cvv_cvc.getText().toString().length() == 0) {
            ToastUtils.showShort(R.string.cvv_cvc_empty);
            return;
        }
        if (cvv_cvc.getText().toString().length() != 3) {
            ToastUtils.showShort(R.string.cvv_cvc_standard);
            return;
        }
        Card.Builder builder = new Card.Builder(card_num.getText().toString(),Integer.parseInt(expiry_month.getText().toString()),Integer.parseInt(expiry_year.getText().toString()),cvv_cvc.getText().toString());
        Card card = builder.build();

        if (card.validateCard()) {
            Stripe stripe = new Stripe(RechargeActivity.this);
            //调用创建token方法
            stripe.createToken(
                    card,//传入card对象
                    Constants.RELEASE_KEY,//可发布的密钥
                    new TokenCallback() {
                        //这里的token打印出来是一串json数据,其中的    token需要用getId()来得到
                        public void onSuccess(Token token) {
                            System.out.println("token=========="+token.getId());//
                            // 这里生成得到了token,你需要将它发送到自己服务器,然后服务器利用这个token和支付金额去向    Stripe请求扣费
//                            submitPaymentInfo(token.getId(),"12.20");//提交支付信息
                            recharge(0,token.getId());

                        }
                        public void onError(Exception error) {
                            // 显示本地错误信息
                            Toast.makeText(RechargeActivity.this,
                                    error+"",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
            );
        }else{//卡号有误
            ToastUtils.showShort("The card number that you entered is invalid");
        }
//        if (!card.validateExpiryDate()) {//过期时间有误
//            ToastUtils.showShort("The expiration date that you entered is invalid");
//        } else if (!card.validateCVC()) {//CVC验证码有误
//            ToastUtils.showShort("The CVC code that you entered is invalid");
//        } else {//卡片详情有误
//            ToastUtils.showShort("The card details that you entered are invalid");
//        }
    }

    @Override
    protected int bindLayoutId() {
        return R.layout.activity_recharge;
    }

    private void recharge(int i,String token) {

//        if (TimeUtils.isFastClick())//快速点击
//        {
//            return;
//        }


        if (card_num.getText().toString().length() == 0) {
            ToastUtils.showShort(R.string.card_num_empty);
            return;
        }
        if (expiry_year.getText().toString().length() == 0) {
            ToastUtils.showShort(R.string.expiry_year_empty);
            return;
        }
        if (expiry_year.getText().toString().length() != 4) {
            ToastUtils.showShort(R.string.expiry_year_standard);
            return;
        }
        if (expiry_month.getText().toString().length() == 0) {
            ToastUtils.showShort(R.string.expiry_month_empty);
            return;
        }
        if (expiry_month.getText().toString().length() != 2) {
            ToastUtils.showShort(R.string.expiry_month_standard);
            return;
        }

        if (cvv_cvc.getText().toString().length() == 0) {
            ToastUtils.showShort(R.string.cvv_cvc_empty);
            return;
        }
        if (cvv_cvc.getText().toString().length() != 3) {
            ToastUtils.showShort(R.string.cvv_cvc_standard);
            return;
        }
        if (email.getText().toString().length() == 0) {
            ToastUtils.showShort(R.string.email_empty);
            return;
        }

        if (!RegexUtils.isEmail(email.getText().toString())) {
            ToastUtils.showShort(R.string.email_standard);
            return;
        }

        SPUtils.getInstance().put("card", card_num.getText().toString());
        SPUtils.getInstance().put("year", expiry_year.getText().toString());
        SPUtils.getInstance().put("month", expiry_month.getText().toString());

        loadingDialog.show();

        CoinEntity coinEntity = new CoinEntity(
                SPUtils.getInstance().getString("username"),
                SPUtils.getInstance().getString("deviceFingerId"), list.get(pos).getCode()
                , card_num.getText().toString()
                , expiry_year.getText().toString()
                , expiry_month.getText().toString()
                , cvv_cvc.getText().toString()
                , email.getText().toString()
                ,payList.get(i).getPayStylePrefix()
                ,token
                , "test"

        );

        System.out.println("gsoncoin====" + new Gson().toJson(coinEntity));



        RetrofitUtils.getInstance().createService(UserApiService.class)
                .rechargeCoin(UserApi.RECHARGE_COIN_URL, coinEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response>() {
                    @Override
                    public void accept(Response response) throws Exception {

                        ToastUtils.showShort(response.getMessage() + "");
                        if (response.getStatus().equals("200")) {
                            Intent intent = new Intent(RechargeActivity.this, PaySuccessActivity.class);
                            intent.putExtra("coin", list.get(pos).getPrice());
                            startActivity(intent);
                            RechargeActivity.this.finish();
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

    @Override
    public void onRechargeClick(Dialog dialog, boolean confirm) {

        if (confirm) {
            loadingDialog.dismiss();

        }
    }
}
