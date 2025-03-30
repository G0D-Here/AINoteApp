package com.example.ainoteapp.youi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ainoteapp.data.local.TaskEntity
import com.example.ainoteapp.data.repository.TaskRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NotesViewModel @Inject constructor(private val repository: TaskRepositoryImpl) : ViewModel() {
    private val _allNotes = MutableStateFlow<List<TaskEntity>>(emptyList())
    val allNotes = _allNotes.asStateFlow()

    private val _note = MutableStateFlow<TaskEntity?>(null)
    val note = _note.asStateFlow()

    private val _result = MutableStateFlow<List<TaskEntity>>(emptyList())
    val result = _result.asStateFlow()

    init {
        getAllNotes()
    }

    private fun getAllNotes() =
        viewModelScope.launch {
            repository.getAllTasks().collect {
                _allNotes.value = it
            }
        }

    fun addNote(note: TaskEntity) = viewModelScope.launch {
        repository.insertTask(task = note)
    }

    fun deleteNote(note: TaskEntity) = viewModelScope.launch {
        repository.deleteTask(note)
    }

    fun updateNote(note: TaskEntity) = viewModelScope.launch {
        repository.updateTask(note)
    }

    fun getTaskById(id: Int) = viewModelScope.launch {
        _note.value = repository.getTaskById(id)
    }

    fun getResults(query: String) = viewModelScope.launch {
        repository.searchResults(query).collect {
            _result.value = it

        }
    }

}