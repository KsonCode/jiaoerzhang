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
import com.blankj.utilcode.util.*
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideOption
import com.bumptech.glide.request.RequestOptions
import com.github.chrisbanes.photoview.PhotoView
import com.gyf.barlibrary.ImmersionBar
import com.jiaoerzhang.platform.R
import com.jiaoerzhang.platform.api.AlbumApiService
import com.jiaoerzhang.platform.api.UserApi
import com.jiaoerzhang.platform.api.UserApiService
import com.jiaoerzhang.platform.common.Constants
import com.jiaoerzhang.platform.entity.album.AlbumBean
import com.jiaoerzhang.platform.entity.album.PhotoEntity
import com.jiaoerzhang.platform.entity.user.CoinEntity
import com.jiaoerzhang.platform.lib_net.network.Response
import com.jiaoerzhang.platform.ui.activity.user.PaySuccessActivity
import com.jiaoerzhang.platform.ui.activity.user.RechargeActivity
import com.jiaoerzhang.platform.utils.AlbumRetrofitUtils
import com.jiaoerzhang.platform.utils.RetrofitUtils
import com.jiaoerzhang.platform.widget.LoadingDialog
import com.jiaoerzhang.platform.widget.PayDialog
import com.jiaoerzhang.platform.widget.RechargeDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.album_detail_layout.*

/**
 * 精品专辑详情
 */
class AlbumDetailActivity : AppCompatActivity(), PayDialog.OnCloseListener {
   var isAlbum:Boolean = true

    private var payDialog: PayDialog? = null
    private var action: Int = 12
    private var coin: Int? = null
    private var pos:Int = 0//图片位置
    private var loadingDialog:LoadingDialog? = null
    override fun onClick(dialog: Dialog?, confirm: Boolean) {
        if (confirm) {
            if (NetworkUtils.isConnected()) {
                if (payDialog!!.confirmTv.text.toString().equals("RECHARGE")){

                    startActivity(Intent(this@AlbumDetailActivity,RechargeActivity::class.java))

                }else{
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
     * 购买
     */
    private fun rechargeCoin() {

        val params = java.util.HashMap<String, Any>()
        params["username"] = SPUtils.getInstance().getString("username") + ""
        if (payDialog!!.isAlbum) {
            params["action"] = 12//专辑
            params["albumPhotoId"] = albumId.toString()//专辑id
        } else {
            params["action"] = 11//图片
            params["albumPhotoId"] = this!!.photoList!![pos].id.toString()//当前图片id
        }

        RetrofitUtils.getInstance().createService(UserApiService::class.java)
                .costCoin(UserApi.COST_COIN_URL, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ coinEntity ->
                    ToastUtils.showShort(coinEntity.message)

                    if (coinEntity.status.equals("200")){
                        payDialog!!.dismiss()
                    }



                }, { })

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

    var photoList:List<PhotoEntity.Data.Content>? = listOf()
    fun initData() {

        albumId = intent.extras.getString("albumId")

        isBuy()

        var hashMap: HashMap<String, String> = HashMap()
        hashMap.put("page", "1")
        hashMap.put("pageSize", "2000")
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
                            photoList = it.data.content
                            isFirstPicBuy()
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
                                            LayoutInflater.from(this@AlbumDetailActivity).inflate(R.layout.item_big_img, null)
                                    val icon = adView.findViewById(R.id.flaw_img) as PhotoView
                                    icon.setBackgroundColor(resources.getColor(R.color.black))
                                    var  options = RequestOptions()
                                            .placeholder(R.drawable.placeholder)
                                    Glide.with(this@AlbumDetailActivity)
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
                                    pos = position
                                    action = 11
                                    bar_title.text = (position + 1).toString() + "/" + it.data.content!!.size
                                    val params = java.util.HashMap<String, String>()
                                    params["username"] = SPUtils.getInstance().getString("username")
                                    params["action"] = action.toString()
                                    params["albumPhotoId"] = it.data.content.get(position).id.toString()



                                    RetrofitUtils.getInstance().createService(UserApiService::class.java)
                                            .buyed(UserApi.ISBUYED_URL, params)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe({ buyedEntity ->
                                                if ("200" == buyedEntity.status) {
                                                    if (buyedEntity.data.buy.equals("false")) {
                                                        photoPrice = buyedEntity.data.photoPrice;
                                                        payDialog!!.show()
                                                        payDialog!!.balanceTv.text = "Lady Coin Balance:${buyedEntity.data.ladyCoin}"
                                                        payDialog!!.singlePaymentTv.text = buyedEntity.data.photoPrice.toString()+" LC"
                                                        payDialog!!.albumPaymentTv.text = buyedEntity.data.albumPrice.toString()+" LC"
                                                        setConfirm(buyedEntity.data.isEnough)

                                                    }
                                                }
                                            }, { })

                                }

                                override fun onPageScrollStateChanged(state: Int) {

                                }

                            })

                            big_img_vp.setCurrentItem(position, true)
                        },
                        {

                            throwable -> println("throwable:$throwable")
                            loadingDialog!!.dismiss()})


    }

    var albumPrice:String = ""
    var photoPrice:String = ""
    /**
     * 是否购买
     */
    private fun isBuy() {
        val params = java.util.HashMap<String, String>()
        params["username"] = SPUtils.getInstance().getString("username")
        params["action"] = action.toString()
        params["albumPhotoId"] = albumId.toString()


        RetrofitUtils.getInstance().createService(UserApiService::class.java).buyed(UserApi.ISBUYED_URL, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ buyedEntity ->
                    if ("200" == buyedEntity.status) {
                        if (buyedEntity.data.buy.equals("false") ) {
                            albumPrice = buyedEntity.data.albumPrice
                            payDialog!!.show()
                            payDialog!!.balanceTv.text = "Lady Coin Balance:"+buyedEntity.data.ladyCoin
                            payDialog!!.albumPaymentTv.text = buyedEntity.data.albumPrice.toString()+" LC"
                            payDialog!!.singlePaymentTv.text = buyedEntity.data.photoPrice.toString()+" LC"
                            //金币不足
                            setConfirm(buyedEntity.data.isEnough)


                        }
                    }
                }, { })
    }


    /**
     * 第一张图片的信息
     */
    private fun isFirstPicBuy() {
        val params = java.util.HashMap<String, String>()
        params["username"] = SPUtils.getInstance().getString("username")
        params["action"] = 11.toString()
        params["albumPhotoId"] = this!!.photoList!![pos].id!!.toString()


        RetrofitUtils.getInstance().createService(UserApiService::class.java).buyed(UserApi.ISBUYED_URL, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ buyedEntity ->
                    if ("200" == buyedEntity.status) {
                        if (buyedEntity.data.buy.equals("false") ) {
                            payDialog!!.show()
                            payDialog!!.balanceTv.text = "Lady Coin Balance:"+buyedEntity.data.ladyCoin
                            payDialog!!.singlePaymentTv.text = buyedEntity.data.photoPrice.toString()+" LC"
                           payDialog!!.albumPaymentTv.text = buyedEntity.data.albumPrice.toString()+" LC"
                            setConfirm(buyedEntity.data.isEnough)

                        }else{
                            payDialog!!.dismiss()
                        }
                    }
                    isBuy()
                }, { })
    }


    private fun initView() {
        loadingDialog = LoadingDialog(this)
        payDialog = PayDialog(this)
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

    /**
     * 余额是否充足
     */
    fun setConfirm(isEnough:String){
        if (isEnough.equals("false")){
            payDialog!!.confirmTv.text = "RECHARGE"
            payDialog!!.notEnough.visibility = View.VISIBLE
        }else{
            payDialog!!.confirmTv.text = "CONFIRM"
            payDialog!!.notEnough.visibility = View.GONE
        }

    }




}


