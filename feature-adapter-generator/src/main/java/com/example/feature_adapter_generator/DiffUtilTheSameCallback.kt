package com.example.feature_adapter_generator

interface DiffUtilTheSameCallback<T> {
    fun areItemsTheSame(oldItem: T, newItem: T): Boolean

    fun areContentsTheSame(oldItem: T, newItem: T): Boolean
}