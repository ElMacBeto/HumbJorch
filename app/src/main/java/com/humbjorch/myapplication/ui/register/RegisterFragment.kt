package com.humbjorch.myapplication.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.humbjorch.myapplication.databinding.FragmentRegisterBinding
import com.humbjorch.myapplication.sis.utils.BiometricsUtils
import com.humbjorch.myapplication.sis.utils.HelperValidations
import com.humbjorch.myapplication.sis.utils.HelperValidations.isValidEmail
import com.humbjorch.myapplication.sis.utils.HelperValidations.isValidName
import com.humbjorch.myapplication.sis.utils.HelperValidations.isValidPassword
import com.humbjorch.myapplication.sis.utils.HelperValidations.setOnFocusListener

class RegisterFragment : Fragment() {


    private val viewModel: RegisterViewModel by viewModels()
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    //validation flags
    private var nameFlag = false
    private var emailFlag = false
    private var passwordFlag = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setWatchers()
        setOnFocus()
        setListeners()
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

    private fun showFingerPrint() {
        BiometricsUtils.showBiometricPrompt(this) {
            if (it) {
                Toast.makeText(requireContext(), "autorizado", Toast.LENGTH_SHORT).show()
            } else {
                binding.swFingerprint.isChecked = false
            }
        }
    }

    private fun setWatchers() {
        binding.tiUserName.editText!!.doAfterTextChanged { inputText ->
            val userName = inputText.toString()
            binding.tiUserName.isErrorEnabled = false
            binding.tiUserName.error = null
            nameFlag = isValidName(userName)
            viewModel.userData.name = userName
            activeButton()
        }
        binding.tiEmail.editText!!.doAfterTextChanged { inputText ->
            val email = inputText.toString()
            binding.tiEmail.isErrorEnabled = false
            binding.tiEmail.error = null
            emailFlag = isValidEmail(email)
            viewModel.userData.email = email
            activeButton()
        }
        binding.tiPassword.editText!!.doAfterTextChanged { inputText ->
            val password = inputText.toString()
            binding.tiEmail.isErrorEnabled = false
            binding.tiEmail.error = null
            passwordFlag = isValidPassword(password)
            viewModel.userData.email = password
            activeButton()
        }
    }

    private fun setOnFocus() {
        binding.tiUserName.editText!!.onFocusChangeListener =
            setOnFocusListener(binding.tiUserName, HelperValidations.Validations.NAME)

        binding.tiEmail.editText!!.onFocusChangeListener =
            setOnFocusListener(binding.tiEmail, HelperValidations.Validations.EMAIL)

        binding.tiPassword.editText!!.onFocusChangeListener =
            setOnFocusListener(binding.tiPassword, HelperValidations.Validations.PASSWORD)
    }

    private fun activeButton() {
        val isEnable = nameFlag && emailFlag && passwordFlag
        binding.btnRegister.isEnabled = isEnable
        binding.swFingerprint.isEnabled = isEnable
    }

}