package ru.createtogether.common.helpers.baseFragment

import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import ru.createtogether.common.helpers.baseFragment.extension.hideKeyboard


abstract class BaseFragment(@LayoutRes val contentLayoutId: Int) : Fragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(contentLayoutId, container, false)
  }

  inline fun <reified T : ViewBinding> Fragment.viewBinding() = FragmentViewBindingDelegate(T::class.java, this)

  override fun onDestroyView() {
    super.onDestroyView()
    hideKeyboard()
  }

  companion object {

  }
}