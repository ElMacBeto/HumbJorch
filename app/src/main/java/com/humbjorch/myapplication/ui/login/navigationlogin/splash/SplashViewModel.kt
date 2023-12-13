package com.humbjorch.myapplication.ui.login.navigationlogin.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.humbjorch.myapplication.sis.di.ModuleSharePreference
import com.humbjorch.myapplication.sis.utils.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val moduleSharePreference: ModuleSharePreference) :
    ViewModel() {

    private var _setDestination = MutableLiveData<Constants.ListenerLoginDestination>()
    val setDestination: LiveData<Constants.ListenerLoginDestination> get() = _setDestination


    fun setDestinationLogin() {
        val isTouchId = moduleSharePreference.getTouchId()

        val isFirstTime = moduleSharePreference.getEmail().toString()
            .isEmpty() && moduleSharePreference.getPhoto().toString().isEmpty()

        val isGoogleSession = moduleSharePreference.getGoogleSession()

        if (isFirstTime && !isTouchId) {
            _setDestination.value = Constants.ListenerLoginDestination.DestinationFirstTime
        } else {
            if(isGoogleSession){
                _setDestination.value = Constants.ListenerLoginDestination.DestinationGoogleSession
            } else if (isTouchId){
                _setDestination.value = Constants.ListenerLoginDestination.DestinationTouchId
            }else{
                _setDestination.value = Constants.ListenerLoginDestination.DestinationEmailAndPASSWORD
            }
        }
    }
}