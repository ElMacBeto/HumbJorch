package com.humbjorch.myapplication.domain

import com.humbjorch.myapplication.data.datSource.ResponseStatus
import com.humbjorch.myapplication.data.datSource.api.remote.WebService
import com.humbjorch.myapplication.data.datSource.localDS.LocalDS
import com.humbjorch.myapplication.data.datSource.makeFirebaseCall
import com.humbjorch.myapplication.data.model.FactsEntity
import com.humbjorch.myapplication.data.model.FactsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface ConnectionWebService {
    suspend fun getFacts(): ResponseStatus<ArrayList<FactsModel>>
}

@Singleton
class FactsRepository @Inject constructor(
    private val apiService: WebService
) : ConnectionWebService {

    override suspend fun getFacts(): ResponseStatus<ArrayList<FactsModel>> {
        return withContext(Dispatchers.IO) {
            val request = async { getAllFacts() }
            val response = request.await()
            if (response is ResponseStatus.Success) {
                ResponseStatus.Success(response.data)
            } else {
                ResponseStatus.Error((response as ResponseStatus.Error).messageId)
            }
        }
    }

    private suspend fun getAllFacts(): ResponseStatus<ArrayList<FactsModel>> = makeFirebaseCall {
        val response = getAllFactsResponse()
        response
    }

    private suspend fun getAllFactsResponse(): ArrayList<FactsModel> {
        return apiService.getFacts().let {
            val data = it.body()!!
            data.results
        }
    }
}