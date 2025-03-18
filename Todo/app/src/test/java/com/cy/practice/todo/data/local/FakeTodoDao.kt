package com.cy.practice.todo.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow


class FakeTodoDao : TodoDao {
    private val todosFlow = MutableStateFlow<List<TodoEntity>>(emptyList())

    override suspend fun insert(todo: TodoEntity) {
        todosFlow.value += todo
    }

    override suspend fun update(todo: TodoEntity) {
        todosFlow.value = todosFlow.value.map {
            if (it.id == todo.id) todo else it
        }
    }

    override suspend fun delete(todo: TodoEntity) {
        todosFlow.value = todosFlow.value.filter { it.id != todo.id }
    }

    override fun getTodoList(): Flow<List<TodoEntity>> = todosFlow
}