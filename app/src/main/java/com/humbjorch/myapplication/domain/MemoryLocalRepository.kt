package com.humbjorch.myapplication.domain

import com.humbjorch.myapplication.data.datSource.ResponseStatus
import com.humbjorch.myapplication.data.datSource.localDS.LocalDS
import com.humbjorch.myapplication.data.datSource.makeFirebaseCall
import com.humbjorch.myapplication.data.model.FactsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface DataBaseConnectionTask {
    suspend fun getFavorites(): ResponseStatus<List<FactsEntity>>

    suspend fun getAllFavorites(limit: Int): ResponseStatus<List<FactsEntity>>
    suspend fun getFacts(limit: Int): ResponseStatus<List<FactsEntity>>
    suspend fun updateData(factsEntity: FactsEntity): ResponseStatus<Int>
}

@Singleton
class MemoryLocalRepository @Inject constructor(private val localDS: LocalDS) :
    DataBaseConnectionTask {
    override suspend fun getFavorites(): ResponseStatus<List<FactsEntity>> {
        return withContext(Dispatchers.IO){
            val getFavorite = async { getFavorite() }
            val getFavoriteResponse = getFavorite.await()

            if (getFavoriteResponse is ResponseStatus.Success) {
                ResponseStatus.Success(getFavoriteResponse.data)
            } else {
                ResponseStatus.Error((getFavoriteResponse as ResponseStatus.Error).messageId)
            }
        }
    }

    override suspend fun getAllFavorites(limit: Int): ResponseStatus<List<FactsEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun getFacts(limit: Int): ResponseStatus<List<FactsEntity>> {
        return withContext(Dispatchers.IO) {
            val getFavorite = async { getAllFacts(limit) }
            val getFactsResponse = getFavorite.await()

            if (getFactsResponse is ResponseStatus.Success) {
                ResponseStatus.Success(getFactsResponse.data)
            } else {
                ResponseStatus.Error((getFactsResponse as ResponseStatus.Error).messageId)
            }
        }
    }

    private suspend fun getFavorite(): ResponseStatus<List<FactsEntity>> = makeFirebaseCall {
        val response = getResponseData()
        response
    }

    private fun getResponseData(): List<FactsEntity> {
        return localDS.getFavorite()
    }

    private suspend fun getAllFacts(limit: Int): ResponseStatus<List<FactsEntity>> =
        makeFirebaseCall {
            val response = getResponseFacts(limit)
            response
        }

    private fun getResponseFacts(limit: Int): List<FactsEntity> {
        return localDS.getDataLocal(limit)
    }

    override suspend fun updateData(factsEntity: FactsEntity): ResponseStatus<Int> {
        return withContext(Dispatchers.IO) {
            val updateDataR = async { updateDataRequest(factsEntity) }
            val getUpdateResponse = updateDataR.await()

            if (getUpdateResponse is ResponseStatus.Success) {
                ResponseStatus.Success(getUpdateResponse.data)
            } else {
                ResponseStatus.Error((getUpdateResponse as ResponseStatus.Error).messageId)
            }
        }
    }

    private suspend fun updateDataRequest(factsEntity: FactsEntity): ResponseStatus<Int> =
        makeFirebaseCall {
            val response = getResponseDataUpdate(factsEntity)
            response
        }

    private fun getResponseDataUpdate(factsEntity: FactsEntity): Int =
        localDS.updateData(factsEntity)
}
