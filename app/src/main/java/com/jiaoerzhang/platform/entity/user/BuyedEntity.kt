package com.jiaoerzhang.platform.entity.user

import com.jiaoerzhang.platform.lib_net.network.BaseResponse

data class BuyedEntity(
    val albumPrice: String,
    val buy: String,
    val isEnough: String,
    val ladyCoin: String,
    val photoPrice: String,
    val status: String
)