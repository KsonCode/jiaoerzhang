package com.jiaoerzhang.platform.ui.activity.album

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.chrisbanes.photoview.PhotoView
import com.gyf.barlibrary.ImmersionBar
import com.jiaoerzhang.platform.R
import com.jiaoerzhang.platform.api.AlbumApiService
import com.jiaoerzhang.platform.api.UserApi
import com.jiaoerzhang.platform.api.UserApiService
import com.jiaoerzhang.platform.common.Constants
import com.jiaoerzhang.platform.entity.album.AlbumBean
import com.jiaoerzhang.platform.entity.user.BvipAlbumEntity
import com.jiaoerzhang.platform.entity.user.IsBvipEntity
import com.jiaoerzhang.platform.lib_net.network.BaseResponse
import com.jiaoerzhang.platform.ui.activity.user.RechargeActivity
import com.jiaoerzhang.platform.utils.AlbumRetrofitUtils
import com.jiaoerzhang.platform.utils.RetrofitUtils
import com.jiaoerzhang.platform.widget.LoadingDialog
import com.jiaoerzhang.platform.widget.NormalPayDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_transaction_details.*
import kotlinx.android.synthetic.main.album_detail_layout.*

/**
 * 普通专辑详情
 */
class NormalAlbumDetailActivity : AppCompatActivity(), NormalPayDialog.OnCloseListener {
    private var payDialog: NormalPayDialog? = null
    private var loadingDialog: LoadingDialog? = null
    override fun onClick(dialog: Dialog?, confirm: Boolean) {
        if (confirm) {
            if (NetworkUtils.isConnected()) {
                if (payDialog!!.confirmTv.text.toString().equals("RECHARGE")) {
                    startActivity(Intent(this, RechargeActivity::class.java))
                } else {
                    rechargeCoin()
                }
            } else {
                ToastUtils.showShort(Constants.NETWORK_ERROT_URL)
            }
        } else {

            this.finish()
        }

    }

    /**
     * bvip购买
     */
    private fun rechargeCoin() {
        loadingDialog!!.show()
        val params = java.util.HashMap<String, Any>()
        params["username"] = SPUtils.getInstance().getString("username") + ""
        params["action"] = payDialog!!.pos

        RetrofitUtils.getInstance().createService(UserApiService::class.java)
                .costCoin(UserApi.COST_COIN_URL, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ coinEntity ->


                    loadingDialog!!.dismiss()
                    ToastUtils.showShort(coinEntity.message)
                    if (coinEntity.status.equals("200")) {
                        if (payDialog!!.isShowing) {
                            payDialog!!.dismiss()
                        }
                    }


                }, { loadingDialog!!.dismiss() })

    }

    private val position: Int = 0
    //    private var paths: ArrayList<String>? = null
    private var albumId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.album_detail_layout)
        initView()
        initData()

    }

    fun initData() {
        isBvip()



        albumId = intent.extras.getString("albumId")


        var hashMap: HashMap<String, String> = HashMap()
        hashMap.put("page", "1")
        hashMap.put("pageSize", "200")
        var albumBean: AlbumBean = AlbumBean()
        albumBean.albumId = albumId

        val params = java.util.HashMap<String, String>()
        params["username"] = SPUtils.getInstance().getString("username")

        loadingDialog!!.show()
        AlbumRetrofitUtils.getInstance().createService(AlbumApiService::class.java)
                .getPhotos(albumBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            loadingDialog!!.dismiss()
                            println("photo:::" + it)
                            bar_title.text = (position + 1).toString() + "/" + it!!.data.content!!.size
                            big_img_vp.adapter = object : PagerAdapter() {
                                override fun getCount(): Int {
                                    return it.data.content?.size
                                }

                                override fun isViewFromObject(view: View, o: Any): Boolean {
                                    return view === o
                                }

                                override fun instantiateItem(container: ViewGroup, position: Int): Any {
                                    val adView =
                                            LayoutInflater.from(this@NormalAlbumDetailActivity).inflate(R.layout.item_big_img, null)
                                    val icon = adView.findViewById(R.id.flaw_img) as PhotoView
                                    icon.setBackgroundColor(resources.getColor(R.color.black))
                                    var  options = RequestOptions()
                                            .placeholder(R.drawable.placeholder)
                                    Glide.with(this@NormalAlbumDetailActivity)
                                            .load(it.data.content!![position].url)
                                            .apply(options)
                                            .into(icon)
                                    container.addView(adView)
                                    return adView
                                }

                                override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                                    container.removeView(`object` as View)
                                }
                            }

                            big_img_vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                                }

                                override fun onPageSelected(position: Int) {

                                    bar_title.text = (position + 1).toString() + "/" + it.data.content!!.size
                                }

                                override fun onPageScrollStateChanged(state: Int) {

                                }

                            })

                            big_img_vp.setCurrentItem(position, true)
                        },
                        { throwable ->
                            println("throwable:$throwable")
                            loadingDialog!!.dismiss()
                        })


    }

    /**
     * 是否购买过bvip
     */
    private fun isBvip() {

        var bvip: HashMap<String, String> = hashMapOf()
        bvip.put("username", SPUtils.getInstance().getString("username"))

        RetrofitUtils.getInstance().createService(UserApiService::class.java)
                .isBvip(UserApi.IS_BVIP_URL, bvip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer<BaseResponse<IsBvipEntity>> {
                    if ("false".equals(it.data.bvip)) {
                        if (!payDialog!!.isShowing) {
                            payDialog!!.show()
                        }
                    }
                    bvip()
                }, Consumer<Throwable> {

                })

    }

    var bvipList: List<BvipAlbumEntity> = arrayListOf()
    /**
     * 获取bvip列表
     */
    private fun bvip() {
        var params: HashMap<String, String> = hashMapOf()
        params.put("username", SPUtils.getInstance().getString("username"))
        RetrofitUtils.getInstance().createService(UserApiService::class.java).bvip(UserApi.BVIP_APPGET_URL, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer<BaseResponse<List<BvipAlbumEntity>>> {
                    bvipList = it!!.data
                    fillBvips()

                }, Consumer<Throwable> {


                })

    }

    /**
     * 填充
     */
    private fun fillBvips() {

        payDialog!!.day1TXT.text = bvipList[0].notes
        payDialog!!.day2TXT.text = bvipList[1].notes
        payDialog!!.day3TXT.text = bvipList[2].notes

        payDialog!!.price1TXT.text = bvipList[0].ladyCoin.toString()
        payDialog!!.price2TXT.text = bvipList[1].ladyCoin.toString()
        payDialog!!.price3TXT.text = bvipList[2].ladyCoin.toString()

        payDialog!!.balanceTv.text = "Lady Coin Balance:" + bvipList[0].balance

        if (bvipList[0].balance < bvipList[0].ladyCoin && bvipList[0].balance < bvipList[1].ladyCoin && bvipList[0].balance < bvipList[2].ladyCoin) {
            payDialog!!.confirmTv.setText("RECHARGE")
            payDialog!!.notEnough.visibility = View.VISIBLE
        } else {
            payDialog!!.confirmTv.setText("CONFIRM")
            payDialog!!.notEnough.visibility = View.GONE
        }


    }


    private fun initView() {

        loadingDialog = LoadingDialog(this)
        payDialog = NormalPayDialog(this)
        payDialog!!.setCancelable(false)
        payDialog!!.setListener(this)
//        payDialog!!.show()

        ImmersionBar.with(this)            //透明状态栏和导航栏，不写默认状态栏为透明色，导航栏为黑色（设置此方法，fullScreen()方法自动为true）
                .statusBarColor(R.color.black).init()
        bar_back.setOnClickListener { this.finish() }

    }

    override fun onDestroy() {
        super.onDestroy()
    }


}


