package com.cy.practice.todo.ui.screen.todo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cy.practice.todo.domain.model.Todo
import com.cy.practice.todo.domain.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val todoRepository: TodoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodoListState())
    val uiState = _uiState.asStateFlow()

    init {
        todoRepository.getTodoList()
            .onEach { todos ->
                todos.forEach{ Timber.d(it.toString())}

                _uiState.update { state ->
                    state.copy(todos = todos.sortedBy { it.isDone })
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: TodoListAction) {
        when (action) {
            is TodoListAction.AddTodo -> addTodo(action.todo)
            is TodoListAction.EditTodo -> editTodo(action.todo)
            is TodoListAction.DeleteTodo -> deleteTodo(action.todo)
        }
    }

    private fun addTodo(newTodo: Todo) {
        viewModelScope.launch {
            todoRepository.insert(newTodo)
        }
    }

    private fun editTodo(updatingTodo: Todo) {
        viewModelScope.launch {
            todoRepository.update(updatingTodo)
        }
    }

    private fun deleteTodo(deletingTodo: Todo) {
        viewModelScope.launch {
            todoRepository.delete(deletingTodo)
        }
    }


}