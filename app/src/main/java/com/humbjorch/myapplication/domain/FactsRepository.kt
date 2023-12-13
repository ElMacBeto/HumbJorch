package com.humbjorch.myapplication.domain

import com.humbjorch.myapplication.data.datSource.ResponseStatus
import com.humbjorch.myapplication.data.datSource.api.remote.WebService
import com.humbjorch.myapplication.data.model.FactsModel
import javax.inject.Inject
import javax.inject.Singleton

interface ConnectionWebService {
    suspend fun getFacts(): ResponseStatus<ArrayList<FactsModel>>
}

@Singleton
class FactsRepository @Inject constructor(
    private val apiService: WebService
): ConnectionWebService {


    override suspend fun getFacts(): ResponseStatus<ArrayList<FactsModel>> {
        val response = apiService.getFacts()
        return if (response.isSuccessful){
            val data = response.body()!!
            val factsList = data.results
            ResponseStatus.Success(factsList)
        }else{
            ResponseStatus.Error(response.message())
        }
    }


}