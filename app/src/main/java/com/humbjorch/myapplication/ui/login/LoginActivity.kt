package com.humbjorch.myapplication.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.humbjorch.myapplication.R
import com.humbjorch.myapplication.sis.utils.alerts.LoaderNBEXWidget
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val loader by lazy { LoaderNBEXWidget() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportFragmentManager.findFragmentById(R.id.fragmentContainerHome) as NavHostFragment
    }

    fun showLoader() {
        try {
            val loaderDialog = supportFragmentManager.findFragmentByTag("Loader")
            val isShowing = loader.dialog?.isShowing ?: false
            if (loaderDialog != null && loaderDialog.isAdded) {
                return
            }

            if (!loader.isAdded && !loader.isVisible && !isShowing) {
                loader.show(supportFragmentManager, "Loader")
                supportFragmentManager.executePendingTransactions()
            }
        } catch (e: Exception) {
            //ERROR
        }
    }

    fun dismissLoader() {
        if (loader.isAdded) {
            if (loader.isResumed) {
                loader.dismiss()
            } else {
                loader.dismissAllowingStateLoss()
            }
        }
    }
}