package ru.createtogether.common.helpers

import androidx.annotation.ColorRes

interface AdapterActions {
     fun getData(): List<Any>
     fun setData(list: List<Any>)
}