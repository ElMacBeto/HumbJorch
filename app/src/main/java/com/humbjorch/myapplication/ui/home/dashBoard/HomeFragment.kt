package com.humbjorch.myapplication.ui.home.dashBoard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.humbjorch.myapplication.data.model.FactsModel
import com.humbjorch.myapplication.databinding.FragmentHomeBinding
import com.humbjorch.myapplication.sis.utils.loadImageUrl
import com.humbjorch.myapplication.ui.home.dashBoard.adapter.FactsAdapter
import com.humbjorch.myapplication.ui.login.LoginSessionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private val sessionViewModel: LoginSessionViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var factAdapter: FactsAdapter
    private var factList: List<FactsModel> = listOf()

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
        viewModel.getFacts()
    }

    private fun setObservers() {
        viewModel.getAllFactsLiveData.observe(viewLifecycleOwner){
            factList = listOf(
                FactsModel("1","sdf",23,"dfsdfsdfsdfggfg", "324234", "fvcbtrbvbgffbfghgff", "wervzdfadfa", "ewewerwer", "wewerwersfd", "hola mundo", "www.asdasdas.com"),
                FactsModel("2","sdf",23,"dfsdfsdfsdfggfg", "324234", "fvcbtrbvbgffbfghgff", "wervzdfadfa", "ewewerwer", "wewerwersfd", "hola mundo", "www.asdasdas.com"),
                FactsModel("3","sdf",23,"dfsdfsdfsdfggfg", "324234", "fvcbtrbvbgffbfghgff", "wervzdfadfa", "ewewerwer", "wewerwersfd", "hola mundo", "www.asdasdas.com"),
                FactsModel("4","sdf",23,"dfsdfsdfsdfggfg", "324234", "fvcbtrbvbgffbfghgff", "wervzdfadfa", "ewewerwer", "wewerwersfd", "hola mundo", "www.asdasdas.com"),
                FactsModel("5","sdf",23,"dfsdfsdfsdfggfg", "324234", "fvcbtrbvbgffbfghgff", "wervzdfadfa", "ewewerwer", "wewerwersfd", "hola mundo", "www.asdasdas.com")
            )
            factAdapter.updateList(factList)
        }
    }

    private fun setAdapter() {
        factAdapter = FactsAdapter(
            dataSet = factList,
            onClick = { fact ->
                Toast.makeText(requireContext(), fact.id, Toast.LENGTH_SHORT).show()
            }
        )
        binding.rvFacts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = factAdapter
        }
    }

}