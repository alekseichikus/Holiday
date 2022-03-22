package com.example.feature_adapter_generator

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class BaseViewHolder<T>(val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(item: T) {
        (view as ViewAction<T>).initData(item = item)
    }
}