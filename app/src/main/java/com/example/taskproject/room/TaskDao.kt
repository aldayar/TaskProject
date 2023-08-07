package com.example.taskproject.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.taskproject.model.Task

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("select * from task")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("select * from task where isDone = :isCompleted")
    fun getTasksByCompletedStatus(isCompleted: Boolean): LiveData<List<Task>>
}