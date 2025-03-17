package com.cy.practice.todo.domain.repository

import com.cy.practice.todo.domain.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    suspend fun insert(todo: Todo)
    suspend fun update(todo: Todo)
    suspend fun delete(todo: Todo)
    fun getTodoList(): Flow<List<Todo>>
}