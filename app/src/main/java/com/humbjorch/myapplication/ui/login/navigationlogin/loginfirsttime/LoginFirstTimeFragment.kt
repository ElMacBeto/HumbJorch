package com.humbjorch.myapplication.ui.login.navigationlogin.loginfirsttime

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.humbjorch.myapplication.R
import com.humbjorch.myapplication.databinding.FragmentLoginFirstTimeBinding
import com.humbjorch.myapplication.databinding.FragmentRegisterBinding
import com.humbjorch.myapplication.sis.utils.BiometricsUtils
import com.humbjorch.myapplication.sis.utils.HelperValidations

class LoginFirstTimeFragment : Fragment() {


    private val viewModel: LoginFirstTimeViewModel by viewModels()
    private var _binding: FragmentLoginFirstTimeBinding? = null
    private val binding get() = _binding!!

    //validations flags
    private var emailFlag = false
    private var passwordFlag = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginFirstTimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        setWatchers()
        setOnFocus()
    }

    private fun setListeners() {
        binding.btnRegister.setOnClickListener {
            viewModel.registerNewUser()
        }

        binding.swFingerprint.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                showFingerPrint()
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