package com.humbjorch.myapplication.ui.login

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.humbjorch.myapplication.R
import com.humbjorch.myapplication.sis.utils.HelperGeolocation
import com.humbjorch.myapplication.sis.utils.alerts.GenericDialog
import com.humbjorch.myapplication.sis.utils.alerts.LoaderNBEXWidget
import com.humbjorch.myapplication.sis.utils.launchTimer
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.core.content.ContextCompat.checkSelfPermission
import android.provider.Settings

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val loader by lazy { LoaderNBEXWidget() }
    var START_DESTINATION_PASSWORD = false
    var hasPermission = false

    @Inject
    lateinit var helperGeolocation: HelperGeolocation

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            hasPermission = true
            for (granted in result.values) {
                if (!granted) {
                    hasPermission = false
                    break
                }
            }
            if (hasPermission && !helperGeolocation.isEnableGeolocation()){
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportFragmentManager.findFragmentById(R.id.fragmentContainerHome) as NavHostFragment
        setPermissions()
        this.launchTimer()
    }

    private fun setPermissions() {
        var permissions = arrayOf<String>()

        if (checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                permissions = permissions.plus(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
        if (checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                permissions = permissions.plus(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        }

        if (permissions.isNotEmpty()) {
            requestMultiplePermissions.launch(permissions)
        } else {
            hasPermission = true
        }
    }

    fun checkLocation(action: () -> Unit) {
        if (hasPermission) {
            if (helperGeolocation.isEnableGeolocation()) {
                action()
            }else{
                genericAlert(
                    titleAlert = getString(R.string.alert_geolocation_title),
                    descriptionAlert = getString(R.string.alert_geolocation_positive_button),
                    txtBtnNegativeAlert = getString(R.string.cancel_dialog),
                    txtBtnPositiveAlert = getString(R.string.dialog_confirm),
                    buttonPositiveAction = {
                        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    },
                    buttonNegativeAction = {

                    }
                )
            }
        } else {
            setPermissions()
        }
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