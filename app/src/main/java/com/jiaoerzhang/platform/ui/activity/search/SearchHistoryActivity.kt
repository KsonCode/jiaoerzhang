package com.jiaoerzhang.platform.ui.activity.searchimport android.app.PendingIntent.getActivityimport android.content.Intentimport android.support.v7.widget.LinearLayoutManagerimport android.view.Viewimport com.blankj.utilcode.util.SPUtilsimport com.chad.library.adapter.base.BaseQuickAdapterimport com.jcodecraeer.xrecyclerview.XRecyclerViewimport com.library.flowlayout.FlowLayoutManagerimport com.jiaoerzhang.platform.Rimport com.jiaoerzhang.platform.adapter.album.AlbumAdapterimport com.jiaoerzhang.platform.adapter.album.SearchAdapterimport com.jiaoerzhang.platform.adapter.album.SearchHistoryAdapterimport com.jiaoerzhang.platform.adapter.user.AccountProblemAdapterimport com.jiaoerzhang.platform.api.AlbumApiimport com.jiaoerzhang.platform.entity.album.AlbumEntityimport com.jiaoerzhang.platform.lib_core.base.BaseActivityimport com.jiaoerzhang.platform.lib_net.network.http.HttpRequestPresenterimport com.jiaoerzhang.platform.lib_net.network.http.ModelCallbackimport com.jiaoerzhang.platform.ui.activity.album.AlbumDetailActivityimport com.jiaoerzhang.platform.ui.activity.user.HelpDetailActivityimport com.jiaoerzhang.platform.widget.LoadingDialogimport kotlinx.android.synthetic.main.activity_search_history.*class SearchHistoryActivity : BaseActivity(), XRecyclerView.LoadingListener {//    var loadingDialog:LoadingDialog? = null    override fun onLoadMore() {        page++        requestData()    }    override fun onRefresh() {        page = 1        requestData()    }    var keyword: String? = null    var page: Int = 1    var pageSize = 10    var albumAdapter: SearchAdapter? = null    override fun initData() {        keyword = intent.extras.getString("keyword")        search_history.text = keyword.toString()        requestData()    }    private fun requestData() {        var params: HashMap<String, String> = HashMap()        params["name"] = keyword.toString()        params["page"] = page.toString()        params["pageSize"] = pageSize.toString()        params["telPhone"] = SPUtils.getInstance().getString("username")//5个        var key: String = "{\"name\":\"$keyword\"}"//        loadingDialog!!.show()        HttpRequestPresenter.getInstance().jsonPostData(AlbumApi.SEARCH_ALBUM_URL, params, key, object : ModelCallback<AlbumEntity>(AlbumEntity::class.java) {            override fun onSuccess(t: AlbumEntity?) {                println("tsize==" + t!!.content.size)                if (page == 1) {                    albumAdapter = SearchAdapter(this@SearchHistoryActivity, t!!.content)                    albumRv.adapter = albumAdapter                    albumRv.refreshComplete()                } else {                    albumAdapter!!.refreshData(t!!.content)                    albumRv.loadMoreComplete()                }//                loadingDialog!!.dismiss()            }            override fun onSuccessMsg(status: String?, message: String?) {//                loadingDialog!!.dismiss()            }            override fun onErrorMsg(code: Int, msg: String?) {//                loadingDialog!!.dismiss()            }        })         }    override fun initView() {//        loadingDialog = LoadingDialog(this)        back.setOnClickListener { this.finish() }        albumRv.layoutManager = LinearLayoutManager(this)        albumRv.setLoadingListener(this)        albumRv.setLoadingMoreEnabled(true)    }    override fun bindLayoutId(): Int {        return R.layout.activity_search_history    }}