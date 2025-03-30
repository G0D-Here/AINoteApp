package com.example.ainoteapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String?,
    val isCompleted: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val imagePath: String? = null, // Store image URI as a string
    val color: Long? = null
)
