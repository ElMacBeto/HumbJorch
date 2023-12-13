package com.humbjorch.myapplication.ui.login.navigationlogin.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.humbjorch.myapplication.R
import com.humbjorch.myapplication.databinding.FragmentSplashLoginBinding
import com.humbjorch.myapplication.sis.utils.util.Constants
import com.humbjorch.myapplication.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashLoginFragment : Fragment() {
    private lateinit var binding: FragmentSplashLoginBinding
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentSplashLoginBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setDestinationLogin()
        observerLiveData()
    }

    private fun observerLiveData() {
        viewModel.setDestination.observe(viewLifecycleOwner) {
            when (it) {
                Constants.ListenerLoginDestination.DestinationEmailAndPASSWORD -> {
                    (activity as LoginActivity).START_DESTINATION_PASSWORD = true
                    binding.root.findNavController()
                        .navigate(R.id.action_splashLoginFragment_to_loginPasswordFragment)
                }

                Constants.ListenerLoginDestination.DestinationFirstTime -> {
                    binding.root.findNavController()
                        .navigate(R.id.action_splashLoginFragment_to_loginFirstTimeFragment)
                }

                Constants.ListenerLoginDestination.DestinationTouchId -> {
                    binding.root.findNavController()
                        .navigate(R.id.action_splashLoginFragment_to_loginTouchIdFragment)
                }

                Constants.ListenerLoginDestination.DestinationGoogleSession -> {
                    binding.root.findNavController()
                        .navigate(R.id.action_splashLoginFragment_to_googleSesionFragment)
                }
            }
        }
    }
}