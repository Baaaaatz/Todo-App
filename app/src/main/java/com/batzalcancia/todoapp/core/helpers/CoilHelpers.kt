package com.batzalcancia.todoapp.core.helpers

import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.batzalcancia.todoapp.R

fun ImageView.loadImageFromUrl(
    url: String?,
    errorImage: Int = R.drawable.svg_todo,
    fromCache: Boolean = true,
    onError: () -> Unit = {},
    onSuccess: () -> Unit = {}
) = load(url ?: "") {
    if (!fromCache) {
        diskCachePolicy(CachePolicy.WRITE_ONLY)
        memoryCachePolicy(CachePolicy.WRITE_ONLY)
    }

    val imageLoader = CircularProgressDrawable(context)
    imageLoader.setStyle(CircularProgressDrawable.DEFAULT)
    placeholder(imageLoader)
    error(errorImage)

    crossfade(true)
    listener(
        onError = { _, _ ->
            onError()
        },
        onSuccess = { _, _ ->
            onSuccess()
        }
    )
}
