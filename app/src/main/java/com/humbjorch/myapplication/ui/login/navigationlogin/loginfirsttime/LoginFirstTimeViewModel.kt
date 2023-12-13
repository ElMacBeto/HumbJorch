package com.humbjorch.myapplication.ui.login.navigationlogin.loginfirsttime

import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humbjorch.myapplication.data.datSource.ResponseStatus
import com.humbjorch.myapplication.data.local.AuthenticationResponse
import com.humbjorch.myapplication.data.model.UserModel
import com.humbjorch.myapplication.domain.NewAuthenticationRepository
import com.humbjorch.myapplication.sis.utils.util.Constants.StatusRequest.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginFirstTimeViewModel @Inject constructor(private val authenticationRepository: NewAuthenticationRepository) :
    ViewModel() {

    private var _createRegister = MutableLiveData<ResponseStatus<Any>>()
    val createRegister: LiveData<ResponseStatus<Any>> get() = _createRegister

    fun getClientProvide() = authenticationRepository.getClientProvide()

    fun createNewRegister(
        user: String,
        password: String
    ) {
        viewModelScope.launch {
            _createRegister.value = ResponseStatus.Loading()
            handelFirebaseResponseStatus(authenticationRepository.createNewRegister(user, password))
        }
    }

    fun loginAccount(
        user: String,
        password: String
    ) {
        viewModelScope.launch {
            _createRegister.value = ResponseStatus.Loading()
            handelFirebaseResponseStatus( authenticationRepository.loginUser(user, password))
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