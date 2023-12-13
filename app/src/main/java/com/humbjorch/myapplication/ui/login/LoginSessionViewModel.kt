package com.humbjorch.myapplication.ui.login

import androidx.lifecycle.ViewModel
import com.humbjorch.myapplication.sis.di.ModuleSharePreference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginSessionViewModel @Inject constructor(private val moduleSharePreference: ModuleSharePreference) :
    ViewModel() {
    fun getEmail() = moduleSharePreference.getEmail()
    fun getImageUrl() = moduleSharePreference.getPhoto()
    fun getTouchId() = moduleSharePreference.getTouchId()
}