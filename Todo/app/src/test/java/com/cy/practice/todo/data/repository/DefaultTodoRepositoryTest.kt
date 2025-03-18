package com.cy.practice.todo.data.repository

import com.cy.practice.todo.data.local.FakeTodoDao
import com.cy.practice.todo.domain.model.Todo
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DefaultTodoRepositoryTest {

    private lateinit var fakeTodoDao: FakeTodoDao
    private lateinit var repository: DefaultTodoRepository

    @Before
    fun setUp() {
        fakeTodoDao = FakeTodoDao()
        repository = DefaultTodoRepository(fakeTodoDao)
    }

    @Test
    fun insertTodo_IfTodoIsInserted_ThenItExistsInSource() = runTest {
        // GIVEN
        val todo = Todo(id = 1, title = "Task 1", isDone = false)
        // WHEN
        repository.insert(todo)
        // ASSERT
        val todos = repository.getTodoList().first()
        assertThat(todos).contains(todo)
    }

    @Test
    fun updateTodo_IfTodoExists_ThenItIsUpdatedInSource() = runTest {
        // GIVEN
        val todo = Todo(id=10, title = "Old Task", isDone = false)
        repository.insert(todo)

        // WHEN
        val updatedTodo = Todo(id = todo.id, title = "Updated Task", isDone = true)
        repository.update(updatedTodo)

        // ASSERT
        val loadedTodoList = repository.getTodoList().first()
        val loadedTodo =  loadedTodoList.first { it.id == todo.id }
        assertThat(loadedTodo).isEqualTo(updatedTodo)
    }

    @Test
    fun deleteTodo_IfTodoIsDeleted_ThenItIsRemovedFromSource() = runTest {
        // GIVEN
        val todo = Todo(id = 1, title = "Task 1", isDone = false)
        repository.insert(todo)
        // WHEN
        repository.delete(todo)
        // ASSERT
        val todos = repository.getTodoList().first()
        assertThat(todos).doesNotContain(todo)
    }

    @Test
    fun getTodoList_IfTodosDidNotExist_ThenReturnsEmptyList() = runTest {
        // WHEN
        val todos = repository.getTodoList().first()

        // ASSERT
        assertThat(todos).isEmpty()
    }

    @Test
    fun getTodoList_IfTodosExist_ThenReturnsCorrectList() = runTest {
        // GIVEN
        val todo1 = Todo(id = 1, title = "Task 1", isDone = false)
        val todo2 = Todo(id = 2, title = "Task 2", isDone = true)
        repository.insert(todo1)
        repository.insert(todo2)

        // WHEN
        val todos = repository.getTodoList().first()

        // ASSERT
        assertThat(todos).isEqualTo(listOf(todo1, todo2))
    }
}