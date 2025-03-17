package com.cy.practice.todo.data.mapper

import com.cy.practice.todo.data.local.TodoEntity
import com.cy.practice.todo.domain.model.Todo


fun TodoEntity.toModel() = Todo(
    id, title, isDone
)

fun Todo.toEntity() = TodoEntity(
    id, title, isDone
)

fun List<TodoEntity>.toModelList(): List<Todo> {
    return this.map { it.toModel() }
}

fun List<Todo>.toEntityList(): List<TodoEntity> {
    return this.map { it.toEntity() }
}