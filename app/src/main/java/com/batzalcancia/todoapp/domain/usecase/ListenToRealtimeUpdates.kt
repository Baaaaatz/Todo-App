package com.batzalcancia.todoapp.domain.usecase

import com.batzalcancia.todoapp.di.repository.TodoRepository
import javax.inject.Inject

class ListenToRealtimeUpdates @Inject constructor(private val repository: TodoRepository) {
    suspend operator fun invoke(invalidateList: () -> Unit) {
        repository.getCollection().addSnapshotListener { value, _ ->
            if (value != null) {
                invalidateList()
            }
        }
    }
}