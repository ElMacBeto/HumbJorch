package com.humbjorch.myapplication.domain

import com.humbjorch.myapplication.data.datSource.ResponseStatus
import com.humbjorch.myapplication.data.datSource.localDS.LocalDS
import com.humbjorch.myapplication.data.datSource.makeFirebaseCall
import com.humbjorch.myapplication.data.model.FactsEntity
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

interface DataBaseConnectionTask {
    suspend fun getFavorites(): ResponseStatus<List<FactsEntity>>
}

@Singleton
class MemoryLocalRepository @Inject constructor(private val localDS: LocalDS) :
    DataBaseConnectionTask {
    override suspend fun getFavorites(): ResponseStatus<List<FactsEntity>> {
        return withContext(Dispatchers.IO){
            val getFavorite = async { getFavorite() }
            val getFavoriteResponse = getFavorite.await()

            if(getFavoriteResponse is ResponseStatus.Success){
                ResponseStatus.Success(getFavoriteResponse.data)
            }else{
                ResponseStatus.Error((getFavoriteResponse as ResponseStatus.Error).messageId)
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
}