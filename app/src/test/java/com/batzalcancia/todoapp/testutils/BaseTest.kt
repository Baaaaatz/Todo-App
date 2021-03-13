package com.batzalcancia.todoapp.testutils

import org.junit.Rule

open class BaseTest  {
    @get:Rule
    val rule = CoroutineTestRule()
}