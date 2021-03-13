package com.batzalcancia.todoapp.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.batzalcancia.todoapp.R
import com.batzalcancia.todoapp.core.helpers.loadImageFromUrl
import com.batzalcancia.todoapp.core.helpers.longToDate
import com.batzalcancia.todoapp.core.utils.ViewBindingViewHolder
import com.batzalcancia.todoapp.databinding.ItemTodoBinding
import com.batzalcancia.todoapp.domain.FormType
import com.batzalcancia.todoapp.domain.entites.Todo
import com.batzalcancia.todoapp.presentation.TodoListFragment
import com.batzalcancia.todoapp.presentation.TodoListFragmentDirections
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TodoItemAdapter(
    private val setupTodoItemBinding: (View) -> ItemTodoBinding,
    private val onLongClickListener: (Todo) -> Unit
) : PagingDataAdapter<Todo, ViewBindingViewHolder<ItemTodoBinding>>(TodoItemsDiffUtil) {

    override fun onBindViewHolder(holder: ViewBindingViewHolder<ItemTodoBinding>, position: Int) {
        val todo = getItem(position)
        todo?.let {
            holder.viewBinding.apply {
                txtTitle.text = it.title
                txtDate.text = it.date.longToDate()
                imgTodo.loadImageFromUrl(it.url)
                root.setOnLongClickListener { _ ->
                    onLongClickListener(it)
                    true
                }
                root.setOnClickListener { view ->
                    view.findNavController().navigate(
                        TodoListFragmentDirections.actionTodoListToTodoForm(
                            Json { }.encodeToString(it),
                            FormType.EDIT
                        )
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewBindingViewHolder<ItemTodoBinding> {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return ViewBindingViewHolder(setupTodoItemBinding(view))
    }

    object TodoItemsDiffUtil : DiffUtil.ItemCallback<Todo>() {
        override fun areItemsTheSame(oldItem: Todo, newItem: Todo) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean =
            oldItem.date == newItem.date

    }
} 