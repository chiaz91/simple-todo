package com.cy.practice.todo.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class TasksDaoTest {
    private lateinit var database: TodoDatabase
    private lateinit var todoDao: TodoDao


    @Before
    fun initDb() {
        // Using an in-memory database so that the information stored here disappears when the
        // process is killed.
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            TodoDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        todoDao = database.getTodoDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() = database.close()

    @Test
    fun insertTodo_IfTodoIsInserted_ThenItIsStoredInDatabase() = runTest {
        // GIVEN
        val todo = TodoEntity(id = 1, title = "Task1", isDone = false)
        todoDao.insert(todo)
        // WHEN
        val loadedList = todoDao.getTodoList().first()
        // ASSERT
        assertThat(loadedList).contains(todo)
    }


    @Test
    fun updateTodo_IfTodoExists_ThenItIsUpdatedInDatabase() = runTest {
        // GIVEN
        val todo = TodoEntity(id = 1, title = "Old Task", isDone = false)
        todoDao.insert(todo)

        // WHEN
        val updatedTodo = todo.copy(title = "Updated Task", isDone = true)
        todoDao.update(updatedTodo)

        // ASSERT
        val loadedList = todoDao.getTodoList().first()
        val loadedTodo = loadedList.first { it.id == todo.id }
        assertThat(loadedTodo).isEqualTo(updatedTodo)
    }


    @Test
    fun deleteTodo_IfTodoExists_ThenItIsRemovedFromDatabase() = runTest {
        // GIVEN
        val todo = TodoEntity(id = 1, title = "Task to Delete", isDone = false)
        todoDao.insert(todo)

        // WHEN
        todoDao.delete(todo)

        // ASSERT
        val loadedList = todoDao.getTodoList().first()
        assertThat(loadedList).doesNotContain(todo)
    }

    @Test
    fun deleteTodo_IfTodoNotExists_ThenItIsRemovedFromDatabase() = runTest {
        // GIVEN
        val todo = TodoEntity(id = 1, title = "Task to Delete", isDone = false)

        // WHEN
        todoDao.delete(todo)

        // ASSERT
        val loadedList = todoDao.getTodoList().first()
        assertThat(loadedList).doesNotContain(todo)
    }

    @Test
    fun getTodoList_IfTodosNotExist_ThenReturnsEmptyList() = runTest {
        // WHEN
        val loadedList = todoDao.getTodoList().first()

        // ASSERT
        assertThat(loadedList).isEmpty()
    }

    @Test
    fun getTodoList_IfTodosExist_ThenReturnsCorrectList() = runTest {
        // GIVEN
        val todo1 = TodoEntity(id = 1, title = "Task 1", isDone = false)
        val todo2 = TodoEntity(id = 2, title = "Task 2", isDone = true)
        todoDao.insert(todo1)
        todoDao.insert(todo2)
        val todoList = listOf(todo1, todo2)

        // WHEN
        val loadedList = todoDao.getTodoList().first()

        // ASSERT
        assertThat(loadedList).isEqualTo(todoList)
    }
}