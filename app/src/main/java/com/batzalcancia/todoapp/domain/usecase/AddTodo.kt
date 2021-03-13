package com.batzalcancia.todoapp.domain.usecase

import com.batzalcancia.todoapp.di.repository.TodoRepository
import javax.inject.Inject

class AddTodo @Inject constructor(private val todoRepository: TodoRepository) {
    suspend operator fun invoke(
        title: String,
        body: String,
        iconUrl: String?
    ) {
        todoRepository.addTodo(title, body, iconUrl)
    }
}
