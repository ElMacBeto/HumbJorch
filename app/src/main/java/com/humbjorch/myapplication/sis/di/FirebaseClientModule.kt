package com.humbjorch.myapplication.sis.di


import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.humbjorch.myapplication.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseClientModule @Inject constructor(context: Context) {
    val getInstance = FirebaseAuth.getInstance()
    fun getInstanceFirebaseAuth(): FirebaseAuth = Firebase.auth

    private val gson = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    val googleSing = GoogleSignIn.getClient(context, gson)

    fun signOutFirebase() = getInstance.signOut()
    fun signOutGoogle() = googleSing.signOut()
}