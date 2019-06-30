package com.jiaoerzhang.platform.presenter

import com.jiaoerzhang.platform.contract.AlbumContract
import com.jiaoerzhang.platform.entity.album.AlbumEntity
import com.jiaoerzhang.platform.entity.home.HomeEntity
import com.jiaoerzhang.platform.lib_net.network.PresenterCallback

class AlbumPresenter : AlbumContract.AlbumPresenter() {
    override fun getAlbums(string: String, hashMap: HashMap<String, String>) {

        mModel.getAlbums(string,hashMap,object :PresenterCallback<AlbumEntity>{
            override fun onSuccess(t: AlbumEntity?) {
                mViews.get()!!.sucess(t!!)
            }

            override fun onSuccessMsg(status: String?, message: String?) {
            }

            override fun onErrorMsg(code: Int, msg: String?) {
            }

        })
    }


}
