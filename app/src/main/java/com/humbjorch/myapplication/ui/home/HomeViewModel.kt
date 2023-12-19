package com.humbjorch.myapplication.ui.home

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humbjorch.myapplication.data.datSource.ResponseStatus
import com.humbjorch.myapplication.data.model.FactsEntity
import com.humbjorch.myapplication.domain.MemoryLocalRepository
import com.humbjorch.myapplication.domain.NewAuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MemoryLocalRepository,
    private val newAuthenticationRepository: NewAuthenticationRepository
) :
    ViewModel() {

    private var _getFavoriteFactsLiveData = MutableLiveData<ResponseStatus<Any>?>()
    val getFavoriteFactsLiveData: LiveData<ResponseStatus<Any>?> get() = _getFavoriteFactsLiveData

    private var _getUpdateLiveData = MutableLiveData<ResponseStatus<Any>>()
    val getUpdateLiveData: LiveData<ResponseStatus<Any>> get() = _getUpdateLiveData

    private var _singOutLiveData = MutableLiveData<ResponseStatus<Any>>()
    val singOutLiveData: LiveData<ResponseStatus<Any>> get() = _singOutLiveData

    fun singOut() {
        viewModelScope.launch {
            handelSingOutResponseStatus(newAuthenticationRepository.logOut())
        }

    }

    fun getFavoritesFacts() {
        _getFavoriteFactsLiveData.value = ResponseStatus.Loading()
        viewModelScope.launch {
            handelServiceResponseStatus(repository.getFavorites())
        }
    }

    fun getAllFacts(limit: Int) {
        _getFavoriteFactsLiveData.value = ResponseStatus.Loading()
        viewModelScope.launch {
            handelServiceResponseStatus(repository.getFacts(limit))
        }
    }

    fun updateData(factsEntity: FactsEntity) {
        factsEntity.apply {
            isFavorite = !this.isFavorite
        }
        viewModelScope.launch {
            handelUpdateResponseStatus(repository.updateData(factsEntity))
        }
    }

    @Suppress("UNCHECKED_CAST")
    @SuppressLint("NullSafeMutableLiveData")
    private fun handelUpdateResponseStatus(apiResponseStatus: ResponseStatus<Int>) {
        _getUpdateLiveData.value = apiResponseStatus as ResponseStatus<Any>
    }

    @Suppress("UNCHECKED_CAST")
    @SuppressLint("NullSafeMutableLiveData")
    private fun handelServiceResponseStatus(apiResponseStatus: ResponseStatus<List<FactsEntity>>) {
        _getFavoriteFactsLiveData.value = apiResponseStatus as ResponseStatus<Any>
    }

    @Suppress("UNCHECKED_CAST")
    @SuppressLint("NullSafeMutableLiveData")
    private fun handelSingOutResponseStatus(apiResponseStatus: ResponseStatus<Boolean>) {
        _singOutLiveData.value = apiResponseStatus as ResponseStatus<Any>
    }

    fun clearLivedata() {
        _getFavoriteFactsLiveData.value = null
    }

}