package com.jiaoerzhang.platform.ui.fragment


import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.blankj.utilcode.util.SPUtils
import com.lzj.gallery.library.views.BannerViewPager
import com.scwang.smartrefresh.header.WaterDropHeader
import com.jiaoerzhang.platform.R
import com.jiaoerzhang.platform.contract.HomeContract
import com.jiaoerzhang.platform.lib_core.base.mvp.BaseMvpFragment
import com.jiaoerzhang.platform.lib_core.base.mvp.BasePresenter
import com.jiaoerzhang.platform.presenter.HomePresenter
import kotlinx.android.synthetic.main.fragment_home_layout.*
import kotlinx.android.synthetic.main.main_header_layout.*
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.jiaoerzhang.platform.adapter.home.HomeItemAdapter
import com.jiaoerzhang.platform.adapter.home.HomeTopAdapter
import com.jiaoerzhang.platform.api.HomeApi
import com.jiaoerzhang.platform.app.App
import com.jiaoerzhang.platform.common.Constants
import com.jiaoerzhang.platform.entity.home.HomeEntity
import com.jiaoerzhang.platform.entity.home.ModelEntity
import com.jiaoerzhang.platform.lib_net.network.http.HttpRequestPresenter
import com.jiaoerzhang.platform.lib_net.network.http.ModelCallback
import com.jiaoerzhang.platform.ui.activity.album.AlbumDetailActivity
import com.jiaoerzhang.platform.ui.activity.album.ModelTopsActivity
import com.jiaoerzhang.platform.ui.activity.album.NormalAlbumDetailActivity
import com.jiaoerzhang.platform.ui.activity.search.SearchActivity
import kotlinx.android.synthetic.main.activity_main.*


/**
 * 首页
 */
class HomeFragment : BaseMvpFragment<HomeContract.IhomeModel, HomeContract.HomePresenter>(), HomeContract.IhomeView {
    var page:Int = 1
    var pageSize:Int = 100



    override fun onFirstUserVisible() {
    }

    override fun Sucess(homeEntity: HomeEntity) {
        loadDatas(homeEntity)
    }

    /**
     * 加载数据
     */
    private fun loadDatas(homeEntity: HomeEntity) {
        var topList: MutableList<HomeEntity.Content> = ArrayList<HomeEntity.Content>()
        var rvList: MutableList<HomeEntity.Content> = ArrayList<HomeEntity.Content>()
        var albumList: MutableList<HomeEntity.Content> = ArrayList<HomeEntity.Content>()
        var bannerList: MutableList<HomeEntity.Content> = ArrayList<HomeEntity.Content>()
        var bannerString: MutableList<String> = ArrayList<String>()
        var albumString: MutableList<String> = ArrayList<String>()


        if (homeEntity.content!=null&&homeEntity.content.size>0){
            for (content in homeEntity.content) {
                when {
                    content.type == 1 -> topList.add(content)
                    content.type == 4 -> rvList.add(content)
                    content.type == 5 -> //banner
                        bannerList.add(content)
                    content.type == 2 -> albumList.add(content)//精品列表
                }
            }

            //底部列表
            var homeItemAdapter:HomeItemAdapter? = null
            if (page==1){
                itemRv.layoutManager = GridLayoutManager(activity, 2)
                homeItemAdapter = HomeItemAdapter(activity,R.layout.home_item_layout, rvList)
                itemRv.adapter = homeItemAdapter
                itemRv.isNestedScrollingEnabled = false
                itemRv.setHasFixedSize(true)
            }else{
                if (homeEntity.content.size>0){
                    if (itemRv.adapter!=null){
                        homeItemAdapter!!.setNewData(homeEntity.content)
                    }
                }

            }

            latest.setOnClickListener {
                //            var homeTopAdapter: HomeTopAdapter = HomeTopAdapter(R.layout.home_top_item_layout, topList)
//            topRv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//            topRv.adapter = homeTopAdapter
                hottest.setTextColor(ContextCompat.getColor(App.context,R.color.common_hint_txt_color))
                latest.setTextColor(ContextCompat.getColor(App.context,R.color.common_txt_color))
                itemRv.layoutManager = GridLayoutManager(activity, 2)
                itemRv.adapter = HomeItemAdapter(activity,R.layout.home_item_layout, rvList)
                itemRv.isNestedScrollingEnabled = false
                itemRv.setHasFixedSize(true)
            }
            hottest.setOnClickListener {
                hottest.setTextColor(ContextCompat.getColor(App.context,R.color.common_txt_color))
                latest.setTextColor(ContextCompat.getColor(App.context,R.color.common_hint_txt_color))
                itemRv.layoutManager = GridLayoutManager(activity, 2)
                itemRv.adapter = HomeItemAdapter(activity,R.layout.home_item_layout, topList)
                itemRv.isNestedScrollingEnabled = false
                itemRv.setHasFixedSize(true)
            }
            if (bannerList.size>0)
            for (content in bannerList) {
                bannerString.add(content.coverUrl)
            }

            if (albumList.size>0)
            for (content in albumList) {

                albumString.add(content.coverUrl)

            }


            if (bannerString.size>0)
                if (bannerLayout!=null){
                    bannerLayout.removeAllViews()
                }
            bannerLayout!!.initBanner(bannerString, false, false)//isGallery参数是否开启3D画廊效果
                    .addPageMargin(10, 0)//参数1page之间的间距,参数2中间item距离边界的间距
                    .addPoint(6)//添加指示器
                    .addPointBottom(7)
                    .addStartTimer(5)//自动轮播5秒间隔
                    .addRoundCorners(7)//圆角
                    .finishConfig()//这句必须加
                    .addBannerListener(object: BannerViewPager.OnClickBannerListener {
                        override fun onBannerClick(position: Int) {
                            var intent: Intent? = null
                            if (bannerList.get(position).clarity == Constants.ORDINARY_CLARITY) {//普通
                                intent = Intent(context, NormalAlbumDetailActivity::class.java)
                            } else if (bannerList.get(position).clarity == Constants.POPULAR_CLARITY) {
                                intent = Intent(context, AlbumDetailActivity::class.java)
                            }
                            intent!!.putExtra("albumId",bannerList.get(position).albumId +"")
                            context!!.startActivity(intent)
                        }

                    })

            if (albumString.size>0){
                if (albumLayout!=null){
                    albumLayout.removeAllViews()
                }
                albumLayout!!.initBanner(albumString, true, true)//isGallery参数是否开启3D画廊效果
                        .addPageMargin(1, 40)//参数1page之间的间距,参数2中间item距离边界的间距
                        .addPoint(0)//添加指示器
                        .addPointBottom(7)
                        .addStartTimer(5)//自动轮播5秒间隔
                        .addRoundCorners(12)//圆角
                        .finishConfig()//这句必须加
                        .hideIndicator()//隐藏指示器
                        .addBannerListener(object: BannerViewPager.OnClickBannerListener {
                            override fun onBannerClick(position: Int) {
                                var intent: Intent? = null
                                if (albumList.get(position).clarity == Constants.ORDINARY_CLARITY) {//普通
                                    intent = Intent(context, NormalAlbumDetailActivity::class.java)
                                } else if (bannerList.get(position).clarity == Constants.POPULAR_CLARITY) {
                                    intent = Intent(context, AlbumDetailActivity::class.java)
                                }
                                intent!!.putExtra("albumId",albumList.get(position).albumId +"")
                                context!!.startActivity(intent);
                            }

                        })
                albumLayout.stopTimer()
            }

        }

    }

    var params:HashMap<String,String>? = hashMapOf()
    override fun init() {
        params!!["telPhone"] = SPUtils.getInstance().getString("username")
        presenter.getHome(params!!)

        HttpRequestPresenter.getInstance().get(HomeApi.TOP_URL, HashMap<String,String>(),object : ModelCallback<ModelEntity>(ModelEntity::class.java) {
            override fun onSuccess(t: ModelEntity?) {
                var topList: List<ModelEntity.Content> = ArrayList<ModelEntity.Content>()
                topList = t!!.content
                var homeTopAdapter: HomeTopAdapter = HomeTopAdapter(R.layout.home_top_item_layout, topList)
                topRv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                topRv.adapter = homeTopAdapter

                homeTopAdapter.setOnItemClickListener { adapter, view, position ->
                    startActivity(Intent(activity,ModelTopsActivity::class.java))
                }

            }

            override fun onSuccessMsg(status: String?, message: String?) {
            }

            override fun onErrorMsg(code: Int, msg: String?) {
            }

        })


    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_home_layout
    }


    override fun initViewsAndEvents(view: View) {


        //刷新
        smartRefreshlayout.setOnRefreshListener(object : OnRefreshListener {
            override fun onRefresh(refreshlayout: RefreshLayout) {
                page = 1
                smartRefreshlayout.finishRefresh()
                presenter.getHome(params!!)
            }
        })
        //加载更多
//        smartRefreshlayout.setOnLoadMoreListener {
//            smartRefreshlayout.finishLoadMore()
//            page++
//            presenter.getHome(HashMap())
//        }


        //设置 Header 为 Material风格
        smartRefreshlayout.setRefreshHeader(WaterDropHeader(activity));
        search_album.setOnClickListener { startActivity(Intent(activity, SearchActivity::class.java)) }



        model_top_more.setOnClickListener {
            startActivity(Intent(activity,ModelTopsActivity::class.java))
        }

        popularAlbum.setOnClickListener {
//            var intent:Intent = Intent(activity,MainActivity::class.java)
//            intent.putExtra("from","album")
//            startActivity(intent)

                activity!!.bottom_navigation_bar!!.selectTab(1)

        }

    }


    override
    fun onUserVisible() {
        //设置图片集合
//        banner.setImages(images)
    }

    override fun onUserInvisible() {

    }


    override fun DetoryViewAndThing() {

    }


    override fun initPresenter(): BasePresenter<*, *>? {
        return HomePresenter()
    }

}
