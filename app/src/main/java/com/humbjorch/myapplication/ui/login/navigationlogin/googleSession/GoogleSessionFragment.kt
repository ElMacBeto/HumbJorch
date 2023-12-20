package com.humbjorch.myapplication.ui.login.navigationlogin.googleSession

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.humbjorch.myapplication.R
import com.humbjorch.myapplication.data.datSource.ResponseStatus
import com.humbjorch.myapplication.databinding.FragmentGoogleSesionBinding
import com.humbjorch.myapplication.sis.utils.alerts.CustomToastWidget
import com.humbjorch.myapplication.sis.utils.alerts.TypeToast
import com.humbjorch.myapplication.sis.utils.loadImageUrl
import com.humbjorch.myapplication.ui.home.MainActivity
import com.humbjorch.myapplication.ui.login.LoginActivity
import com.humbjorch.myapplication.ui.login.LoginSessionViewModel
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

    private val responseLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                viewModel.launchGoogle(it.data!!)
            }
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

        binding.btnGoggleSession.setOnClickListener {
            (activity as LoginActivity).checkLocation {
                val intent = viewModel.getClientProvide().signInIntent
                responseLauncher.launch(intent)
            }
        }

        setObserverLiveData()
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
}