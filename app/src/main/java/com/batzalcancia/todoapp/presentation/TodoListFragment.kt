package com.batzalcancia.todoapp.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.batzalcancia.todoapp.R
import com.batzalcancia.todoapp.core.helpers.showAlertDialog
import com.batzalcancia.todoapp.core.utils.UiState
import com.batzalcancia.todoapp.databinding.FragmentTodoListBinding
import com.batzalcancia.todoapp.databinding.ItemTodoBinding
import com.batzalcancia.todoapp.presentation.adapter.TodoItemAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

@AndroidEntryPoint
class TodoListFragment : Fragment(R.layout.fragment_todo_list) {

    private lateinit var viewBinding: FragmentTodoListBinding

    private val viewModel: TodoListViewModel by viewModels()

    private val todoListAdapter by lazy {
        TodoItemAdapter(ItemTodoBinding::bind) {
            requireContext().showAlertDialog(
                getString(R.string.message_delete_task),
                getString(R.string.title_delete_task),
                getString(R.string.label_yes),
                getString(R.string.label_no)
            ) {
                viewModel.onDeleteClicked(it.date)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentTodoListBinding.bind(view)

        viewBinding.fabAddTodo.setOnClickListener {
            findNavController().navigate(R.id.action_todo_list_to_todo_form)
        }

        viewBinding.rcvTodo.adapter = todoListAdapter

        viewModel.onRealTimeUpdate {
            todoListAdapter.refresh()
        }

        viewModel.pagingData.onEach {
            todoListAdapter.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        todoListAdapter.loadStateFlow.onEach {
            viewBinding.rcvTodo.isVisible = it.refresh is LoadState.NotLoading
            viewBinding.prgTodo.isVisible = it.refresh is LoadState.Loading

            viewBinding.txtEmptyTasks.isVisible =
                it.refresh !is LoadState.Loading && todoListAdapter.itemCount == 0
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.deleteTodoState.onEach {
            if (it != null) {
                if (it == UiState.Complete) {
                    todoListAdapter.refresh()
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
