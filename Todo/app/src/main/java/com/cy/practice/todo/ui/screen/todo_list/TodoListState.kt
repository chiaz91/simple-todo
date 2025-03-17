package com.cy.practice.todo.ui.screen.todo_list

import com.cy.practice.todo.domain.model.Todo


data class TodoListState(
    val todos: List<Todo> = emptyList(),
)