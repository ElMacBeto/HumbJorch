package com.humbjorch.myapplication.data.datSource.api.remote

import com.humbjorch.myapplication.data.datSource.api.response.NewFactsResponse
import com.humbjorch.myapplication.sis.utils.util.Constants.END_POINT_FACTS
import retrofit2.Response
import retrofit2.http.GET

interface WebService {

    @GET(END_POINT_FACTS)
    suspend fun getFacts(): Response<NewFactsResponse>

}