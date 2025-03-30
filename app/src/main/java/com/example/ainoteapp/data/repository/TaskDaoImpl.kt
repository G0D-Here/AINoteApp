package com.example.ainoteapp.data.repository

import com.example.ainoteapp.data.local.TaskDao
import com.example.ainoteapp.data.local.TaskEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(private val dao: TaskDao) : TaskRepository {
    override suspend fun insertTask(task: TaskEntity) {
        dao.insertTask(task)
    }

    override suspend fun updateTask(task: TaskEntity) {
        dao.updateTask(task)
    }

    override suspend fun deleteTask(task: TaskEntity) {
        dao.deleteTask(task)
    }

    override fun getAllTasks(): Flow<List<TaskEntity>> = dao.getAllTasks()

    override suspend fun getTaskById(taskId: Int): TaskEntity? = dao.getTaskById(taskId)

    override fun searchResults(query: String): Flow<List<TaskEntity>> = dao.searchResults(query)

}