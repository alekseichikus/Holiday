package ru.createtogether.common.helpers.baseFragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import ru.createtogether.common.R
import ru.createtogether.common.helpers.baseFragment.extension.hideKeyboard

abstract class BaseDialogFragment(@LayoutRes val contentLayoutId: Int) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        configureDialog()
        return inflater.inflate(contentLayoutId, container, false)
    }

    private fun configureDialog() {
        dialog?.let {
            it.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            isCancelable = false
            it.requestWindowFeature(Window.FEATURE_NO_TITLE)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireActivity(), R.style.BaseDialogTheme)
    }

    inline fun <reified T : ViewBinding> Fragment.viewBinding() =
        FragmentViewBindingDelegate(T::class.java, this)

    override fun onDestroyView() {
        super.onDestroyView()
        hideKeyboard()
    }
}