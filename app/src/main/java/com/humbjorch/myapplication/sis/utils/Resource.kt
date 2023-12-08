package com.humbjorch.myapplication.sis.utils

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class Resource<T>(
    val status: Status,
    val data: T? = null,
    val message: String? = null
) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data)
        }

        fun <T> error(msg: String? = null, data: T? = null): Resource<T> {
            return Resource(Status.ERROR, message = msg, data = data)
        }

        fun <T> loading(msg: Int? = null): Resource<T> {
            return Resource(Status.LOADING)
        }
    }
}

