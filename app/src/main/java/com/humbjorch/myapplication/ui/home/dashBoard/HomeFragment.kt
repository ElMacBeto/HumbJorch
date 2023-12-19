package com.humbjorch.myapplication.ui.home.dashBoard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getFavoritesFacts(0)
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
        binding.imgPhotoProfile.loadImageUrl(sessionViewModel.getImageUrl())
        setAdapter()
        setObservers()
        setListenerActions()
    }

    private fun setListenerActions() {
        binding.btnAllPost.setOnClickListener {
            val action = HomeFragmentDirections.actionNavigationHomeToAllListFragment(false)
            binding.root.findNavController().navigate(action)
        }

        binding.allFavorite.setOnClickListener {
            val action = HomeFragmentDirections.actionNavigationHomeToAllListFragment(true)
            binding.root.findNavController().navigate(action)
        }
    }

    private fun setObservers() {
        viewModel.getFavoriteFactsLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseStatus.Loading -> {
                    (activity as MainActivity).showLoader()
                }

                is ResponseStatus.Success -> {
                    if((it.data as List<FactsEntity>).isNotEmpty()){
                        factList = it.data
                        factAdapter.updateList(factList)
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
            }
        }
    }

    private fun setAdapter() {
        factAdapter = FactsAdapter(
            dataSet = factList,
            onClick = { fact ->
                binding.root.findNavController().navigate(R.id.action_navigation_home_to_detailFragment)
            }
        )
        binding.rvFacts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = factAdapter
        }
    }

}