package com.jiaoerzhang.platform.model

import com.jiaoerzhang.platform.api.HomeApi
import com.jiaoerzhang.platform.contract.HomeContract
import com.jiaoerzhang.platform.entity.home.HomeEntity
import com.jiaoerzhang.platform.lib_net.network.PresenterCallback
import com.jiaoerzhang.platform.lib_net.network.http.HttpRequestPresenter
import com.jiaoerzhang.platform.lib_net.network.http.ModelCallback

class HomeModel : HomeContract.IhomeModel {
    override fun getHome(hashMap: HashMap<String,String>,callback: PresenterCallback<HomeEntity>) {
        HttpRequestPresenter.getInstance().post(HomeApi.HOME_URL, hashMap,object : ModelCallback<HomeEntity>(HomeEntity::class.java) {
            override fun onSuccess(t: HomeEntity?) {
                callback.onSuccess(t)
            }
            override fun onSuccessMsg(status: String?, message: String?) {
            }
            override fun onErrorMsg(code: Int, msg: String?) {
            }

        })
    }


}
