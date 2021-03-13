package com.batzalcancia.todoapp.domain.usecase

import com.batzalcancia.todoapp.di.repository.TodoRepository
import com.batzalcancia.todoapp.domain.entites.Todo
import javax.inject.Inject

class DeleteTodo @Inject constructor(private val todoRepository: TodoRepository) {
    suspend operator fun invoke(date: Long) = todoRepository.deleteTodo(date)
}