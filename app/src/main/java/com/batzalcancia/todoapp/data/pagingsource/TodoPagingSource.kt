package com.batzalcancia.todoapp.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.batzalcancia.todoapp.domain.entites.Todo
import com.batzalcancia.todoapp.domain.usecase.GetTodo
import javax.inject.Inject

class TodoPagingSource @Inject constructor(
    private val getTodo: GetTodo
) : PagingSource<Long, Todo>() {

    override fun getRefreshKey(state: PagingState<Long, Todo>): Long? = null

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Todo> {
        return try {
            val response = getTodo(params.key)
            LoadResult.Page(
                data = response,
                prevKey = null,
                nextKey = response.last().date
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}
