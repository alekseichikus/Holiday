package com.example.feature_adapter_generator

interface AdapterAction <T, out BA: BaseAction<T>> {
     var items: Collection<T>

     val action: BA?
}