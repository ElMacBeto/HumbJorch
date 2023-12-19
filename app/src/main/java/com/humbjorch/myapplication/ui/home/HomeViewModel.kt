package com.humbjorch.myapplication.ui.home

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humbjorch.myapplication.data.datSource.ResponseStatus
import com.humbjorch.myapplication.data.model.FactsEntity
import com.humbjorch.myapplication.domain.MemoryLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: MemoryLocalRepository) :
    ViewModel() {

    private var _getFavoriteFactsLiveData = MutableLiveData<ResponseStatus<Any>>()


    val getFavoriteFactsLiveData: LiveData<ResponseStatus<Any>> get() = _getFavoriteFactsLiveData



    fun getFavoritesFacts(limit:Int) {
        _getFavoriteFactsLiveData.value = ResponseStatus.Loading()
        viewModelScope.launch {
            handelServiceResponseStatus(repository.getFavorites(limit))
        }
    }

    fun getAllFacts(limit:Int) {
        _getFavoriteFactsLiveData.value = ResponseStatus.Loading()
        viewModelScope.launch {
            handelServiceResponseStatus(repository.getFacts(limit))
        }
    }

    @Suppress("UNCHECKED_CAST")
    @SuppressLint("NullSafeMutableLiveData")
    private fun handelServiceResponseStatus(apiResponseStatus: ResponseStatus<List<FactsEntity>>) {
        _getFavoriteFactsLiveData.value = apiResponseStatus as ResponseStatus<Any>
    }

}