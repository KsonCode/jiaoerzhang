package com.jiaoerzhang.platform.entity.user

import com.jiaoerzhang.platform.lib_net.network.BaseResponse

data class LoginEntity(
    val loginToken: String,
    val username: String
):BaseResponse<LoginEntity>()