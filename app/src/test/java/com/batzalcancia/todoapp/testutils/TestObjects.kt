package com.batzalcancia.todoapp.testutils

import com.batzalcancia.todoapp.domain.entites.Todo
import kotlin.random.Random

val todos = mutableListOf<Todo>().apply {
    repeat(30) {
        add(
            Todo(
                title = "Title$it",
                body = "username$it",
                url = "image$it",
                date = Random.nextLong()
            )
        )
    }
}