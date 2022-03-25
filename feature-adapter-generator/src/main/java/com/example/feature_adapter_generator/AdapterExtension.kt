package com.example.feature_adapter_generator

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

fun <T, V : ViewGroup, BA : BaseAction<T>, DU : DiffUtilTheSameCallback<T>> RecyclerView.initAdapter(
    data: Collection<T>,
    view: Class<V>,
    baseAction: BA? = null,
    diffUtil: DU
) {
    val baseAdapter: BaseAdapter<T, V, BaseAction<T>>

    if (adapter == null) {
        baseAdapter = BaseAdapter(
            viewClass = view,
            diffUtilTheSameCallback = diffUtil
        ).apply {
            baseAction?.let {
                if (view.interfaces.contains(ViewAction::class.java))
                    action = it
                else
                    throw IllegalArgumentException("View must have a viewAction interface")
            }
        }
        adapter = baseAdapter
    } else {
        baseAdapter = adapter as BaseAdapter<T, V, BaseAction<T>>
    }

    baseAdapter.items = data
}