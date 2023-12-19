package com.humbjorch.myapplication.data.datSource.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.humbjorch.myapplication.data.model.FactsEntity

@Dao
interface FactsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(factsEntity: FactsEntity)

    @Query("SELECT * FROM FACTS LIMIT :limit, 10")
    fun getAllData(limit:Int): List<FactsEntity>

    @Update
    fun updateData(factsEntity: FactsEntity): Int

    @Query("SELECT COUNT(*) FROM FACTS")
     fun getCount(): Int

    @Query("select max(id) from FACTS")
    fun getMaxId(): Int

    @Query("SELECT * FROM FACTS WHERE isFavorite = :isFavorite LIMIT :limit, 10")
    fun getFavorite(isFavorite: Boolean, limit:Int): List<FactsEntity>
    @Query("DELETE from FACTS")
    fun deleteTable():Int

}