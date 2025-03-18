package com.cy.practice.todo.data.repository

import com.cy.practice.todo.domain.model.Todo
import com.cy.practice.todo.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeTodoRepository : TodoRepository {
    private val todosFlow = MutableStateFlow<List<Todo>>(emptyList())

    override suspend fun insert(todo: Todo) {
        todosFlow.value += todo
    }

    override suspend fun update(todo: Todo) {
        todosFlow.value = todosFlow.value.map {
            if (it.id == todo.id) todo else it
        }
    }

    override suspend fun delete(todo: Todo) {
        todosFlow.value = todosFlow.value.filter { it.id != todo.id }
    }

    override fun getTodoList(): Flow<List<Todo>> = todosFlow
}