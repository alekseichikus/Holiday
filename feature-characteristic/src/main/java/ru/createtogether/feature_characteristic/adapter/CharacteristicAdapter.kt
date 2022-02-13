package ru.createtogether.feature_characteristic.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.createtogether.common.helpers.AdapterActions
import ru.createtogether.feature_characteristic.presenter.CharacteristicView
import ru.createtogether.feature_characteristic_utils.CharacteristicModel

class CharacteristicAdapter(
    private var characteristics: MutableList<CharacteristicModel>,
) : RecyclerView.Adapter<CharacteristicAdapter.CharacteristicViewHolder>(), AdapterActions {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CharacteristicViewHolder(CharacteristicView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        })

    override fun onBindViewHolder(holder: CharacteristicViewHolder, position: Int) {
        holder.bind(characteristic = characteristics[position], position = position)
    }

    override fun getData() = characteristics

    override fun setData(list: List<Any>) {
        characteristics = list as MutableList<CharacteristicModel>
    }

    override fun getItemCount() = characteristics.size

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
}