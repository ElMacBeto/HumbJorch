package com.humbjorch.myapplication.domain

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.humbjorch.myapplication.R
import com.humbjorch.myapplication.data.datSource.ResponseStatus
import com.humbjorch.myapplication.data.datSource.makeFirebaseCall
import com.humbjorch.myapplication.data.local.AuthenticationResponse
import com.humbjorch.myapplication.sis.di.FirebaseClientModule
import com.humbjorch.myapplication.sis.di.ModuleSharePreference
import com.humbjorch.myapplication.sis.utils.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface ConnectionTask {
    suspend fun singInWithGoogle(
        data: Intent
    ): ResponseStatus<AuthenticationResponse>

    suspend fun createNewRegister(
        user: String,
        password: String,
        touchId: Boolean
    ): ResponseStatus<AuthenticationResponse>

    fun getClientProvide(): GoogleSignInClient

    suspend fun loginUser(
        user: String,
        password: String,
        touchId: Boolean
    ): ResponseStatus<AuthenticationResponse>
}

@Singleton
class NewAuthenticationRepository @Inject constructor(
    private val auth: FirebaseClientModule,
    private val moduleSharePreference: ModuleSharePreference
) : ConnectionTask {
    private val authFirebase = auth.getInstanceFirebaseAuth()

    /***
     *  GOOGLE
     */

    override suspend fun singInWithGoogle(
        data: Intent
    ): ResponseStatus<AuthenticationResponse> {
        return withContext(Dispatchers.IO) {
            val getAuthGoogle = async { getAuthGoogle(data) }
            val getAuthResponse = getAuthGoogle.await()

            if (getAuthResponse is ResponseStatus.Success) {
                ResponseStatus.Success(getAuthResponse.data)
            } else {
                ResponseStatus.Error((getAuthResponse as ResponseStatus.Error).messageId)
            }
        }
    }

    private suspend fun getAuthGoogle(
        data: Intent
    ): ResponseStatus<AuthenticationResponse> =
        makeFirebaseCall {
            val response = authWithGoogle(data)
            response
        }


    private suspend fun authWithGoogle(data: Intent): AuthenticationResponse {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        val googleInAccount = task.getResult(ApiException::class.java)
        val idToken = googleInAccount.idToken
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return auth.getInstance.signInWithCredential(credential).await().let {
            moduleSharePreference.saveEmailAndPassword(
                authFirebase.currentUser!!.email ?: "",
                if (authFirebase.currentUser?.photoUrl == null) Constants.PHOTO_AUTHENTICATION_DEFAULT else authFirebase.currentUser?.photoUrl.toString()
            )
            moduleSharePreference.saveGoogleSession(true)
            AuthenticationResponse(
                userEmail = authFirebase.currentUser?.email,
                photoUrl = authFirebase.currentUser?.photoUrl.toString()
            )
        }
    }


    /***
     * REGISTER
     */

    override suspend fun createNewRegister(
        user: String,
        password: String,
        touchId: Boolean
    ): ResponseStatus<AuthenticationResponse> {
        return withContext(Dispatchers.IO) {
            val getAuthRegister = async { getAuthRegister(user, password, touchId) }
            val getAuthRegisterResponse = getAuthRegister.await()

            if (getAuthRegisterResponse is ResponseStatus.Success) {
                ResponseStatus.Success(getAuthRegisterResponse.data)
            } else {
                ResponseStatus.Error((getAuthRegisterResponse as ResponseStatus.Error).messageId)
            }
        }
    }

    private suspend fun getAuthRegister(
        user: String,
        password: String,
        touchId: Boolean
    ): ResponseStatus<AuthenticationResponse> = makeFirebaseCall {
        val response = authRegisterFirebase(user, password, touchId)
        response
    }

    private suspend fun authRegisterFirebase(
        user: String,
        password: String,
        touchId: Boolean
    ): AuthenticationResponse {
        return authFirebase.createUserWithEmailAndPassword(user, password).await().let {
            moduleSharePreference.saveEmailAndPassword(
                authFirebase.currentUser!!.email ?: "",
                if (authFirebase.currentUser?.photoUrl == null) Constants.PHOTO_AUTHENTICATION_DEFAULT else authFirebase.currentUser?.photoUrl.toString()
            )
            moduleSharePreference.saveTouchId(touchId)
            AuthenticationResponse(
                userEmail = authFirebase.currentUser?.email,
                photoUrl = authFirebase.currentUser?.photoUrl.toString()
            )
        }
    }

    /***
     * LOG IN
     */

    override suspend fun loginUser(
        user: String,
        password: String,
        touchId: Boolean
    ): ResponseStatus<AuthenticationResponse> {
        return withContext(Dispatchers.IO) {
            val getAuthLogin = async { getAuthLogin(user, password, touchId) }
            val getAuthLoginResponse = getAuthLogin.await()

            if (getAuthLoginResponse is ResponseStatus.Success) {
                ResponseStatus.Success(getAuthLoginResponse.data)
            } else {
                ResponseStatus.Error((getAuthLoginResponse as ResponseStatus.Error).messageId)
            }
        }
    }

    private suspend fun getAuthLogin(user: String, password: String, touchId: Boolean) =
        makeFirebaseCall {
            val response = authLoginFirebase(user, password, touchId)
            response
        }

    private suspend fun authLoginFirebase(
        user: String,
        password: String,
        touchId: Boolean
    ): AuthenticationResponse {
        return authFirebase.signInWithEmailAndPassword(user, password).await().let {
            moduleSharePreference.saveEmailAndPassword(
                authFirebase.currentUser!!.email ?: "",
                if (authFirebase.currentUser?.photoUrl == null) Constants.PHOTO_AUTHENTICATION_DEFAULT else authFirebase.currentUser?.photoUrl.toString()
            )
            moduleSharePreference.saveTouchId(touchId)
            AuthenticationResponse(
                userEmail = authFirebase.currentUser?.email,
                photoUrl = authFirebase.currentUser?.photoUrl.toString()
            )
        }
    }

    /***
     * PROVIDE INTENT
     */

    override fun getClientProvide(): GoogleSignInClient = auth.googleSing
}