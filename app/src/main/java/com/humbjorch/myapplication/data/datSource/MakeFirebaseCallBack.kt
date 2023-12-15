package com.humbjorch.myapplication.data.datSource

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.SQLException

suspend fun <T> makeFirebaseCall(
    call: suspend () -> T
): ResponseStatus<T> = withContext(Dispatchers.IO) {
    try {
        ResponseStatus.Success(call())
    } catch (e: ClassNotFoundException) {
        ResponseStatus.Error(e.message.toString())
    } catch (e: Exception) {
        ResponseStatus.Error(e.message.toString())
    } catch (e: SQLException) {
        ResponseStatus.Error(e.message.toString())
    }
}

sealed class ResponseStatus<T> {
    class Success<T>(val data: T) : ResponseStatus<T>()
    class Loading<T> : ResponseStatus<T>()
    class Error<T>(val messageId: String) : ResponseStatus<T>()
}