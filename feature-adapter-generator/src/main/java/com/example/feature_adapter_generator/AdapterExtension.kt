package com.example.feature_adapter_generator

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.lang.ClassCastException

fun <T, V, BA : BaseAction<T>, DU : DiffUtilTheSameCallback<T>> RecyclerView.initAdapter(
    data: Collection<T>,
    view: Class<V>,
    baseAction: BA? = null,
    diffUtil: DU
) where V : ViewGroup, V : ViewAction<T> {
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
                    throw ClassCastException("View must have a viewAction interface")
            }
        }
        adapter = baseAdapter
    } else {
        baseAdapter = adapter as BaseAdapter<T, V, BaseAction<T>>
    }

    baseAdapter.items = data
}