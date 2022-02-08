package ru.createtogether.common.helpers.baseFragment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import ru.createtogether.common.helpers.baseFragment.BaseBottomDialogFragment
import ru.createtogether.common.R
import ru.createtogether.common.databinding.BottomDialogBaseBinding

class BaseDropDownListFragment: BaseBottomDialogFragment(R.layout.bottom_dialog_base) {
    private val binding: BottomDialogBaseBinding by viewBinding()

    var callback: ((Int) -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttons.forEachIndexed { index, s ->
            val itemView: View = LayoutInflater.from(context).inflate(R.layout.item_drop_down, null)
            itemView.setOnClickListener {
                callback?.invoke(index)
                dismiss()
            }

            (itemView as TextView).text = s
            binding.llContainer.addView(itemView)
        }
    }

    private val buttons by lazy {
        requireArguments().getSerializable(BUTTONS) as List<String>
    }

    companion object{
        const val BUTTONS = "buttons"
        fun getInstance(buttons: List<String>, callback: (Int) -> Unit): BaseDropDownListFragment {
            return BaseDropDownListFragment().apply {
                arguments = bundleOf(BUTTONS to buttons)
                this.callback = callback
            }
        }
    }
}