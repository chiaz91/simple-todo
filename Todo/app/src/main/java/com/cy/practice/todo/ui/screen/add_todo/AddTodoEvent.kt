package com.cy.practice.todo.ui.screen.add_todo


sealed class AddTodoEvent {
    data object TodoSaved : AddTodoEvent()
    data class Error(val message: String) : AddTodoEvent()
}