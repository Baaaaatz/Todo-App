package com.batzalcancia.todoapp

import com.batzalcancia.todoapp.data.TodoRepositoryImpl
import com.batzalcancia.todoapp.data.remote.TodoRemoteSource
import com.batzalcancia.todoapp.di.repository.TodoRepository
import com.batzalcancia.todoapp.testutils.BaseTest
import com.batzalcancia.todoapp.testutils.todos
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TodoRepositoryTest : BaseTest() {

    private lateinit var todoRepository: TodoRepository
    lateinit var todoRemoteSource: TodoRemoteSource

    @BeforeTest
    fun setup() {
        todoRemoteSource = mockk()
        todoRepository = TodoRepositoryImpl(
            todoRemoteSource
        )
    }

    @Test
    fun `getTodo should return list of todos`() = runBlocking {
        // When
        coEvery {
            todoRemoteSource.getTodoList(null)
        } returns todos

        val actualUsers = todoRepository.getTodo(null)

        // Then
        assertEquals(30, actualUsers.size)
    }

}
