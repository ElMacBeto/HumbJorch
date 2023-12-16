package com.humbjorch.myapplication.ui.login

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.humbjorch.myapplication.R
import com.humbjorch.myapplication.sis.utils.alerts.GenericDialog
import com.humbjorch.myapplication.sis.utils.alerts.LoaderNBEXWidget
import com.humbjorch.myapplication.sis.utils.launchTimer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val loader by lazy { LoaderNBEXWidget() }
    var START_DESTINATION_PASSWORD = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportFragmentManager.findFragmentById(R.id.fragmentContainerHome) as NavHostFragment

        this.launchTimer()
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


    fun genericAlert(
        imageAlert: Int = R.drawable.generic_icon_warning,
        titleAlert: String,
        descriptionAlert: String,
        txtBtnPositiveAlert: String,
        txtBtnNegativeAlert: String,
        isCancelableAlert: Boolean = false,
        buttonPositiveAction: (() -> Unit)? = null,
        buttonNegativeAction: (() -> Unit)? = null,
    ) {
        lifecycleScope.launchWhenResumed {
            GenericDialog().apply {
                imgDialog = imageAlert
                txtConfirm = txtBtnPositiveAlert
                txtCancel = txtBtnNegativeAlert
                txtTitle = titleAlert
                txtMessageAlert = descriptionAlert
                isCancelable = isCancelableAlert
                listener = object : GenericDialog.OnClickListener {
                    override fun onClick(which: Int) {
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                                buttonPositiveAction?.invoke()
                            }

                            else -> {
                                buttonNegativeAction?.invoke()
                            }
                        }
                    }
                }
                this.show(supportFragmentManager, System.currentTimeMillis().toString())
            }
        }
    }
}