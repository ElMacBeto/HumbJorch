package com.humbjorch.myapplication.sis.utils

import android.util.Patterns
import android.view.View
import com.google.android.material.textfield.TextInputLayout
import com.humbjorch.myapplication.App
import com.humbjorch.myapplication.R
import java.util.regex.Pattern

object HelperValidations {

    enum class Validations {
        NAME,
        EMAIL,
        PASSWORD
    }

    fun setOnFocusListener(textInputLayout: TextInputLayout, validation: Validations) =
        View.OnFocusChangeListener { _, hasFocus ->
            val text = textInputLayout.editText!!.text.toString()
            if (!hasFocus) {
                if (text.isNotEmpty()) {
                    if (validate(text, validation)) {
                        textInputLayout.isErrorEnabled = false
                        textInputLayout.error = null
                    } else {
                        textInputLayout.isErrorEnabled = true
                        textInputLayout.error = getValidationErrorMessage(validation)
                    }
                }
            } else {
                textInputLayout.isErrorEnabled = false
                textInputLayout.error = null
            }
        }

    private fun validate(inputText: String, validation: Validations): Boolean {
        return when (validation) {
            Validations.NAME -> {
                isValidName(inputText)
            }

            Validations.EMAIL -> {
                isValidEmail(inputText)
            }

            Validations.PASSWORD -> {
                isValidPassword(inputText)
            }
        }
    }

    private fun getValidationErrorMessage(validation: Validations): String {
        return when (validation) {
            Validations.NAME -> {
                App.instance.getString(R.string.error_name)
            }

            Validations.EMAIL -> {
                App.instance.getString(R.string.error_email)
            }

            Validations.PASSWORD -> {
                App.instance.getString(R.string.error_email)
            }
        }
    }

    fun isValidName(name: String): Boolean {
        return name.length in 2..30
    }

    fun isValidEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        return password.length > 3
    }

}