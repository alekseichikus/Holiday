package com.example.feature_adapter_generator

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class BaseViewHolder<V : ViewGroup>(val view: V) : RecyclerView.ViewHolder(view) {
    inline fun <T, reified VA : ViewAction<T>> bind(item: T) {
        if (view is VA)
            view.initData(item = item)
        else
            throw NoSuchMethodException("Use viewAction interface in your customView")
    }
}