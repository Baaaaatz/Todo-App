package com.batzalcancia.todoapp.di.repository

import com.batzalcancia.todoapp.domain.entites.Todo
import com.google.firebase.firestore.CollectionReference

interface TodoRepository {
    suspend fun addTodo(
        title: String,
        body: String,
        iconUrl: String?
    )

    suspend fun getTodo(after: Long? = null): List<Todo>

    suspend fun deleteTodo(date: Long)

    suspend fun updateTodo(
        title: String,
        body: String,
        iconUrl: String?,
        date: Long
    )

    suspend fun getCollection(): CollectionReference
}
