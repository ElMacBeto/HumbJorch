package com.humbjorch.myapplication.ui.login.navigationlogin.loginTouchID

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.humbjorch.myapplication.R
import com.humbjorch.myapplication.databinding.FragmentLoginTouchIdBinding
import com.humbjorch.myapplication.databinding.FragmentSplashLoginBinding
import com.humbjorch.myapplication.sis.utils.BiometricsUtils
import com.humbjorch.myapplication.sis.utils.alerts.CustomToastWidget
import com.humbjorch.myapplication.sis.utils.alerts.TypeToast
import com.humbjorch.myapplication.sis.utils.loadImageUrl
import com.humbjorch.myapplication.ui.home.MainActivity
import com.humbjorch.myapplication.ui.login.LoginActivity
import com.humbjorch.myapplication.ui.login.LoginSessionViewModel
import com.humbjorch.myapplication.ui.login.navigationlogin.splash.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginTouchIdFragment : Fragment() {
    private lateinit var binding: FragmentLoginTouchIdBinding
    private val viewModel: LoginSessionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentLoginTouchIdBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    private val onBackPressed = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            (activity as LoginActivity).genericAlert(
                titleAlert = getString(R.string.custom_dialog_title),
                descriptionAlert = getString(R.string.custom_dialog_body),
                txtBtnNegativeAlert = getString(R.string.cancel_dialog),
                txtBtnPositiveAlert = getString(R.string.dialog_confirm),
                buttonPositiveAction = {
                    (activity as LoginActivity).finish()
                },
                buttonNegativeAction = {

                }
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressed)
        (activity as LoginActivity).dismissLoader()
        binding.imgPhotoProfile.loadImageUrl(viewModel.getImageUrl())
        setListenerAction()
    }

    private fun setListenerAction() {
        binding.btnWithEmail.setOnClickListener {
            binding.root.findNavController().navigate(R.id.action_loginTouchIdFragment_to_loginPasswordFragment)
        }
        binding.btnTouchId.setOnClickListener {
            showFingerPrint()
        }
    }

    private fun showFingerPrint() {
        BiometricsUtils.showBiometricPrompt(this) {
            if (it) {
                CustomToastWidget.show(
                    activity = requireActivity(),
                    message = getString(R.string.welcome_message),
                    type = TypeToast.SUCCESS
                )
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                (activity as LoginActivity).finish()
            } else {
                CustomToastWidget.show(
                    requireActivity(),
                    getString(R.string.touch_error), TypeToast.ERROR
                )
            }
        }
    }
}