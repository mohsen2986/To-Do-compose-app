package com.example.to_docompose.data.repositories

import com.example.to_docompose.data.TodoDao
import com.example.to_docompose.data.models.TodoTask
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class TodoRepository @Inject constructor(
    private val todoDao: TodoDao
) {

    val tasks: Flow<List<TodoTask>> = todoDao.getAllData()
    val sortByLowPriority = todoDao.sortByLowPriority()
    val sortByHighPriority = todoDao.sortByHighPriority()

    suspend fun getSelectedTask(taskId: Int) =
        todoDao.getById(taskId)

    suspend fun insertData(toDoData: TodoTask){
        todoDao.insertData(toDoData)
    }

    suspend fun updateData(toDoData: TodoTask) =
        todoDao.updateData(toDoData)

    suspend fun deleteItem(toDoData: TodoTask) =
        todoDao.deleteItem(toDoData)

    suspend fun deleteAll() =
        todoDao.deleteAll()

    fun searchDatabase(searchQuery: String) =
        todoDao.searchDatabase(searchQuery)
}