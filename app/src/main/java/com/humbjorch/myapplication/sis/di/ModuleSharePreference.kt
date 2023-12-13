package com.humbjorch.myapplication.sis.di

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.humbjorch.myapplication.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModuleSharePreference @Inject constructor(private val context: Context) {
    private val shareSessionPreferences = context.getSharedPreferences(
        context.getString(R.string.share_preferences_session),
        Context.MODE_PRIVATE
    )

    private val sharedPreferencesSessionEdit = shareSessionPreferences.edit()


    @SuppressLint("CommitPrefEdits")
    fun saveEmailAndPassword(email: String, photo: String) {
        sharedPreferencesSessionEdit.putString(context.getString(R.string.share_preferences_email), email)
        sharedPreferencesSessionEdit.putString(
            context.getString(R.string.share_preferences_photo_url),
            photo
        )
        sharedPreferencesSessionEdit.apply()
    }

    @SuppressLint("CommitPrefEdits")
    fun saveTouchId(touchId: Boolean) {
        sharedPreferencesSessionEdit.putBoolean(
            context.getString(R.string.share_preferences_touch_id_session),
            touchId
        )
        sharedPreferencesSessionEdit.apply()
    }
    @SuppressLint("CommitPrefEdits")
    fun saveGoogleSession(touchId: Boolean) {
        sharedPreferencesSessionEdit.putBoolean(
            context.getString(R.string.share_preferences_google_session),
            touchId
        )
        sharedPreferencesSessionEdit.apply()
    }


    fun getEmail() = shareSessionPreferences.getString(
        context.getString(R.string.share_preferences_email),
        ""
    )

    fun getPhoto() = shareSessionPreferences.getString(
        context.getString(R.string.share_preferences_photo_url),
        ""
    )
    fun getTouchId() = shareSessionPreferences.getBoolean(
        context.getString(R.string.share_preferences_touch_id_session),
        false
    )
    fun getGoogleSession() = shareSessionPreferences.getBoolean(
        context.getString(R.string.share_preferences_google_session),
        false
    )

    @SuppressLint("CommitPrefEdits")
    fun clearPreferences() {
        sharedPreferencesSessionEdit.clear()
        sharedPreferencesSessionEdit.apply()
    }
}