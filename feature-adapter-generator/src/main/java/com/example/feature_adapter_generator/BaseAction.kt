package com.example.feature_adapter_generator

interface BaseAction<in T> {
    fun onClick(item: T)
}