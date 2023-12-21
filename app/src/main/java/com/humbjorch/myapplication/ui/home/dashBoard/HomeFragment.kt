package com.humbjorch.myapplication.ui.home.dashBoard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.humbjorch.myapplication.R
import com.humbjorch.myapplication.data.datSource.ResponseStatus
import com.humbjorch.myapplication.data.model.FactsEntity
import com.humbjorch.myapplication.databinding.FragmentHomeBinding
import com.humbjorch.myapplication.sis.utils.alerts.CustomToastWidget
import com.humbjorch.myapplication.sis.utils.alerts.TypeToast
import com.humbjorch.myapplication.sis.utils.loadImageUrl
import com.humbjorch.myapplication.ui.home.HomeViewModel
import com.humbjorch.myapplication.ui.home.MainActivity
import com.humbjorch.myapplication.ui.home.MainActivity.Companion.CHANGE_HOME_LIST
import com.humbjorch.myapplication.ui.home.dashBoard.adapter.FactsAdapter
import com.humbjorch.myapplication.ui.login.LoginSessionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private val sessionViewModel: LoginSessionViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var factAdapter: FactsAdapter
    private var factList: List<FactsEntity> = listOf()

    private val onBackPressed = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            (activity as MainActivity).genericAlert(
                titleAlert = getString(R.string.custom_dialog_title),
                descriptionAlert = getString(R.string.custom_dialog_body),
                txtBtnNegativeAlert = getString(R.string.cancel_dialog),
                txtBtnPositiveAlert = getString(R.string.dialog_confirm),
                buttonPositiveAction = {
                    (activity as MainActivity).finish()
                },
                buttonNegativeAction = {

                }
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.saveLastLocation()
        viewModel.getFavoritesFacts()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FragmentHomeBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressed)
        binding.imgPhotoProfile.loadImageUrl(sessionViewModel.getImageUrl())
        setAdapter()
        setObservers()
        setListenerActions()
    }

    private fun setListenerActions() {
        binding.btnAllPost.setOnClickListener {
            val action = HomeFragmentDirections.actionNavigationHomeToAllListFragment()
            binding.root.findNavController().navigate(action)
        }

        binding.imgProfile.setOnClickListener {
            (activity as MainActivity).genericAlert(
                titleAlert = getString(R.string.custom_sign_off),
                descriptionAlert = getString(R.string.custom_dialog_session),
                txtBtnNegativeAlert = getString(R.string.cancel_dialog),
                txtBtnPositiveAlert = getString(R.string.dialog_confirm),
                buttonPositiveAction = {
                    viewModel.singOut()
                },
                buttonNegativeAction = {

                }
            )
        }
    }

    private fun setObservers() {
        viewModel.getFavoriteFactsLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseStatus.Loading -> {
                    (activity as MainActivity).showLoader()
                }

                is ResponseStatus.Success -> {
                    if ((it.data as List<FactsEntity>).isNotEmpty()) {
                        factList = it.data
                        factAdapter.updateList(factList)
                    } else {
                        factAdapter.updateList(emptyList())
                    }
                    binding.root.postDelayed({
                        (activity as MainActivity).dismissLoader()
                    }, 3000)
                }

                is ResponseStatus.Error -> {
                    (activity as MainActivity).dismissLoader()
                    CustomToastWidget.show(
                        activity = requireActivity(),
                        message = it.messageId,
                        type = TypeToast.ERROR
                    )
                }

                null -> {}
            }
        }

        viewModel.singOutLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseStatus.Loading -> {}

                is ResponseStatus.Success -> {
                    if ((it.data as Boolean))
                    CustomToastWidget.show(
                        requireActivity(),
                        getString(R.string.message_exit),
                        TypeToast.SUCCESS
                    )
                    (activity as MainActivity).goToLogin()
                }

                is ResponseStatus.Error -> {
                    CustomToastWidget.show(
                        activity = requireActivity(),
                        message = it.messageId,
                        type = TypeToast.ERROR
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (CHANGE_HOME_LIST) {
            viewModel.getFavoritesFacts()
            CHANGE_HOME_LIST = false
        }
    }

    private fun setAdapter() {
        factAdapter = FactsAdapter(
            dataSet = factList,
            onClick = { fact ->
                (activity as MainActivity).checkLocation(action = {
                    val action = HomeFragmentDirections.actionNavigationHomeToDetailFragment(fact)
                    binding.root.findNavController().navigate(action)
                }, checkConnection = viewModel.checkLocation())
            }
        )
        binding.rvFacts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = factAdapter
        }
    }

}