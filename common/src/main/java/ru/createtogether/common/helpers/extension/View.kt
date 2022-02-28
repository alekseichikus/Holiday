package ru.createtogether.common.helpers.extension

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.WindowInsets
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ru.createtogether.common.R
import ru.createtogether.common.helpers.Utils
import java.text.SimpleDateFormat
import java.util.*
import kotlin.contracts.contract

fun View.setPaddingTopMenu() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        setOnApplyWindowInsetsListener { v, insets ->
            v.updatePadding(
                top = insets.getInsets(WindowInsets.Type.systemBars()).top,
            )
            return@setOnApplyWindowInsetsListener insets
        }
    }
}

fun Fragment.getTopOffset(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        requireView().rootWindowInsets.getInsets(WindowInsets.Type.systemBars()).top
    } else {
        0
    }
}

fun Fragment.hideKeyboard() {
    (requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(
            requireView().applicationWindowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.toggleVisibility() {
    if (this.visibility == View.INVISIBLE || this.visibility == View.GONE)
        this.visibility = View.VISIBLE
    else
        this.visibility = View.GONE
}

fun FragmentManager.isNotExistFragment(tag: String): Boolean {
    return findFragmentByTag(tag) == null
}

val Int.toPx
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )

fun Int.toDp(context: Context) = (this / context.resources.displayMetrics.density).toInt()

fun Int.toPx(context: Context) = (this * context.resources.displayMetrics.density).toInt()

fun IntRange.random() =
    Random().nextInt((endInclusive + 1) - start) + start

fun Calendar.setDateString(date: String): Calendar {
    this.timeInMillis = Utils.convertDateStringToLong(date = date)
    return this
}

fun Date.withPattern(pattern: String) = SimpleDateFormat(pattern, Locale.getDefault()).format(time)

fun Fragment.onBack() {
    parentFragmentManager.popBackStack()
}

fun Fragment.onOpen(fragment: Fragment, fragmentManager: FragmentManager? = null) {
    (fragmentManager ?: parentFragmentManager).beginTransaction()
        .replace(R.id.fragmentContainerView, fragment, fragment.javaClass.name).setCustomAnimations(
            R.anim.fragment_fade_enter,
            R.anim.fragment_fade_exit,
            R.anim.fragment_fade_enter,
            R.anim.fragment_fade_exit
        )
        .addToBackStack(null)
        .commit()
}

fun Fragment.onOpen(fragment: Fragment, fragmentManager: FragmentManager? = null, isAdd: Boolean) {
    (fragmentManager ?: parentFragmentManager).beginTransaction()
        .add(R.id.fragmentContainerView, fragment, fragment.javaClass.name).setCustomAnimations(
            R.anim.fragment_fade_enter,
            R.anim.fragment_fade_exit,
            R.anim.fragment_fade_enter,
            R.anim.fragment_fade_exit
        )
        .addToBackStack(null)
        .commit()
}

fun AppCompatActivity.onOpen(fragment: Fragment) {
    supportFragmentManager.beginTransaction()
        .add(R.id.fragmentContainerView, fragment, fragment.javaClass.name).setCustomAnimations(
            R.anim.fragment_fade_enter,
            R.anim.fragment_fade_exit,
            R.anim.fragment_fade_enter,
            R.anim.fragment_fade_exit
        ).commit()
}

fun <T> Collection<T>?.isNotNull() = this != null