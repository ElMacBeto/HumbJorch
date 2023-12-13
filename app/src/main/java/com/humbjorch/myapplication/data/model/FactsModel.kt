package com.humbjorch.myapplication.data.model

data class FactsModel(
    val id: String,
    val columns: String,
    val createdAt: Int,
    val dataset: String,
    val dateInsert: String,
    val fact: String,
    val operations: String,
    val organization: String,
    val resource: String,
    val slug: String,
    val url: String
)