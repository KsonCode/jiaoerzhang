package com.jiaoerzhang.platform.entity.user

import com.jiaoerzhang.platform.lib_net.network.BaseResponse

data class SignEntity(
    val signDates: String
):BaseResponse<SignEntity>()