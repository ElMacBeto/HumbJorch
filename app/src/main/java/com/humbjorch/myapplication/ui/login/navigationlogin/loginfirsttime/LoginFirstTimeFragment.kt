package com.humbjorch.myapplication.ui.login.navigationlogin.loginfirsttime


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import com.humbjorch.myapplication.R
import com.humbjorch.myapplication.data.datSource.ResponseStatus
import com.humbjorch.myapplication.databinding.FragmentLoginFirstTimeBinding
import com.humbjorch.myapplication.sis.utils.alerts.CustomToastWidget
import com.humbjorch.myapplication.sis.utils.alerts.TypeToast.ERROR
import com.humbjorch.myapplication.sis.utils.alerts.TypeToast.SUCCESS
import com.humbjorch.myapplication.ui.home.MainActivity
import com.humbjorch.myapplication.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import com.humbjorch.myapplication.sis.utils.BiometricsUtils
import com.humbjorch.myapplication.sis.utils.HelperValidations

@AndroidEntryPoint
class LoginFirstTimeFragment : Fragment() {
    private lateinit var binding: FragmentLoginFirstTimeBinding
    private val viewModel: LoginFirstTimeViewModel by viewModels()
    //validations flags
    private var emailFlag = false
    private var passwordFlag = false

    private val responseLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                viewModel.launchGoogle(it.data!!)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentLoginFirstTimeBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerLiveData()
        setListenerActions()

        setWatchers()
        setOnFocus()
    }

    private fun setListenerActions() {
        binding.btnRegister.setOnClickListener {
            viewModel.createNewRegister(
                binding.etEmail.text.toString(),
                binding.etPass.text.toString()
            )
        }

        binding.btnLogin.setOnClickListener {
            viewModel.loginAccount(
                binding.etEmail.text.toString(),
                binding.etPass.text.toString()
            )
        }

        binding.containerGoogleLogin.setOnClickListener {
            val intent = viewModel.getClientProvide().signInIntent
            responseLauncher.launch(intent)
        }

        binding.swFingerprint.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                showFingerPrint()
            }
        }
    }

    private fun observerLiveData() {
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
                            type = SUCCESS
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
                            type = ERROR
                        )
                    }
                }
            }
        }
    }

    private fun setWatchers() {
        binding.tilEmail.editText!!.doAfterTextChanged { inputText ->
            val email = inputText.toString()
            binding.tilEmail.isErrorEnabled = false
            binding.tilEmail.error = null
            emailFlag = HelperValidations.isValidEmail(email)
            viewModel.userData.name = email
            activeButton()
        }
        binding.tilPass.editText!!.doAfterTextChanged { inputText ->
            val password = inputText.toString()
            binding.tilPass.isErrorEnabled = false
            binding.tilPass.error = null
            passwordFlag = HelperValidations.isValidPassword(password)
            viewModel.userData.email = password
            activeButton()
        }
    }

    private fun setOnFocus() {
        binding.tilEmail.editText!!.onFocusChangeListener =
            HelperValidations.setOnFocusListener(
                binding.tilEmail,
                HelperValidations.Validations.EMAIL
            )

        binding.tilPass.editText!!.onFocusChangeListener =
            HelperValidations.setOnFocusListener(
                binding.tilPass,
                HelperValidations.Validations.PASSWORD
            )
    }

    private fun activeButton() {
        val isEnable = emailFlag && passwordFlag
        binding.btnRegister.isEnabled = isEnable
        binding.swFingerprint.isEnabled = isEnable
    }

    private fun showFingerPrint() {
        BiometricsUtils.showBiometricPrompt(this) {
            if (it) {
                Toast.makeText(requireContext(), "autorizado", Toast.LENGTH_SHORT).show()
            } else {
                binding.swFingerprint.isChecked = false
            }
        }
    }

}