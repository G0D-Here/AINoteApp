package com.example.ainoteapp.data.repository

import com.example.ainoteapp.data.local.TaskEntity
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun insertTask(task: TaskEntity)
    suspend fun updateTask(task: TaskEntity)
    suspend fun deleteTask(task: TaskEntity)
    fun getAllTasks(): Flow<List<TaskEntity>>
    suspend fun getTaskById(taskId: Int): TaskEntity?
   fun searchResults(query: String): Flow<List<TaskEntity>>

}