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

    fun getDataLocal(observer: Observer<List<FactsEntity>>) {
        CoroutineScope(Dispatchers.IO).launch {
            val facts = factsDAO.getAllData()
            withContext(Dispatchers.Main) {
                observer.onChanged(facts)
            }
        }
    }

    fun getCount(): Int = factsDAO.getCount()


    fun getFavorite(): List<FactsEntity> = factsDAO.getFavorite(true)

}