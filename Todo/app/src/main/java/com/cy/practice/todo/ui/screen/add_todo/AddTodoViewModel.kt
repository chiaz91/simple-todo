package com.cy.practice.todo.ui.screen.add_todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cy.practice.todo.domain.repository.TodoRepository
import com.cy.practice.todo.domain.model.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTodoViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTodoState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<AddTodoEvent>()
    val event = _event.receiveAsFlow()

    fun onAction(action: AddTodoAction) {
        when (action) {
            is AddTodoAction.UpdateTitle -> _uiState.update { it.copy(title = action.title) }
            is AddTodoAction.Save -> saveTask()
        }
    }

    private fun saveTask() {
        viewModelScope.launch {
            val error = validateForm()
            if (error != null) {
                _event.send(AddTodoEvent.Error(error))
                return@launch
            }
            val state = _uiState.value
            val todo = Todo( title = state.title)
            repository.insert(todo)
            _event.send(AddTodoEvent.TodoSaved)
        }
    }

    private fun validateForm(): String? {
        val state = _uiState.value
        val title = state.title
        if (title.isBlank()) {
            return "Title cannot be empty"
        }
        return null
    }

}