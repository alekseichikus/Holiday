package com.example.feature_adapter_generator

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class BaseViewHolder<T, V>(val view: V) :
    RecyclerView.ViewHolder(view) where V : ViewGroup, V : ViewAction<T> {
    fun bind(item: T) {
        view.initData(item = item)
    }
}