package com.cy.practice.todo.ui.screen.todo_list

import com.cy.practice.todo.domain.model.Todo

sealed class TodoListEvent {
    data object TodoSaved : TodoListEvent()
    data class TodoDeleted(val todo: Todo) : TodoListEvent()
}