package ru.createtogether.common.helpers.baseFragment.base.adapter

import androidx.recyclerview.widget.RecyclerView
import ru.createtogether.common.helpers.AdapterActions
import java.lang.IllegalArgumentException

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder> :
    RecyclerView.Adapter<VH>(),
    AdapterActions<T> {

    override fun getItemCount() = items.size

    override fun setData(array: Array<T>) {
        items = array
    }
    inline fun <reified T> getActions(): T {
        if(action is T)
            return action as T
        throw IllegalArgumentException()
    }

    override fun getData() = items

    override lateinit var action: BaseAction<T>
}