package ru.createtogether.common.helpers.baseFragment

import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import ru.createtogether.common.helpers.ViewModelActions
import ru.createtogether.common.helpers.extension.hideKeyboard
import ru.createtogether.common.helpers.extension.setupSnackBar


abstract class BaseFragment(@LayoutRes val contentLayoutId: Int) : Fragment() {

  abstract val viewModel: ViewModelActions

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(contentLayoutId, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setSnackBar()
  }

  inline fun <reified T : ViewBinding> Fragment.viewBinding() = FragmentViewBindingDelegate(T::class.java, this)

  override fun onDestroyView() {
    super.onDestroyView()
    hideKeyboard()
  }

  private fun setSnackBar(){
    requireView().setupSnackBar(this, viewModel.snackBarResponse, Snackbar.LENGTH_SHORT)
  }
}