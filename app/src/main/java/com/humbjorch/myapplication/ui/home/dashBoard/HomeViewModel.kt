package com.humbjorch.myapplication.ui.home.dashBoard

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humbjorch.myapplication.data.datSource.ResponseStatus
import com.humbjorch.myapplication.data.model.FactsModel
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {


    private var _getAllFactsLiveData = MutableLiveData<ResponseStatus<Any>>()
    val getAllFactsLiveData: LiveData<ResponseStatus<Any>> get() = _getAllFactsLiveData

    fun getFacts(){
        viewModelScope.launch {
            handelServiceResponseStatus(ResponseStatus.Success(arrayListOf()))
        }
    }

    @Suppress("UNCHECKED_CAST")
    @SuppressLint("NullSafeMutableLiveData")
    private fun handelServiceResponseStatus(apiResponseStatus: ResponseStatus<ArrayList<FactsModel>>) {
        _getAllFactsLiveData.value = apiResponseStatus as ResponseStatus<Any>
    }
}