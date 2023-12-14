package com.humbjorch.myapplication.data.datSource.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.humbjorch.myapplication.data.model.FactsEntity

@Database(
    entities = [FactsEntity::class],
    version = 1,
    exportSchema = true
)
abstract class BuildDataBase : RoomDatabase() {
    abstract fun factsDAO(): FactsDAO
}