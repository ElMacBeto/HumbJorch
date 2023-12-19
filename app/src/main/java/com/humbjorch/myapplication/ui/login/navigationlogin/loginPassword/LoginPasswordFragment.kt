package com.humbjorch.myapplication.ui.login.navigationlogin.loginPassword

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.humbjorch.myapplication.R
import com.humbjorch.myapplication.data.datSource.ResponseStatus
import com.humbjorch.myapplication.databinding.FragmentLoginPasswordBinding
import com.humbjorch.myapplication.databinding.FragmentSplashLoginBinding
import com.humbjorch.myapplication.sis.utils.HelperValidations
import com.humbjorch.myapplication.sis.utils.alerts.CustomToastWidget
import com.humbjorch.myapplication.sis.utils.alerts.TypeToast
import com.humbjorch.myapplication.sis.utils.loadImageUrl
import com.humbjorch.myapplication.ui.home.MainActivity
import com.humbjorch.myapplication.ui.login.LoginActivity
import com.humbjorch.myapplication.ui.login.LoginSessionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginPasswordFragment : Fragment() {
    private lateinit var binding: FragmentLoginPasswordBinding
    private val viewModel: LoginSessionViewModel by viewModels()
    private var passwordFlag = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentLoginPasswordBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    private val onBackPressed = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (!(activity as LoginActivity).START_DESTINATION_PASSWORD) {
                binding.root.findNavController().popBackStack()
            }else{
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressed)
        (activity as LoginActivity).dismissLoader()
        binding.imgPhotoProfile.loadImageUrl(viewModel.getImageUrl())
        binding.swFingerprint.isChecked = viewModel.getTouchId()
        setWatchers()
        setObserverLiveData()
        binding.btnLogin.setOnClickListener {
            (activity as LoginActivity).checkLocation {
                viewModel.loginAccount(
                    binding.etPass.text.toString(),
                    binding.swFingerprint.isChecked
                )
            }
        }
    }

    private fun setObserverLiveData() {
        with(viewModel) {
            createRegister.observe(viewLifecycleOwner) {
                when (it) {
                    is ResponseStatus.Loading -> {
                        (activity as LoginActivity).showLoader()
                    }

                    is ResponseStatus.Success -> {
                        (activity as LoginActivity).dismissLoader()
                        CustomToastWidget.show(
                            activity = requireActivity(),
                            message = getString(R.string.welcome_message),
                            type = TypeToast.SUCCESS
                        )
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                        (activity as LoginActivity).finish()
                    }

                    is ResponseStatus.Error -> {
                        (activity as LoginActivity).dismissLoader()
                        CustomToastWidget.show(
                            activity = requireActivity(),
                            message = it.messageId,
                            type = TypeToast.ERROR
                        )
                    }
                }
            }
        }
    }

    private fun setWatchers() {
        binding.tilPass.editText!!.doAfterTextChanged { inputText ->
            val password = inputText.toString()
            binding.tilPass.isErrorEnabled = false
            binding.tilPass.error = null
            passwordFlag = HelperValidations.isValidPassword(password)
            activeButton()
        }
    }

    private fun activeButton() {
        val isEnable = passwordFlag
        binding.btnLogin.isEnabled = isEnable
        binding.swFingerprint.isEnabled = isEnable
    }
}