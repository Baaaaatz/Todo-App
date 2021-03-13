package com.batzalcancia.todoapp.core.helpers

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Context.showAlertDialog(
    message: String,
    title: String? = null,
    positiveLabel: String? = getString(android.R.string.ok),
    negativeLabel: String? = getString(android.R.string.cancel),
    action: () -> Unit = {}
) = MaterialAlertDialogBuilder(this).apply {
    title?.let { setTitle(it) }
    setMessage(message)
    setPositiveButton(positiveLabel) { dialog, _ ->
        dialog.dismiss()
        action()
    }
    setNegativeButton(negativeLabel) { dialog, _ ->
        dialog.dismiss()
    }
    setCancelable(true)
}.show()
