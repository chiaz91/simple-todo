package com.cy.practice.todo.ui.screen.add_todo

import app.cash.turbine.test
import com.cy.practice.todo.MainCoroutineRule
import com.cy.practice.todo.data.repository.FakeTodoRepository
import com.cy.practice.todo.domain.model.Todo
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class AddTodoViewModelTest{
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeTodoRepository: FakeTodoRepository
    private lateinit var viewModel: AddTodoViewModel

    @Before
    fun setUp() {
        fakeTodoRepository = FakeTodoRepository()
        viewModel = AddTodoViewModel(fakeTodoRepository)
    }


    @Test
    fun updateTitle_ShouldUpdateUiStateWithNewTitle() = runTest {
        // WHEN
        viewModel.onAction(AddTodoAction.UpdateTitle("New Task"))

        // ASSERT
        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(AddTodoState(title = "New Task"))
        }
    }

    @Test
    fun saveTask_WithValidTitle_ShouldSaveTodoAndEmitTodoSavedEvent() = runTest {
        // GIVEN
        viewModel.onAction(AddTodoAction.UpdateTitle("Valid Task"))

        // WHEN
        viewModel.onAction(AddTodoAction.Save)

        // ASSERT
        fakeTodoRepository.getTodoList().drop(1).test {
            assertThat(awaitItem()).contains(Todo(title = "Valid Task"))
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.event.test {
            assertThat(awaitItem()).isEqualTo(AddTodoEvent.TodoSaved)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun saveTask_WithEmptyTitle_ShouldEmitErrorEvent() = runTest {
        // WHEN
        viewModel.onAction(AddTodoAction.Save)

        // ASSERT
        viewModel.event.test {
            assertThat(awaitItem()).isEqualTo(AddTodoEvent.Error("Title cannot be empty"))
            cancelAndIgnoreRemainingEvents()
        }
    }
}