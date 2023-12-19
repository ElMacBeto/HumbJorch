package com.humbjorch.myapplication.ui.home.allList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.humbjorch.myapplication.data.datSource.ResponseStatus
import com.humbjorch.myapplication.data.model.FactsEntity
import com.humbjorch.myapplication.databinding.FragmentAllListBinding
import com.humbjorch.myapplication.sis.utils.alerts.CustomToastWidget
import com.humbjorch.myapplication.sis.utils.alerts.TypeToast
import com.humbjorch.myapplication.ui.home.HomeViewModel
import com.humbjorch.myapplication.ui.home.MainActivity
import com.humbjorch.myapplication.ui.home.dashBoard.adapter.FactsAdapter
import com.humbjorch.myapplication.ui.home.detail.DetailFragment
import com.humbjorch.myapplication.ui.home.detail.FACT
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllListFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentAllListBinding
    private lateinit var factAdapter: FactsAdapter
    private var factList: List<FactsEntity> = listOf()
    private var isFavoriteView: Boolean = false
    private var stopPagination: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isFavoriteView = it.getBoolean("isVavoriteView")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllListBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSearchView()
        setAdapter()
        setObservers()
        getFactList()
    }

    private fun getFactList() {
        if (factList.isNotEmpty())
            return
        if (isFavoriteView)
            viewModel.getFavoritesFacts(0)
        else
            viewModel.getAllFacts(0)
    }

    private fun setObservers() {
        viewModel.getFavoriteFactsLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseStatus.Loading -> {
                    (activity as MainActivity).showLoader()
                }

                is ResponseStatus.Success -> {
                    val listBD = (it.data as List<FactsEntity>)

                    if (listBD.isNotEmpty()) {
                        it.data.forEach { fact ->
                            factList = factList.plus(fact)
                        }
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

                null -> {}
            }
        }
    }

    private fun setAdapter() {
        factAdapter = FactsAdapter(
            dataSet = factList,
            onClick = { fact ->
                val action = AllListFragmentDirections.actionAllListFragmentToDetailFragment(fact)
                binding.root.findNavController().navigate(action)
            }
        )

        factAdapter.onChargePage = {
            if (stopPagination)
                viewModel.getAllFacts(factAdapter.itemCount)
        }

        binding.rvFacts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = factAdapter
        }
    }

    private fun setSearchView() {
        binding.svFacts.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(input: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(input: String?): Boolean {
                if (!input.isNullOrEmpty()) {
                    stopPagination = false
                    val filterList = getFilterList(input)
                    factAdapter.updateList(filterList)
                } else {
                    stopPagination = true
                    factAdapter.updateList(factList)
                }
                return true
            }
        })
    }

    private fun getFilterList(query: String): List<FactsEntity> {
        val list = factList
        return list.filter { fact ->
            fact.slug?.lowercase()!!.contains(query.lowercase()) ||
                    fact.fact?.lowercase()!!.contains(query.lowercase()) ||
                    fact.id_api?.lowercase()!!.contains(query.lowercase())
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.clearLivedata()
    }
}