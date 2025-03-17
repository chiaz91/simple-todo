package com.cy.practice.todo.data.repository

import com.cy.practice.todo.data.local.TodoDao
import com.cy.practice.todo.data.mapper.toEntity
import com.cy.practice.todo.data.mapper.toModelList
import com.cy.practice.todo.domain.model.Todo
import com.cy.practice.todo.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultTodoRepository(
    private val todoDao: TodoDao,
): TodoRepository {
    override suspend fun insert(todo: Todo) {
        todoDao.insert(todo.toEntity())
    }
    override suspend fun update(todo: Todo) {
        todoDao.update(todo.toEntity())
    }
    override suspend fun delete(todo: Todo) {
        todoDao.delete(todo.toEntity())
    }
    override fun getTodoList(): Flow<List<Todo>> {
        return todoDao.getTodoList().map { it.toModelList() }
    }
}