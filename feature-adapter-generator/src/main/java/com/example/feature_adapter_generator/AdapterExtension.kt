package com.example.feature_adapter_generator

import androidx.recyclerview.widget.RecyclerView

fun <T, V, BA : BaseAction<T>, DU : DiffUtilTheSameCallback<T>> RecyclerView.initAdapter(
    data: Collection<T>,
    view: Class<V>,
    actionAdapter: BA? = null,
    diffUtil: DU
) {
    val baseAdapter: BaseAdapter<T, V, BaseAction<T>>?

    if (adapter == null) {
        baseAdapter = BaseAdapter(
            viewClass = view,
            diffUtilTheSameCallback = diffUtil
        ).apply {
            actionAdapter?.let {
                if (view.interfaces.contains(ViewAction::class.java))
                    action = it
                else
                    throw IllegalArgumentException("View must have a viewAction interface")
            }
        }
        adapter = baseAdapter
    }
    (adapter as AdapterAction<T, BA>).setData(data)
}