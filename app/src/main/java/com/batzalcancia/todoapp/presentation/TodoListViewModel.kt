package com.batzalcancia.todoapp.presentation

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.batzalcancia.todoapp.core.utils.UiState
import com.batzalcancia.todoapp.data.pagingsource.TodoPagingSource
import com.batzalcancia.todoapp.domain.entites.Todo
import com.batzalcancia.todoapp.domain.usecase.DeleteTodo
import com.batzalcancia.todoapp.domain.usecase.GetTodo
import com.batzalcancia.todoapp.domain.usecase.ListenToRealtimeUpdates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class TodoListViewModel @Inject constructor(
    private val getTodo: GetTodo,
    private val deleteTodo: DeleteTodo,
    private val listenToRealTimeUpdates: ListenToRealtimeUpdates,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), LifecycleObserver {

    private val _deleteTodoState = MutableStateFlow<UiState?>(null)
    val deleteTodoState: StateFlow<UiState?> get() = _deleteTodoState

    val pagingData = Pager(
        config = PagingConfig(
            pageSize = 30,
            enablePlaceholders = false
        ),
        initialKey = null
    ) {
        TodoPagingSource(getTodo)
    }.flow

    fun onRealTimeUpdate(invalidateList: () -> Unit) {
        viewModelScope.launch(CoroutineExceptionHandler { _, _ ->  }) {
            listenToRealTimeUpdates { invalidateList() }
        }
    }

    fun onDeleteClicked(date: Long) {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            _deleteTodoState.value = UiState.Error(throwable)
        }) {
            _deleteTodoState.value = UiState.Loading
            deleteTodo(date)
            _deleteTodoState.value = UiState.Complete
        }
    }

}