package com.humbjorch.myapplication.ui.login

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.humbjorch.myapplication.R
import com.humbjorch.myapplication.sis.utils.HelperGeolocation
import com.humbjorch.myapplication.sis.utils.alerts.CustomToastWidget
import com.humbjorch.myapplication.sis.utils.alerts.GenericDialog
import com.humbjorch.myapplication.sis.utils.alerts.LoaderNBEXWidget
import com.humbjorch.myapplication.sis.utils.alerts.TypeToast
import com.humbjorch.myapplication.sis.utils.isPermissionGranted
import com.humbjorch.myapplication.sis.utils.launchTimer
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val REQUEST_CODE = 100
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val loader by lazy { LoaderNBEXWidget() }
    var START_DESTINATION_PASSWORD = false
    //var hasPermission = false

    private val permissionList =
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    @Inject
    lateinit var helperGeolocation: HelperGeolocation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportFragmentManager.findFragmentById(R.id.fragmentContainerHome) as NavHostFragment
        this.launchTimer()

        if (!isPermissionGranted()) {
            requestPermission()
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            permissionList,
            REQUEST_CODE
        )
    }

    fun checkLocation(action: () -> Unit) {
        if (isPermissionGranted()) {
            if (helperGeolocation.isEnableGeolocation()) {
                action()
            } else {
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
            requestPermission()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    val locationOneAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val locationTwoAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED
                    if (locationOneAccepted || locationTwoAccepted) {
                        CustomToastWidget.show(
                            activity = this,
                            message = getString(R.string.label_permission_success),
                            type = TypeToast.SUCCESS
                        )
                    } else {
                        finish()
                    }
                }
            }
        }
    }
}