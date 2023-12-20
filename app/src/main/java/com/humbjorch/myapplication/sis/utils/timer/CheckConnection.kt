package com.humbjorch.myapplication.sis.utils.timer

import android.app.Activity
import com.humbjorch.myapplication.R
import com.humbjorch.myapplication.sis.utils.alerts.ShowDialog
import com.humbjorch.myapplication.sis.utils.alerts.TypeToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.TimerTask


class CheckConnection constructor(private val context: Activity) : TimerTask() {
    private val _dialog = ShowDialog()
    private var _conter = 0

    override fun run() {
        if (NetworkUtils.isNetworkAvailable(context)) {
            //CONNECTED
            CoroutineScope(Dispatchers.Main).launch {
                _conter = 0
            }
        } else {
            //DISCONECTED
            _conter++
            CoroutineScope(Dispatchers.Main).launch {
                if (_conter == 1) {
                    _dialog.showAlertDialog(
                        title = context.getString(R.string.dialog_error),
                        message = context.getString(R.string.not_conection),
                        context,
                        positiveButton = {
                            _conter = 0
                        },
                        null,
                        TypeToast.WARNING
                    )
                }

                else if (_conter == 6){
                    context.finish()
                }
            }
        }
    }
}