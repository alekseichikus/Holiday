package com.example.feature_adapter_generator

interface ViewAction<in T> {
    fun initData(item: T)
}