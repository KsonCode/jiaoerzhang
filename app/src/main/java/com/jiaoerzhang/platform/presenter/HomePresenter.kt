package com.jiaoerzhang.platform.presenter

import com.jiaoerzhang.platform.contract.HomeContract
import com.jiaoerzhang.platform.entity.home.HomeEntity
import com.jiaoerzhang.platform.lib_net.network.PresenterCallback

class HomePresenter : HomeContract.HomePresenter() {
    override fun getHome(hashMap: HashMap<String,String>) {

        mModel.getHome(hashMap,object :PresenterCallback<HomeEntity>{
            override fun onSuccess(t: HomeEntity?) {
                mViews.get()!!.Sucess(t!!)
            }

            override fun onSuccessMsg(status: String?, message: String?) {
            }

            override fun onErrorMsg(code: Int, msg: String?) {
            }

        })
    }
}
