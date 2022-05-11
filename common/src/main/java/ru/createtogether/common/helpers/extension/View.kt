package ru.createtogether.common.helpers.extension

import Constants
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.TypedValue
import android.view.View
import android.view.WindowInsets
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Response
import ru.createtogether.common.R
import ru.createtogether.common.helpers.Event
import ru.createtogether.common.helpers.Status
import ru.createtogether.common.helpers.Utils
import java.io.Serializable
import java.lang.Exception
import java.lang.NullPointerException
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.properties.ReadWriteProperty

fun View.setPaddingTop() {
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

fun getDateString(date: String, patterns: String): String {
    val time = Calendar.getInstance().setDateString(date = date).time
    var text = ""

    with(patterns.split(Constants.DATE_DELIMITERS)) {
        forEachIndexed { index, pattern ->
            text += time.withPattern(pattern)
            if (index != this.size - 1)
                text += " "
        }
    }
    return text
}

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

suspend fun <T> Flow<T>.exceptionProcessing(stateFlow: MutableStateFlow<Event<T>>) {
    runCatching {
        collect {
            stateFlow.value = Event.Success(data = it)
        }
    }.onFailure { throwable ->
        stateFlow.value = Event.Error(throwable = throwable)
    }
}

fun <T> Response<T>.responseProcessing(): T {
    if (isSuccessful && body().isNotNull()) {
        return body()!!
    } else
        throw IllegalArgumentException()
}

fun Any?.isNotNull() = this != null

fun View.showSnackBar(snackBarText: String, timeLength: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, snackBarText, timeLength).run {
        show()
    }
}

fun Context.shareText(title: String, text: StringBuilder) {
    startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtras(bundleOf(Intent.EXTRA_TEXT to text))
    }, title))
}

fun ImageView.loadImage(
    url: String,
    onSuccess: (() -> Unit)? = null,
    onError: (() -> Unit)? = null
) {
    Glide.with(this@loadImage)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade(Constants.ANIMATE_TRANSITION_DURATION))
        .addListener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                onError?.invoke()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                onSuccess?.invoke()
                return false
            }
        })
        .into(this@loadImage)
}

fun <T> Bundle.put(key: String, value: T) {
    when (value) {
        is Boolean -> putBoolean(key, value)
        is String -> putString(key, value)
        is Int -> putInt(key, value)
        is Short -> putShort(key, value)
        is Long -> putLong(key, value)
        is Byte -> putByte(key, value)
        is ByteArray -> putByteArray(key, value)
        is Char -> putChar(key, value)
        is CharArray -> putCharArray(key, value)
        is CharSequence -> putCharSequence(key, value)
        is Float -> putFloat(key, value)
        is Bundle -> putBundle(key, value)
        is Parcelable -> putParcelable(key, value)
        is Serializable -> putSerializable(key, value)
        else -> throw IllegalStateException("Type of property $key is not supported")
    }
}

fun <T : Any> argument(): ReadWriteProperty<Fragment, T> =
    FragmentArgumentDelegate()