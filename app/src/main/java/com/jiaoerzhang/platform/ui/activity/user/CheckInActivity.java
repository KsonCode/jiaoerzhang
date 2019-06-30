package com.jiaoerzhang.platform.ui.activity.user;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.test.sign_calender.DPCManager;
import com.test.sign_calender.DPDecor;
import com.test.sign_calender.DatePicker;
import com.test.sign_calender.DatePicker2;
import com.jiaoerzhang.platform.R;
import com.jiaoerzhang.platform.api.UserApi;
import com.jiaoerzhang.platform.api.UserApiService;
import com.jiaoerzhang.platform.entity.user.SignEntity;
import com.jiaoerzhang.platform.lib_core.base.BaseActivity;
import com.jiaoerzhang.platform.lib_net.network.BaseResponse;
import com.jiaoerzhang.platform.lib_net.network.Response;
import com.jiaoerzhang.platform.utils.RetrofitUtils;
import com.jiaoerzhang.platform.widget.LoadingDialog;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CheckInActivity extends BaseActivity {

    @BindView(R.id.check_in)
    Button check_in;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.check_success)
    ImageView check_success;

    @BindView(R.id.pickerCardView)
    CardView pickerCardview;

    private LoadingDialog loadingDialog;

    List<String> tmp = new ArrayList<>();

    @Override
    protected void initData() {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", SPUtils.getInstance().getString("username"));

        loadingDialog.show();
        RetrofitUtils.getInstance().createService(UserApiService.class)
                .signDates(UserApi.SIGN_DATE_URL, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse<SignEntity>>() {
                    @Override
                    public void accept(BaseResponse<SignEntity> response) throws Exception {
                       loadingDialog.dismiss();
                        if ("200".equals(response.getStatus())) {
                            String dates = response.getData().getSignDates();
                            System.out.println("dates======" + dates);
                            if (!TextUtils.isEmpty(dates)) {
                                String[] strs = dates.split(",");
                                System.out.println("strs:" + dates);
                                tmp = Arrays.asList(strs);

                                System.out.println("tempsize：" + tmp.size());



                            }

                        } else {
                            showToast(response.getMessage());
                        }

                        myCalendar();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        loadingDialog.dismiss();
                    }
                });

    }

    @Override
    protected void initView() {
        loadingDialog = new LoadingDialog(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckInActivity.this.finish();
            }
        });
        check_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingDialog.show();
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("username", SPUtils.getInstance().getString("username"));

                RetrofitUtils.getInstance().createService(UserApiService.class)
                        .sign(UserApi.SIGN_URL, params)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Response>() {
                            @Override
                            public void accept(Response response) throws Exception {

                                loadingDialog.dismiss();
                                showToast(response.getMessage());

                                if ("200".equals(response.getStatus())) {
                                    check_success.setVisibility(View.VISIBLE);
                                    check_in.setEnabled(false);
                                    check_in.setBackgroundResource(R.drawable.checkin_gray_bg);

                                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(check_success, "alpha", 1f, 0f);
                                    objectAnimator.setDuration(2000);
                                    objectAnimator.start();
                                    objectAnimator.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {

                                            check_success.setVisibility(View.GONE);

                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animation) {

                                        }
                                    });
                                    gainCoin();
                                } else {
                                    check_success.setVisibility(View.GONE);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                                loadingDialog.dismiss();
                            }
                        });
            }
        });

    }

    /**
     * 获取金币
     */
    private void gainCoin() {

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("username", SPUtils.getInstance().getString("username"));
        params.put("comment", "sign");
        params.put("action", "2");//签到


        RetrofitUtils.getInstance().createService(UserApiService.class)
                .gainCoin(UserApi.GAIN_COIN_URL, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response>() {
                    @Override
                    public void accept(Response response) throws Exception {
                        showToast(response.getMessage());
                        if ("200".equals(response.getStatus())) {


                        } else {

                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });

    }


    @Override
    protected int bindLayoutId() {
        return R.layout.activity_check_in;
    }


    private void myCalendar() {



        DatePicker2 picker = new DatePicker2(this);
        pickerCardview.addView(picker);

        picker.setFestivalDisplay(true); //是否显示节日
        picker.setHolidayDisplay(true); //是否显示假期
        picker.setDeferredDisplay(true); //是否显示补休
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();//首先要获取日历对象
        int mYear = c.get(Calendar.YEAR); // 获取当前年份
        final int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
        Date date=c.getTime();
        String currentData = df.format(date);
        System.out.println("currdate==="+currentData);
        picker.setDate(mYear, mMonth);
        if (tmp!=null&&tmp.size()>0){
            for (String s : tmp) {
                if (s.contains(currentData)){

                    check_in.setEnabled(false);
                    check_in.setBackgroundResource(R.drawable.checkin_gray_bg);
                }

            }
            DPCManager.getInstance().setDecorBG(tmp);
            picker.setDPDecor(new DPDecor() {
                @Override
                public void drawDecorBG(Canvas canvas, Rect rect, Paint paint) {
                    paint.setColor(Color.RED);
                    //                paint.setStyle(Paint.Style.STROKE);
                    //                paint.setTextAlign(Paint.Align.CENTER);
                    //                paint.setTextSize(16);
                    paint.setAntiAlias(true);
                    InputStream is = getResources().openRawResource(R.mipmap.red_circle);
                    Bitmap mBitmap = BitmapFactory.decodeStream(is);
                    canvas.drawBitmap(mBitmap, rect.centerX() - mBitmap.getWidth() / 2f, rect.centerY() - mBitmap.getHeight() / 2f, paint);
                }
            });
        }


        picker.setOnDateSelectedListener(new DatePicker.OnDateSelectedListener() {
            @Override
            public void onDateSelected(List<String> date) {
                String result = "";
                Iterator iterator = date.iterator();
                while (iterator.hasNext()) {
                    result += iterator.next();
                    if (iterator.hasNext()) {
                        result += "\n";
                    }
                }
                Toast.makeText(CheckInActivity.this, result, Toast.LENGTH_LONG).show();
            }
        });


    }
}
