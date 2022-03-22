package ru.createtogether.feature_characteristic.presenter

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import ru.createtogether.feature_characteristic.databinding.ViewCharacteristicBinding
import ru.createtogether.feature_characteristic_utils.CharacteristicModel

class CharacteristicView constructor(context: Context) :
    FrameLayout(context), com.example.feature_adapter_generator.ViewAction<CharacteristicModel> {

    private var binding = ViewCharacteristicBinding.inflate(LayoutInflater.from(context), this, false)

    private lateinit var characteristic: CharacteristicModel

    init {
        addView(binding.root)
    }

    fun setCharacteristic(characteristic: CharacteristicModel) {
        this.characteristic = characteristic

        setImage()
        setText()
        setDescription()
    }

    private fun setImage() {
        binding.imageView.setImageResource(characteristic.imageRes)
    }

    private fun setText() {
        binding.tvText.text = characteristic.text
    }

    private fun setDescription() {
        binding.tvDescription.text = characteristic.description
    }

    override fun initData(item: CharacteristicModel) {
        setCharacteristic(characteristic = item)
    }
}