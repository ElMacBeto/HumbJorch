package com.humbjorch.myapplication.sis.di

import com.humbjorch.myapplication.data.datSource.db.BuildDataBase
import com.humbjorch.myapplication.data.datSource.db.FactsDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module(includes = [DataBaseModule::class])
@InstallIn(SingletonComponent::class)
object ModuleDAOHilt {

    @Provides
    @Singleton
    fun provideFactsDAO(buildDataBase: BuildDataBase): FactsDAO {
        return buildDataBase.factsDAO()
    }
}