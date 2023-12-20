package com.humbjorch.myapplication.ui.login.navigationlogin.splash

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humbjorch.myapplication.data.datSource.ResponseStatus
import com.humbjorch.myapplication.data.datSource.localDS.LocalDS
import com.humbjorch.myapplication.data.model.FactsEntity
import com.humbjorch.myapplication.data.model.FactsModel
import com.humbjorch.myapplication.domain.FactsRepository
import com.humbjorch.myapplication.sis.di.ModuleSharePreference
import com.humbjorch.myapplication.sis.utils.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val moduleSharePreference: ModuleSharePreference,
    private val factsRepository: FactsRepository,
    private val localDS: LocalDS
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

    fun getFacts() {
        _getAllFactsLiveData.value = ResponseStatus.Loading()
        CoroutineScope(Dispatchers.IO).launch {
            if (localDS.getCount() > 0) {
                CoroutineScope(Dispatchers.Main).launch {
                    setDestinationLogin()
                }
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    handelServiceResponseStatus(factsRepository.getFacts())
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    @SuppressLint("NullSafeMutableLiveData")
    private fun handelServiceResponseStatus(apiResponseStatus: ResponseStatus<ArrayList<FactsModel>>) {
        _getAllFactsLiveData.value = apiResponseStatus as ResponseStatus<Any>
    }

    fun createEntityInsert(data: ArrayList<FactsModel>) {
        viewModelScope.launch {
            data.forEach { factsModel ->
                localDS.insertEntity(setData(factsModel))
            }
        }
    }

    private fun setData(factsModel: FactsModel): FactsEntity {
        return FactsEntity(
            id = 0,
            id_api = factsModel._id,
            columns = factsModel.columns,
            createdAt = factsModel.createdAt,
            dataset = factsModel.dataset,
            fact = factsModel.fact,
            dateInsert = factsModel.date_insert,
            operations = factsModel.operations,
            organization = factsModel.organization,
            resource = factsModel.resource,
            slug = factsModel.slug,
            url = factsModel.url
        )
    }
}