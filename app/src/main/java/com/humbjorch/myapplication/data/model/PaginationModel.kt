package com.humbjorch.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class PaginationModel (
    @SerializedName("pageSize" ) var pageSize : Int? = null,
    @SerializedName("page"     ) var page     : Int? = null,
    @SerializedName("total"    ) var total    : Int? = null

)