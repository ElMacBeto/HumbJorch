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
import com.humbjorch.myapplication.R
import com.humbjorch.myapplication.data.datSource.ResponseStatus
import com.humbjorch.myapplication.data.local.AuthenticationResponse
import com.humbjorch.myapplication.databinding.FragmentLoginFirstTimeBinding
import com.humbjorch.myapplication.sis.utils.alerts.CustomToastWidget
import com.humbjorch.myapplication.sis.utils.alerts.TypeToast.ERROR
import com.humbjorch.myapplication.sis.utils.alerts.TypeToast.SUCCESS
import com.humbjorch.myapplication.ui.home.MainActivity
import com.humbjorch.myapplication.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFirstTimeFragment : Fragment() {
    private lateinit var binding: FragmentLoginFirstTimeBinding
    private val viewModel: LoginFirstTimeViewModel by viewModels()

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
}