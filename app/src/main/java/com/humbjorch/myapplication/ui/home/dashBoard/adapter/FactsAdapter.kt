package com.humbjorch.myapplication.ui.home.dashBoard.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.humbjorch.myapplication.R
import com.humbjorch.myapplication.data.model.FactsEntity
import com.humbjorch.myapplication.databinding.ItemFactBinding
import com.humbjorch.myapplication.sis.utils.formatDate
import com.humbjorch.myapplication.sis.utils.toDateFormatMonths

class FactsAdapter(
    private var dataSet: List<FactsEntity>,
    private val onClick: (FactsEntity) -> Unit
) :
    RecyclerView.Adapter<FactsAdapter.ViewHolder>() {

    var onChargePage:  (() -> Unit)? = null


    fun updateList(newList: List<FactsEntity>, itemPosition:Int = -1){
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

        @RequiresApi(Build.VERSION_CODES.O)
        fun bindView(fact: FactsEntity) {
            binding.fatc = fact
            binding.tvDate.text = formatDate(fact.dateInsert!!).toDateFormatMonths()
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_fact, viewGroup, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val fact = dataSet[position]

        if(position == (itemCount -1)){
            onChargePage?.invoke()
        }

        viewHolder.bindView(fact)
        viewHolder.binding.imgDetail.setOnClickListener{
            onClick.invoke(fact)
        }
    }

    override fun getItemCount() = dataSet.size

}
