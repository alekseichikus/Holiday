package com.example.feature_adapter_generator

interface BaseAction<T> {
    fun onClick(item: T)
}