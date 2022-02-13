package ru.createtogether.feature_characteristic.presenter

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import ru.createtogether.feature_characteristic.R
import ru.createtogether.feature_characteristic.databinding.ViewCharacteristicBinding
import ru.createtogether.feature_characteristic_utils.CharacteristicModel

class CharacteristicView(context: Context) : FrameLayout(context) {

    private var _binding: ViewCharacteristicBinding? = null
    private val binding get() = _binding!!

    private lateinit var characteristic: CharacteristicModel
    private var position: Int = 0

    init {
        _binding = ViewCharacteristicBinding.inflate(LayoutInflater.from(context), this, false)
        addView(binding.root)
    }

    fun setCharacteristic(characteristic: CharacteristicModel, position: Int) {
        this.characteristic = characteristic
        this.position = position

        setImage()
        setText()
        setDescription()
        setBackground()
    }

    private fun setBackground() {
        binding.root.setBackgroundResource(
            if (position % 2 == 0)
                R.color.backgroundContent
            else
                R.color.backgroundFill
        )
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
}