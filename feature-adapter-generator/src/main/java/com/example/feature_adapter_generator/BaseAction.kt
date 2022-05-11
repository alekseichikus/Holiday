package com.example.feature_adapter_generator

fun interface BaseAction<in T> {
    fun onClick(item: T)
}