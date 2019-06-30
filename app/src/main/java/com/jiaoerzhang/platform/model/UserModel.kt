package com.jiaoerzhang.platform.model

import com.jiaoerzhang.platform.api.HomeApi
import com.jiaoerzhang.platform.contract.user.UserContract
import com.jiaoerzhang.platform.entity.user.UserEntity
import com.jiaoerzhang.platform.lib_net.network.PresenterCallback
import com.jiaoerzhang.platform.lib_net.network.http.HttpRequestPresenter
import com.jiaoerzhang.platform.lib_net.network.http.ModelCallback

import java.util.HashMap

class UserModel : UserContract.IUserModel {
    override fun login(callback: PresenterCallback<UserEntity>) {

        HttpRequestPresenter.getInstance().get(HomeApi.HOME_URL, HashMap<String, String>(), object : ModelCallback<UserEntity>(UserEntity::class.java) {
            override fun onSuccess(t: UserEntity?) {
                callback.onSuccess(t)
            }

            override fun onSuccessMsg(status: String?, message: String?) {
            }

            override fun onErrorMsg(code: Int, msg: String?) {
            }

        })
    }



}
