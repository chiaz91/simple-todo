package com.cy.practice.todo.ui.screen.todo_list

import com.cy.practice.todo.domain.model.Todo


sealed class TodoListAction {
    data object AddTodo : TodoListAction()
    data class EditTodo(val todo: Todo) : TodoListAction()
    data class DeleteTodo(val todo: Todo) : TodoListAction()
}