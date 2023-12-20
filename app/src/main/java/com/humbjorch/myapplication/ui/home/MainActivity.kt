package com.humbjorch.myapplication.ui.home

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.humbjorch.myapplication.R
import com.humbjorch.myapplication.sis.utils.alerts.GenericDialog
import com.humbjorch.myapplication.sis.utils.alerts.LoaderNBEXWidget
import com.humbjorch.myapplication.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        var CHANGE_HOME_LIST = false
    }

    private val loader by lazy { LoaderNBEXWidget() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment

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

    fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
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

    fun checkLocation(action: () -> Unit, checkConnection: Boolean) {
        if (checkConnection) {
            action()
        } else {
            genericAlert(
                titleAlert = getString(R.string.alert_geolocation_title),
                descriptionAlert = getString(R.string.alert_geolocation_positive_button),
                txtBtnNegativeAlert = getString(R.string.cancel_dialog),
                txtBtnPositiveAlert = getString(R.string.dialog_confirm),
                buttonPositiveAction = {
                    startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                },
                buttonNegativeAction = {

                }
            )
        }
    }
}