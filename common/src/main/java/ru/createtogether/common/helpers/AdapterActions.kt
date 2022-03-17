package ru.createtogether.common.helpers

import ru.createtogether.common.helpers.baseFragment.base.adapter.BaseAction

interface AdapterActions <T> {

     var items: Array<T>

     var action: BaseAction<T>

     fun setData(array: Array<T>)
     fun getData(): Array<T>
}