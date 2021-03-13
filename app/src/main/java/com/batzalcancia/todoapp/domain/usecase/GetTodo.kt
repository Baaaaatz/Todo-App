package com.batzalcancia.todoapp.domain.usecase

import com.batzalcancia.todoapp.di.repository.TodoRepository
import javax.inject.Inject

class GetTodo @Inject constructor(private val todoRepository: TodoRepository) {
    suspend operator fun invoke(after: Long? = null) = todoRepository.getTodo(after)
}
