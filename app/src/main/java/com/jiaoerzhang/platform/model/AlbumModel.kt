package com.jiaoerzhang.platform.model

import com.jiaoerzhang.platform.api.AlbumApi
import com.jiaoerzhang.platform.api.HomeApi
import com.jiaoerzhang.platform.contract.AlbumContract
import com.jiaoerzhang.platform.entity.album.AlbumEntity
import com.jiaoerzhang.platform.entity.home.HomeEntity
import com.jiaoerzhang.platform.lib_net.network.PresenterCallback
import com.jiaoerzhang.platform.lib_net.network.http.HttpRequestPresenter
import com.jiaoerzhang.platform.lib_net.network.http.ModelCallback


class AlbumModel : AlbumContract.IAlbumModel {
    override fun getAlbums(string: String, hashMap: HashMap<String, String>, callback: PresenterCallback<AlbumEntity>) {



        HttpRequestPresenter.getInstance().jsonPostData(AlbumApi.SEARCH_ALBUM_URL,hashMap, string, object : ModelCallback<AlbumEntity>(AlbumEntity::class.java) {

            override fun onSuccess(t: AlbumEntity?) {
                callback.onSuccess(t)
            }

            override fun onSuccessMsg(status: String?, message: String?) {
            }

            override fun onErrorMsg(code: Int, msg: String?) {
            }

        })

    }





}
