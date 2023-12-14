package com.humbjorch.myapplication.ui.login.navigationlogin.splash

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humbjorch.myapplication.data.datSource.ResponseStatus
import com.humbjorch.myapplication.data.model.FactsModel
import com.humbjorch.myapplication.domain.FactsRepository
import com.humbjorch.myapplication.sis.di.ModuleSharePreference
import com.humbjorch.myapplication.sis.utils.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val moduleSharePreference: ModuleSharePreference,
    private val factsRepository: FactsRepository
) :
    ViewModel() {

    private var _setDestination = MutableLiveData<Constants.ListenerLoginDestination>()
    val setDestination: LiveData<Constants.ListenerLoginDestination> get() = _setDestination

    private var _getAllFactsLiveData = MutableLiveData<ResponseStatus<Any>>()
    val getAllFactsLiveData: LiveData<ResponseStatus<Any>> get() = _getAllFactsLiveData


    fun setDestinationLogin() {
        val isTouchId = moduleSharePreference.getTouchId()

        val isFirstTime = moduleSharePreference.getEmail().toString()
            .isEmpty() && moduleSharePreference.getPhoto().toString().isEmpty()

        val isGoogleSession = moduleSharePreference.getGoogleSession()

        if (isFirstTime && !isTouchId) {
            _setDestination.value = Constants.ListenerLoginDestination.DestinationFirstTime
        } else {
            if (isGoogleSession) {
                _setDestination.value = Constants.ListenerLoginDestination.DestinationGoogleSession
            } else if (isTouchId) {
                _setDestination.value = Constants.ListenerLoginDestination.DestinationTouchId
            } else {
                _setDestination.value =
                    Constants.ListenerLoginDestination.DestinationEmailAndPASSWORD
            }
        }
    }

    fun getFacts(){
        viewModelScope.launch {
            _getAllFactsLiveData.value = ResponseStatus.Loading()
            handelServiceResponseStatus(factsRepository.getFacts())
        }
    }

    @Suppress("UNCHECKED_CAST")
    @SuppressLint("NullSafeMutableLiveData")
    private fun handelServiceResponseStatus(apiResponseStatus: ResponseStatus<ArrayList<FactsModel>>) {
        _getAllFactsLiveData.value = apiResponseStatus as ResponseStatus<Any>
    }

}