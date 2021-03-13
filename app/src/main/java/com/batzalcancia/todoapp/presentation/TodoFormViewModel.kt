package com.batzalcancia.todoapp.presentation

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batzalcancia.todoapp.domain.usecase.AddTodo
import com.batzalcancia.todoapp.core.utils.UiState
import com.batzalcancia.todoapp.domain.usecase.UpdateTodo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class TodoFormViewModel @Inject constructor(
    private val addTodo: AddTodo,
    private val updateTodo: UpdateTodo,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), LifecycleObserver {

    val title = MutableStateFlow<String?>(null)
    val body = MutableStateFlow<String?>(null)
    val iconUrl = MutableStateFlow<String?>(null)

    private val titleHasError = title.mapLatest {
        it.isNullOrEmpty()
    }

    private val bodyHasError = body.mapLatest {
        it.isNullOrEmpty()
    }

    val enableButton = combine(
        titleHasError,
        bodyHasError
    ) { titleHasError, bodyHasError ->
        !titleHasError && !bodyHasError
    }

    private val _sendFormState = MutableStateFlow<UiState?>(null)
    val sendFormState: StateFlow<UiState?> = _sendFormState

    fun onAddTodoClicked() {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            _sendFormState.value = UiState.Error(throwable)
        }) {
            _sendFormState.value = UiState.Loading
            addTodo(
                title.value ?: throw IllegalArgumentException("Invalid Title"),
                body.value ?: throw IllegalArgumentException("Invalid Body"),
                iconUrl.value
            )
            _sendFormState.value = UiState.Complete
        }
    }

    fun onEditTodoClicked(date: Long) {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            _sendFormState.value = UiState.Error(throwable)
        }) {
            _sendFormState.value = UiState.Loading
            updateTodo(
                title.value ?: throw IllegalArgumentException("Invalid Title"),
                body.value ?: throw IllegalArgumentException("Invalid Body"),
                iconUrl.value,
                date
            )
            _sendFormState.value = UiState.Complete
        }
    }

}
