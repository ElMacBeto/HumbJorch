package com.humbjorch.myapplication.ui.home.dashBoard.adapter

import androidx.recyclerview.widget.DiffUtil
import com.humbjorch.myapplication.data.model.FactsModel

class FactDiffUtil(
    private val oldList: List<FactsModel>,
    private val newList: List<FactsModel>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].fact ==  newList[newItemPosition].fact) &&
                (oldList[oldItemPosition].slug ==  newList[newItemPosition].slug) &&
                (oldList[oldItemPosition].dataset ==  newList[newItemPosition].dataset)
    }

}