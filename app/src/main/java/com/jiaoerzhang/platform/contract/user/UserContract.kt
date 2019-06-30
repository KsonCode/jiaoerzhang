package com.jiaoerzhang.platform.contract.user

import com.jiaoerzhang.platform.entity.user.UserEntity
import com.jiaoerzhang.platform.lib_core.base.mvp.BasePresenter
import com.jiaoerzhang.platform.lib_core.base.mvp.IBaseModel
import com.jiaoerzhang.platform.lib_net.network.PresenterCallback
import com.jiaoerzhang.platform.model.UserModel

/**
 * 首页契约类统一管理
 */
interface UserContract {

    abstract class UserPresenter : BasePresenter<IUserModel, IUserView>() {

        override fun getmModel(): IUserModel {
            return UserModel()
        }

         abstract fun login(hashMap: HashMap<String,String>)

    }

    interface IUserView {

        fun sucess(userEntity: UserEntity)

    }

    interface IUserModel : IBaseModel {

        fun login(callback: PresenterCallback<UserEntity>)
    }
}
