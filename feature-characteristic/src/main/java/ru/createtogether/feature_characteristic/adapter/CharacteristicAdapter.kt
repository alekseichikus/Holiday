package ru.createtogether.feature_characteristic.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.createtogether.common.helpers.baseFragment.base.adapter.BaseAdapter
import ru.createtogether.feature_characteristic.presenter.CharacteristicView
import ru.createtogether.feature_characteristic_utils.CharacteristicModel

class CharacteristicAdapter :
    BaseAdapter<CharacteristicModel, CharacteristicAdapter.CharacteristicViewHolder, >() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CharacteristicViewHolder(CharacteristicView(parent.context))

    inner class CharacteristicViewHolder(
        private val characteristicView: CharacteristicView
    ) : RecyclerView.ViewHolder(characteristicView) {
        fun bind(characteristic: CharacteristicModel, position: Int) {
            characteristicView.setCharacteristic(
                characteristic = characteristic,
                position = position
            )
        }
    }

    override fun onBindViewHolder(holder: CharacteristicViewHolder, position: Int) {
        holder.bind(characteristic = items[position], position = position)
    }

    override var items: Array<CharacteristicModel> = arrayOf()
}