package com.example.feature_adapter_generator

interface AdapterAction <T, BA: BaseAction<T>> {

     val items: Collection<T>

     val action: BA?

     fun setData(items: Collection<T>)

     fun getData(): Collection<T>
}