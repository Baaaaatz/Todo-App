package com.batzalcancia.todoapp.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.batzalcancia.todoapp.R
import com.batzalcancia.todoapp.core.helpers.textChangesFlow
import com.batzalcancia.todoapp.core.utils.UiState
import com.batzalcancia.todoapp.databinding.FragmentTodoFormBinding
import com.batzalcancia.todoapp.domain.FormType
import com.batzalcancia.todoapp.domain.entites.Todo
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.json.Json

@AndroidEntryPoint
@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class TodoFormFragment : Fragment(R.layout.fragment_todo_form) {

    private lateinit var viewBinding: FragmentTodoFormBinding

    private val formViewModel: TodoFormViewModel by viewModels()

    private val todoFormNavArgs: TodoFormFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentTodoFormBinding.bind(view)

        viewBinding.btnAddTodo.setOnClickListener {
            when (todoFormNavArgs.formType) {
                FormType.ADD -> formViewModel.onAddTodoClicked()
                FormType.EDIT -> {
                    todoFormNavArgs.todoItem?.let {
                        val todoDate = Json { }.decodeFromString(Todo.serializer(), it).date
                        formViewModel.onEditTodoClicked(todoDate)
                    }
                }
            }
        }

        viewBinding.edtBody.textChangesFlow()
            .debounce(300)
            .mapLatest { formViewModel.body.value = it }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewBinding.edtUrl.textChangesFlow()
            .debounce(300)
            .mapLatest { formViewModel.iconUrl.value = it }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewBinding.edtTitle.textChangesFlow()
            .debounce(300)
            .mapLatest { formViewModel.title.value = it }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        formViewModel.enableButton.onEach {
            viewBinding.btnAddTodo.isEnabled = it
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        if (todoFormNavArgs.formType == FormType.EDIT) {
            todoFormNavArgs.todoItem?.let {
                Json { }.decodeFromString(Todo.serializer(), it).apply {
                    viewBinding.edtBody.setText(body)
                    viewBinding.edtTitle.setText(title)
                    url?.let { viewBinding.edtUrl.setText(it) }
                    viewBinding.btnAddTodo.text = getString(R.string.label_update_task)
                }
            }
        }

        formViewModel.sendFormState.onEach {
            if (it != null) {
                viewBinding.prgAddTodo.isVisible = it == UiState.Loading
                viewBinding.btnAddTodo.isInvisible = it == UiState.Loading
                viewBinding.edtBody.isEnabled = it != UiState.Loading
                viewBinding.edtTitle.isEnabled = it != UiState.Loading
                viewBinding.edtUrl.isEnabled = it != UiState.Loading

                if (it == UiState.Complete) {
                    Snackbar.make(
                        requireView(),
                        when (todoFormNavArgs.formType) {
                            FormType.ADD -> getString(R.string.message_task_added)
                            FormType.EDIT -> getString(R.string.message_task_updated)
                        },
                        Snackbar.LENGTH_LONG
                    ).show()
                    findNavController().navigateUp()
                } else if (it is UiState.Error) {
                    Snackbar.make(
                        requireView(),
                        it.throwable.localizedMessage ?: getString(R.string.generic_error_message),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

}