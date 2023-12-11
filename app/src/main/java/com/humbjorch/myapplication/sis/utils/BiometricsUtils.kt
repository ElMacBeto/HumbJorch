package com.humbjorch.myapplication.sis.utils

import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import java.util.concurrent.Executors

object BiometricsUtils {
    private fun createBiometricPrompt(
        fragment: Fragment,
        processSuccess: (Boolean) -> Unit
    ): BiometricPrompt {
        return BiometricPrompt(
            fragment,
            Executors.newSingleThreadExecutor(),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    fragment.requireActivity().runOnUiThread {
                        processSuccess(false)
                    }
                    super.onAuthenticationError(errorCode, errString)
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    fragment.requireActivity().runOnUiThread {
                        processSuccess(true)
                    }
                    super.onAuthenticationSucceeded(result)
                }

                override fun onAuthenticationFailed() {
                    fragment.requireActivity().runOnUiThread {
                        processSuccess(false)
                    }
                    super.onAuthenticationFailed()
                }
            }
        )
    }


    fun showBiometricPrompt(
        fragment: Fragment,
        processSuccess: (Boolean) -> Unit
    ) {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticacion")
            .setSubtitle("Autenticacion usando fingerprint")
            .setNegativeButtonText("Cancelar")
            .build()
        createBiometricPrompt(fragment, processSuccess).authenticate(promptInfo)
    }

}