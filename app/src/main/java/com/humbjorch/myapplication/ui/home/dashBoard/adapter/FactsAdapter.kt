package com.humbjorch.myapplication.ui.home.dashBoard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.humbjorch.myapplication.R
import com.humbjorch.myapplication.data.model.FactsModel
import com.humbjorch.myapplication.databinding.ItemFactBinding

class FactsAdapter(
    private var dataSet: List<FactsModel>,
    private val onClick: (FactsModel) -> Unit
) :
    RecyclerView.Adapter<FactsAdapter.ViewHolder>() {

    fun updateList(newList: List<FactsModel>, itemPosition:Int = -1){
        if (itemPosition >= 0){
            notifyItemChanged(itemPosition)
        }else{
            val projectDiffUtil = FactDiffUtil(dataSet, newList)
            val result = DiffUtil.calculateDiff(projectDiffUtil)
            dataSet = newList
            result.dispatchUpdatesTo(this)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val binding = ItemFactBinding.bind(view)

        fun bindView(fact: FactsModel) {
            binding.fatc = fact
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_fact, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val fact = dataSet[position]

        viewHolder.bindView(fact)
        viewHolder.binding.imgDetail.setOnClickListener{
            onClick.invoke(fact)
        }
    }

    override fun getItemCount() = dataSet.size

}
