package com.example.feature_adapter_generator

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

fun <T, V, BA : BaseAction<T>, DU : DiffUtilTheSameCallback<T>> RecyclerView.initAdapter(
    data: Collection<T>,
    viewClass: Class<V>,
    baseAction: BA? = null,
    diffUtil: DU
) where V : ViewGroup, V : ViewAction<T> {
    val baseAdapter: BaseAdapter<T, V, BaseAction<T>>

    if (adapter == null) {
        baseAdapter = BaseAdapter(
            viewClass = viewClass,
            diffUtilTheSameCallback = diffUtil
        ).apply {
            baseAction?.let {
                action = it
            }
        }
        adapter = baseAdapter
    } else {
        baseAdapter = adapter as BaseAdapter<T, V, BaseAction<T>>
    }
    baseAdapter.items = data
}