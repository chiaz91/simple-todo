package com.cy.practice.todo.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: TodoEntity)//: Long

    @Update
    suspend fun update(todo: TodoEntity)//: Int

    @Delete
    suspend fun delete(todo: TodoEntity)//: Int

    @Query("SELECT * FROM todo_table")
    fun getTodoList(): Flow<List<TodoEntity>>

}