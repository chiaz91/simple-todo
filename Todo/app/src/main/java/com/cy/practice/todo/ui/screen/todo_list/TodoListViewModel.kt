package com.cy.practice.todo.ui.screen.todo_list

import androidx.lifecycle.ViewModel
import com.cy.practice.todo.domain.model.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodoListState())
    val uiState = _uiState.asStateFlow()



    init {
        // create dummy data
        val data = mutableListOf<Todo>()
        repeat(10) {
            data.add(Todo(id = it, title = "Task ${it+1}"))
        }
        _uiState.update {
            it.copy(todos = data)
        }
    }


}