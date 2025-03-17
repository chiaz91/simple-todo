package com.cy.practice.todo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [TodoEntity::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun getTodoDao(): TodoDao

    companion object {
        private const val DB_NAME = "todo_db"

        fun getInstance(context: Context) =
            Room.databaseBuilder(context, TodoDatabase::class.java, DB_NAME)
                .build()
    }
}