package com.humbjorch.myapplication.data.datSource.api.response

import com.google.gson.annotations.SerializedName
import com.humbjorch.myapplication.data.model.FactsModel
import com.humbjorch.myapplication.data.model.PaginationModel

data class NewFactsResponse(
    @SerializedName("pagination")
    var pagination: PaginationModel? = PaginationModel(),
    @SerializedName("results")
    var results: ArrayList<FactsModel> = arrayListOf()
)
