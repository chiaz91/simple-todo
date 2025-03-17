package com.cy.practice.todo.ui.screen.add_todo


sealed class AddTodoAction {
    data class UpdateTitle(val title: String) : AddTodoAction()
    data object Save : AddTodoAction()
}