package com.humbjorch.myapplication.ui.home.dashBoard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.humbjorch.myapplication.data.model.FactsModel
import com.humbjorch.myapplication.databinding.FragmentHomeBinding
import com.humbjorch.myapplication.ui.home.dashBoard.adapter.FactsAdapter

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var factAdapter: FactsAdapter
    private var factList: List<FactsModel> = listOf(
        FactsModel("1","sdf",23,"dfsdfsdfsdfggfg", "324234", "fvcbtrbvbgffbfghgff", "wervzdfadfa", "ewewerwer", "wewerwersfd", "hola mundo", "www.asdasdas.com"),
        FactsModel("2","sdf",23,"dfsdfsdfsdfggfg", "324234", "fvcbtrbvbgffbfghgff", "wervzdfadfa", "ewewerwer", "wewerwersfd", "hola mundo", "www.asdasdas.com"),
        FactsModel("3","sdf",23,"dfsdfsdfsdfggfg", "324234", "fvcbtrbvbgffbfghgff", "wervzdfadfa", "ewewerwer", "wewerwersfd", "hola mundo", "www.asdasdas.com"),
        FactsModel("4","sdf",23,"dfsdfsdfsdfggfg", "324234", "fvcbtrbvbgffbfghgff", "wervzdfadfa", "ewewerwer", "wewerwersfd", "hola mundo", "www.asdasdas.com"),
        FactsModel("5","sdf",23,"dfsdfsdfsdfggfg", "324234", "fvcbtrbvbgffbfghgff", "wervzdfadfa", "ewewerwer", "wewerwersfd", "hola mundo", "www.asdasdas.com")
    )

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

        setAdapter()
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