package com.batzalcancia.todoapp.core

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TodoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}