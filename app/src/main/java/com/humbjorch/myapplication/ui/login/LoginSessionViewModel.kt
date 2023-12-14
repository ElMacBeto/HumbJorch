package com.humbjorch.myapplication.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humbjorch.myapplication.data.datSource.ResponseStatus
import com.humbjorch.myapplication.data.local.AuthenticationResponse
import com.humbjorch.myapplication.domain.NewAuthenticationRepository
import com.humbjorch.myapplication.sis.di.ModuleSharePreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginSessionViewModel @Inject constructor(
    private val moduleSharePreference: ModuleSharePreference,
    private val authenticationRepository: NewAuthenticationRepository
) :
    ViewModel() {


    private var _createRegister = MutableLiveData<ResponseStatus<Any>>()
    val createRegister: LiveData<ResponseStatus<Any>> get() = _createRegister
    private fun getEmail() = moduleSharePreference.getEmail()
    fun getImageUrl() = moduleSharePreference.getPhoto()
    fun getTouchId() = moduleSharePreference.getTouchId()

    fun loginAccount(
        password: String,
        touchId: Boolean
    ) {
        viewModelScope.launch {
            _createRegister.value = ResponseStatus.Loading()
            handelFirebaseResponseStatus(
                authenticationRepository.loginUser(
                    getEmail()!!,
                    password,
                    touchId
                )
            )
        }
    }

    fun launchGoogle(
        data: Intent
    ) {
        viewModelScope.launch {
            _createRegister.value = ResponseStatus.Loading()
            handelFirebaseResponseStatus(authenticationRepository.singInWithGoogle(data))
        }
    }

    fun getClientProvide() = authenticationRepository.getClientProvide()

    /***
     * HANDLE
     *
     */
    @Suppress("UNCHECKED_CAST")
    @SuppressLint("NullSafeMutableLiveData")
    private fun handelFirebaseResponseStatus(apiResponseStatus: ResponseStatus<AuthenticationResponse>) {
        _createRegister.value = apiResponseStatus as ResponseStatus<Any>
    }
}