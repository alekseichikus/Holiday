package com.example.feature_adapter_generator

import androidx.recyclerview.widget.DiffUtil

internal open class BaseDiffUtilCallback<out T>(
    private var oldList: Collection<T>,
    private var newList: Collection<T>,
    private val diffUtilTheSameCallback: DiffUtilTheSameCallback<T>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldProduct = oldList.elementAt(oldItemPosition)
        val newProduct = newList.elementAt(newItemPosition)
        return diffUtilTheSameCallback.areItemsTheSame(oldProduct, newProduct)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldProduct = oldList.elementAt(oldItemPosition)
        val newProduct = newList.elementAt(newItemPosition)
        return diffUtilTheSameCallback.areContentsTheSame(oldProduct, newProduct)
    }

    fun calculateDiff() = DiffUtil.calculateDiff(this, false)
}