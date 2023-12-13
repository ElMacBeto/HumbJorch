package com.humbjorch.myapplication.data.datSource.api.remote

import com.humbjorch.myapplication.data.datSource.api.response.NewFactsResponse
import retrofit2.Response
import retrofit2.http.GET

interface WebService {

    @GET("/v1/gobmx.facts")
    suspend fun getFacts(): Response<NewFactsResponse>

}