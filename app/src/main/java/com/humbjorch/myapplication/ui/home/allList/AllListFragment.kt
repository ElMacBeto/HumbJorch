package com.humbjorch.myapplication.ui.home.allList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.humbjorch.myapplication.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_list, container, false)
    }

}