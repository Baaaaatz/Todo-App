package com.batzalcancia.todoapp.domain.entites

import kotlinx.serialization.Serializable

@Serializable
data class Todo(
    val title: String,
    val body: String,
    val url: String?,
    val date: Long
) {
    constructor() : this("", "", "", 0L)
}