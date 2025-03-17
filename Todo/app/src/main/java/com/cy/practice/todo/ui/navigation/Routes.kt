package com.cy.practice.todo.ui.navigation

import kotlinx.serialization.Serializable


sealed interface Routes {
    @Serializable
    data object TodoList : Routes

    @Serializable
    data object AddTodo : Routes
}