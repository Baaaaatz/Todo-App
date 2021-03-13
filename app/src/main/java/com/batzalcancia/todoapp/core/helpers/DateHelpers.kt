package com.batzalcancia.todoapp.core.helpers

import java.text.SimpleDateFormat
import java.util.*

fun Long.longToDate(): String {
    val date = Date(this)
    val format = SimpleDateFormat("MMM dd, yyyy | hh:mm:aa", Locale.getDefault())
    return format.format(date)
}
