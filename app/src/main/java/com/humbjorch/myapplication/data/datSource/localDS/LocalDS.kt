package com.humbjorch.myapplication.data.datSource.localDS

import androidx.lifecycle.Observer
import com.humbjorch.myapplication.data.datSource.db.FactsDAO
import com.humbjorch.myapplication.data.model.FactsEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDS @Inject constructor(private val factsDAO: FactsDAO) {
    fun insertEntity(factsEntity: FactsEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            factsDAO.insertData(factsEntity)
        }
    }

    fun getDataLocal(limit: Int): List<FactsEntity> = factsDAO.getAllData(limit)

    fun getCount(): Int = factsDAO.getCount()

    fun getFavorite(limit: Int): List<FactsEntity> = factsDAO.getFavorite(true, limit)

    fun updateData(factsEntity: FactsEntity) =
        factsDAO.updateData(factsEntity)

    fun delete() = factsDAO.deleteTable()


}