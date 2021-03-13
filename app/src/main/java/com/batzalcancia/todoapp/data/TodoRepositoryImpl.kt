package com.batzalcancia.todoapp.data

import com.batzalcancia.todoapp.data.remote.TodoRemoteSource
import com.batzalcancia.todoapp.di.repository.TodoRepository
import com.batzalcancia.todoapp.domain.entites.Todo
import com.google.firebase.firestore.CollectionReference
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val todoRemoteSource: TodoRemoteSource
) : TodoRepository {

    override suspend fun addTodo(title: String, body: String, iconUrl: String?) {
        todoRemoteSource.addTodo(title, body, iconUrl)
    }

    override suspend fun getTodo(after: Long?): List<Todo> = todoRemoteSource.getTodoList(after)

    override suspend fun deleteTodo(date: Long) {
        todoRemoteSource.deleteTodo(date)
    }

    override suspend fun updateTodo(title: String, body: String, iconUrl: String?, date: Long) {
        todoRemoteSource.updateTodo(title, body, iconUrl, date)
    }

    override suspend fun getCollection() = todoRemoteSource.getCollection()

}