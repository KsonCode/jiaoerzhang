package com.jiaoerzhang.platform.contract

import com.jiaoerzhang.platform.entity.home.HomeEntity
import com.jiaoerzhang.platform.lib_core.base.mvp.BasePresenter
import com.jiaoerzhang.platform.lib_core.base.mvp.IBaseModel
import com.jiaoerzhang.platform.lib_net.network.PresenterCallback
import com.jiaoerzhang.platform.model.HomeModel

/**
 * 首页契约类统一管理
 */
interface HomeContract {

    abstract class HomePresenter : BasePresenter<IhomeModel, IhomeView>() {

        override fun getmModel(): IhomeModel {
            return HomeModel()
        }

//         abstract fun getBanner()
         abstract fun getHome(hashMap: HashMap<String,String>)

    }

    interface IhomeView {

//        fun bannerSucess(bannerList: List<BannerEntity>)
        fun Sucess(homeEntity: HomeEntity)

    }

    interface IhomeModel : IBaseModel {

//        fun getBanner(callback: PresenterCallback<List<BannerEntity>>)
        fun getHome(hashMap: HashMap<String, String>,callback: PresenterCallback<HomeEntity>)
    }
}
