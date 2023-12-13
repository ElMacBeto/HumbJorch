package com.humbjorch.myapplication.data.datSource.api.mapper

interface Mapper<I, O> {
    suspend fun map(input: I): O
}