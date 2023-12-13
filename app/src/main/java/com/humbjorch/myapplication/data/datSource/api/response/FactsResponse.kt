package com.humbjorch.myapplication.data.datSource.api.response

import com.google.gson.annotations.SerializedName

data class FactsResponse(
    @SerializedName("_id"          ) var id           : String? = null,
    @SerializedName("date_insert"  ) var dateInsert   : String? = null,
    @SerializedName("slug"         ) var slug         : String? = null,
    @SerializedName("columns"      ) var columns      : String? = null,
    @SerializedName("fact"         ) var fact         : String? = null,
    @SerializedName("organization" ) var organization : String? = null,
    @SerializedName("resource"     ) var resource     : String? = null,
    @SerializedName("url"          ) var url          : String? = null,
    @SerializedName("operations"   ) var operations   : String? = null,
    @SerializedName("dataset"      ) var dataset      : String? = null,
    @SerializedName("created_at"   ) var createdAt    : Int?    = null
)
