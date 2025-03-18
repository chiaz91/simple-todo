package com.cy.practice.todo.ui.screen.todo_list

import app.cash.turbine.test
import com.cy.practice.todo.MainCoroutineRule
import com.cy.practice.todo.data.repository.FakeTodoRepository
import com.cy.practice.todo.domain.model.Todo
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TodoListViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeTodoRepository: FakeTodoRepository
    private lateinit var viewModel: TodoListViewModel

    @Before
    fun setUp() {
        fakeTodoRepository = FakeTodoRepository()
        viewModel = TodoListViewModel(fakeTodoRepository)
    }

    @Test
    fun testUiState_IfRepositoryHasTodos_ThenUiStateIsUpdated() = runTest {
        // GIVEN
        val todo1 = Todo(id = 1, title = "Task 1", isDone = false)
        val todo2 = Todo(id = 2, title = "Task 2", isDone = false)
        fakeTodoRepository.insert(todo1)
        fakeTodoRepository.insert(todo2)

        // ASSERT
        viewModel.uiState.drop(1).test {
            assertThat(awaitItem()).isEqualTo(TodoListState(todos = listOf(todo1, todo2)))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testUiState_IfRepositoryHasTodos_ThenCheckedTodoIsSortedBehind() = runTest {
        // GIVEN
        val todo1 = Todo(id = 1, title = "Task 1", isDone = false)
        val todo2 = Todo(id = 2, title = "Task 2", isDone = true)
        val todo3 = Todo(id = 2, title = "Task 3", isDone = false)
        fakeTodoRepository.insert(todo1)
        fakeTodoRepository.insert(todo2)
        fakeTodoRepository.insert(todo3)

        // ASSERT
        viewModel.uiState.drop(1).test {
            assertThat(awaitItem()).isEqualTo(TodoListState(todos = listOf(todo1, todo3, todo2)))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testAddAction_IfAddTodo_ThenTodoIsAddedAndEventEmitted() = runTest {
        // GIVEN
        val todo = Todo(id = 1, title = "New Task", isDone = false)

        // WHEN
        viewModel.onAction(TodoListAction.AddTodo(todo))

        // ASSERT
        viewModel.uiState.drop(1).test {
            assertThat(awaitItem()).isEqualTo(TodoListState(todos = listOf(todo)))
            cancelAndIgnoreRemainingEvents()
        }
        viewModel.event.test {
            assertThat(awaitItem()).isEqualTo(TodoListEvent.TodoSaved)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testEditAction_IfEditTodo_ThenTodoIsUpdatedAndEventEmitted() = runTest {
        // GIVEN
        val todo = Todo(id = 1, title = "Old Task", isDone = false)
        fakeTodoRepository.insert(todo)
        val updatedTodo = todo.copy(title = "Updated Task", isDone = true)


        // WHEN
        viewModel.onAction(TodoListAction.EditTodo(updatedTodo))

        // ASSERT
        viewModel.uiState.drop(1).test {
            assertThat(awaitItem()).isEqualTo(TodoListState(todos = listOf(todo)))
            assertThat(awaitItem()).isEqualTo(TodoListState(todos = listOf(updatedTodo)))
        }

        viewModel.event.test {
            assertThat(awaitItem()).isEqualTo(TodoListEvent.TodoSaved)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testDeleteAction_IfDeleteTodo_ThenTodoIsDeletedAndEventEmitted() = runTest {
        // GIVEN
        val todo = Todo(id = 1, title = "Task to Delete", isDone = false)
        fakeTodoRepository.insert(todo)

        // WHEN
        viewModel.onAction(TodoListAction.DeleteTodo(todo))

        // ASSERT
        viewModel.uiState.drop(1).test {
            assertThat(awaitItem()).isEqualTo(TodoListState(todos = listOf(todo)))
            assertThat(awaitItem()).isEqualTo(TodoListState(todos = emptyList()))
            cancelAndIgnoreRemainingEvents()
        }
        viewModel.event.test {
            assertThat(awaitItem()).isEqualTo(TodoListEvent.TodoDeleted(todo))
            cancelAndIgnoreRemainingEvents()
        }
    }
}