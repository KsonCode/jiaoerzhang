package com.jiaoerzhang.platform.contract

import com.jiaoerzhang.platform.entity.album.AlbumEntity
import com.jiaoerzhang.platform.entity.home.HomeEntity
import com.jiaoerzhang.platform.lib_core.base.mvp.BasePresenter
import com.jiaoerzhang.platform.lib_core.base.mvp.IBaseModel
import com.jiaoerzhang.platform.lib_net.network.PresenterCallback
import com.jiaoerzhang.platform.model.AlbumModel

/**
 * 首页契约类统一管理
 */
interface AlbumContract {

    abstract class AlbumPresenter : BasePresenter<IAlbumModel, IAlbumView>() {

        override fun getmModel(): IAlbumModel {
            return AlbumModel()
        }

         abstract fun getAlbums(string: String,hashMap: HashMap<String,String>)

    }

    interface IAlbumView {

        fun sucess(albumEntity: AlbumEntity)

    }

    interface IAlbumModel : IBaseModel {

        fun getAlbums(string: String,hashMap: HashMap<String, String>,callback: PresenterCallback<AlbumEntity>)
    }
}
