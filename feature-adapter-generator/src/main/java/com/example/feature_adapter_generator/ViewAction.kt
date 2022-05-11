package com.example.feature_adapter_generator

interface ViewAction<T> {
    fun initData(item: T, baseAction: BaseAction<T>?)
}