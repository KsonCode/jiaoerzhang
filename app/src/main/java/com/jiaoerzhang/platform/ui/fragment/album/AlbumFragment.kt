package com.jiaoerzhang.platform.ui.fragment.album

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.jiaoerzhang.platform.R
import com.jiaoerzhang.platform.api.AlbumApi
import com.jiaoerzhang.platform.contract.AlbumContract
import com.jiaoerzhang.platform.entity.album.AlbumEntity
import com.jiaoerzhang.platform.entity.album.SearchAlbumEntity
import com.jiaoerzhang.platform.entity.album.StyleEntity
import com.jiaoerzhang.platform.entity.home.HomeEntity
import com.jiaoerzhang.platform.entity.home.HomeTopEntity
import com.jiaoerzhang.platform.lib_core.base.mvp.BaseMvpFragment
import com.jiaoerzhang.platform.lib_core.base.mvp.BasePresenter
import com.jiaoerzhang.platform.lib_net.network.http.HttpRequestPresenter
import com.jiaoerzhang.platform.lib_net.network.http.ModelCallback
import com.jiaoerzhang.platform.presenter.AlbumPresenter
import com.jiaoerzhang.platform.ui.activity.search.SearchActivity
import kotlinx.android.synthetic.main.fragment_album_layout.*
import android.R.id.tabs
import android.os.Bundle
import com.astuetz.PagerSlidingTabStrip


/**
 * 专辑
 */
class AlbumFragment : BaseMvpFragment<AlbumContract.IAlbumModel, AlbumContract.AlbumPresenter>(),


        AlbumContract.IAlbumView {

    private var TITLES: MutableList<StyleEntity.Content>? = ArrayList<StyleEntity.Content>()

    override fun sucess(albumEntity: AlbumEntity) {
    }


    override fun onFirstUserVisible() {
    }

    override fun init() {

        HttpRequestPresenter.getInstance().get(
                AlbumApi.ALBUM_STYLE_URL,
                HashMap<String, String>(),
                object : ModelCallback<StyleEntity>(StyleEntity::class.java) {
                    override fun onSuccess(t: StyleEntity?) {

                        TITLES = t!!.content
                        var content: StyleEntity.Content = StyleEntity.Content("", 1, 1, "all")
                        TITLES!!.add(0, content)


                        val fragments: MutableList<Fragment> = ArrayList()

                        var tls: MutableList<String> = arrayListOf()
                        for (s in TITLES!!) {
                            val bundle = Bundle()
                            bundle.putString("name", s.name)
                            bundle.putString("code", s.code)
                            tls.add(s.name)
                            fragments.add(FragmentContent.getInstance(bundle))
                        }
                        pager.adapter = MyFrPagerAdapter(activity!!.getSupportFragmentManager(), tls as java.util.ArrayList<String>?, fragments)
                        tabs.setViewPager(pager)
                        pager.currentItem = 0

//                    var adapter = MyPagerAdapter(activity!!.supportFragmentManager)
//                    pager.offscreenPageLimit = 0
//                    pager.adapter = adapter
//                    tabs.setViewPager(pager)
//                    pager.currentItem = 0
//
//                    tabs.setOnTabReselectedListener {
//
//                    }

                    }

                    override fun onSuccessMsg(status: String?, message: String?) {
                    }

                    override fun onErrorMsg(code: Int, msg: String?) {
                    }

                })

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_album_layout
    }

    override fun initViewsAndEvents(view: View) {
        bar_search.setOnClickListener {
            startActivity(Intent(activity, SearchActivity::class.java))
        }


    }


    override fun DetoryViewAndThing() {

    }


    override fun onUserVisible() {

    }

    override fun onUserInvisible() {

    }


    override fun initPresenter(): BasePresenter<*, *>? {
        return AlbumPresenter()
    }


}
