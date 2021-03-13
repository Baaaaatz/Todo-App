package com.batzalcancia.todoapp.data.remote

import com.batzalcancia.todoapp.BuildConfig
import com.batzalcancia.todoapp.domain.entites.Todo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TodoRemoteSource @Inject constructor(private val firestore: FirebaseFirestore) {
    suspend fun addTodo(
        title: String,
        body: String,
        iconUrl: String?
    ) = withContext(Dispatchers.IO) {
        val date = System.currentTimeMillis()
        firestore.collection(BuildConfig.COLLECTION_NAME)
            .document(date.toString())
            .set(Todo(title, body, iconUrl, date), SetOptions.merge())
            .addOnFailureListener {
                throw it
            }
    }

    suspend fun getTodoList(after: Long? = null): List<Todo> = withContext(Dispatchers.IO) {
        if (after != null) {
            firestore.collection(BuildConfig.COLLECTION_NAME)
                .orderBy(BuildConfig.COLLECTION_KEY, Query.Direction.DESCENDING)
                .whereLessThan(BuildConfig.COLLECTION_KEY, after)
                .limit(30)
                .get()
                .addOnFailureListener { throw it }
                .await()
                .toObjects(Todo::class.java)
        } else {
            firestore.collection(BuildConfig.COLLECTION_NAME)
                .orderBy(BuildConfig.COLLECTION_KEY, Query.Direction.DESCENDING)
                .get()
                .addOnFailureListener { throw it }
                .await()
                .toObjects(Todo::class.java)
        }
    }

    suspend fun deleteTodo(
        date: Long
    ): Unit = withContext(Dispatchers.IO) {
        firestore.collection(BuildConfig.COLLECTION_NAME)
            .document(date.toString())
            .delete()
            .addOnFailureListener { throw it }
            .await()
    }

    suspend fun updateTodo(
        title: String,
        body: String,
        iconUrl: String?,
        date: Long
    ): Unit = withContext(Dispatchers.IO) {
        firestore.collection(BuildConfig.COLLECTION_NAME).document(date.toString()).update(
            mapOf(
                "title" to title,
                "body" to body,
                "url" to iconUrl
            )
        ).addOnFailureListener { throw it }
    }

    suspend fun getCollection() = withContext(Dispatchers.IO) {
        firestore.collection(BuildConfig.COLLECTION_NAME)
    }
}
