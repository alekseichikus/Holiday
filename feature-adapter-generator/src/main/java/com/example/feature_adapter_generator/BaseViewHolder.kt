package com.example.feature_adapter_generator

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

internal class BaseViewHolder<T, V>(private val view: V) :
    RecyclerView.ViewHolder(view) where V : ViewGroup, V : ViewAction<T> {
    fun bind(item: T, baseAction: BaseAction<T>?) {
        view.initData(item = item, baseAction = baseAction)
    }
}