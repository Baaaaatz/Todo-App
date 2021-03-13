package com.batzalcancia.todoapp.di.module

import android.content.Context
import android.content.SharedPreferences
import com.batzalcancia.todoapp.data.TodoRepositoryImpl
import com.batzalcancia.todoapp.data.remote.TodoRemoteSource
import com.batzalcancia.todoapp.di.repository.TodoRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object TodoModule {

    @Provides
    fun provideSharedPreference(
        @ApplicationContext context: Context
    ): SharedPreferences = context.getSharedPreferences("todo_v1", Context.MODE_PRIVATE)

    @Provides
    fun providesTodoRemoteSource(
        firebaseFireStore: FirebaseFirestore
    ): TodoRemoteSource = TodoRemoteSource(firebaseFireStore)

    @Provides
    fun provideTodoRepository(
        todoRemoteSource: TodoRemoteSource
    ): TodoRepository = TodoRepositoryImpl(todoRemoteSource)

}
