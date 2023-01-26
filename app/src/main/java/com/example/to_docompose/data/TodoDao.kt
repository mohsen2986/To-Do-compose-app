package com.example.to_docompose.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.to_docompose.data.models.TodoTask
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao{
    @Query("SELECT * FROM todo_list ORDER BY id ASC")
    fun getAllData(): Flow<List<TodoTask>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(toDoData: TodoTask)

    @Update
    suspend fun updateData(toDoData: TodoTask)

    @Delete
    suspend fun deleteItem(toDoData: TodoTask)

    @Query("DELETE FROM todo_list")
    suspend fun deleteAll()

    @Query("SELECT * FROM todo_list WHERE title LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): Flow<List<TodoTask>>

    @Query("SELECT * FROM todo_list ORDER BY CASE WHEN priority LIKE 'H%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'L%' THEN 3 END")
    fun sortByHighPriority(): Flow<List<TodoTask>>

    @Query("SELECT * FROM todo_list ORDER BY CASE WHEN priority LIKE 'L%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END")
    fun sortByLowPriority(): Flow<List<TodoTask>>

    @Query("SELECT * FROM todo_list WHERE id = :taskId")
    abstract fun getById(taskId: Int): Flow<TodoTask>
}