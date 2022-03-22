package ru.createtogether.feature_photo.helpers

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.createtogether.feature_photo.PhotoLoadView
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun PhotoLoadView.loadImage(lifeCoroutineScope: CoroutineScope, url: String) = lifeCoroutineScope.launch {
    Glide.with(getImageView())
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade(Constants.ANIMATE_TRANSITION_DURATION))
        .addListener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                updateState(Result.failure(e ?: Exception()))
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                updateState(Result.success(value = true))
                return false
            }
        })
        .into(getImageView())
}
