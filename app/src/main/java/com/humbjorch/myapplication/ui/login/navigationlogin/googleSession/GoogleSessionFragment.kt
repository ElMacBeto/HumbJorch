package com.humbjorch.myapplication.ui.login.navigationlogin.googleSession

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.humbjorch.myapplication.R
import com.humbjorch.myapplication.databinding.FragmentGoogleSesionBinding
import com.humbjorch.myapplication.databinding.FragmentSplashLoginBinding
import com.humbjorch.myapplication.sis.utils.loadImageUrl
import com.humbjorch.myapplication.ui.login.LoginSessionViewModel
import com.humbjorch.myapplication.ui.login.navigationlogin.splash.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GoogleSessionFragment : Fragment() {
    private lateinit var binding: FragmentGoogleSesionBinding
    private val viewModel: LoginSessionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentGoogleSesionBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }



    private val onBackPressed = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressed)
        binding.imgPhotoProfile.loadImageUrl(viewModel.getImageUrl())
    }

}