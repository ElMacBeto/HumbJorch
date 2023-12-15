package com.humbjorch.myapplication.sis.utils.alerts

import android.app.Activity
import android.text.SpannableStringBuilder
import androidx.core.text.bold
import androidx.core.text.color
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.humbjorch.myapplication.App
import com.humbjorch.myapplication.R


class ShowDialog {
    fun showAlertDialog(
        title: String,
        message: String,
        context: Activity,
        positiveButton: () -> Unit,
        negativeButton: (() -> Unit)?,
        typeToast: TypeToast
    ) {
        val titleAlert = SpannableStringBuilder()
            .bold { color(getIdColor(typeToast)) { append(title) } }

        val materialDialog = MaterialAlertDialogBuilder(
            context,
            com.google.android.material.R.style.MaterialAlertDialog_MaterialComponents_Title_Icon_CenterStacked
        )
            .setIcon(typeToast.type)
            .setTitle(titleAlert)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(context.getString(R.string.dialog_confirm)) { _, _ ->
                positiveButton()
            }

        if (negativeButton != null) {
            materialDialog.setNegativeButton(context.getString(R.string.cancel_dialog)) { _, _ ->
                negativeButton()
            }
        }

        /*fix to the error
        android.view.WindowManager$BadTokenException: Unable to add window -- token android.os.BinderProxy@d6f21a9 is not valid; is your activity running?*/
        if (!context.isFinishing) {
            materialDialog.show()
        }
    }

    private fun getIdColor(type: TypeToast): Int {
        return when (type) {
            TypeToast.SUCCESS -> App.instance.getColor(R.color.toast_success)
            TypeToast.ERROR -> App.instance.getColor(R.color.toast_error)
            TypeToast.INFORMATION -> App.instance.getColor(R.color.toast_information)
            TypeToast.WARNING -> App.instance.getColor(R.color.toast_waerning)
        }
    }
}