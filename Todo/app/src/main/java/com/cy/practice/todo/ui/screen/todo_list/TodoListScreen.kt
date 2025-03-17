package com.cy.practice.todo.ui.screen.todo_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cy.practice.todo.domain.model.Todo
import com.cy.practice.todo.ui.screen.todo_list.component.NewTodoBottomSheet
import com.cy.practice.todo.ui.screen.todo_list.component.TodoList
import com.cy.practice.todo.ui.theme.SimpleTodoTheme


@Composable
fun TodoListScreen(modifier: Modifier = Modifier, vm: TodoListViewModel = hiltViewModel()) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    TodoListScreen(uiState, vm::onAction, modifier)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
    state: TodoListState,
    onAction: (TodoListAction) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddTask by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier.imePadding()) {
        TodoList(
            state.todos,
            { todo, isChecked ->
                onAction(TodoListAction.EditTodo(todo.copy(isDone = isChecked)))
            },
            { todo -> onAction(TodoListAction.DeleteTodo(todo)) },
            modifier = Modifier.weight(1f)
        )

        TextButton(
            onClick = { showAddTask = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add new task",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text("Add Task")
        }
    }

    if (showAddTask) {
        NewTodoBottomSheet(
            {showAddTask = false},
            {todo -> onAction(TodoListAction.AddTodo(todo))}
        )
    }
}




@Preview(showBackground = true)
@Composable
fun TodoScreenPreview(modifier: Modifier = Modifier) {
    val state = TodoListState(
        todos = listOf(
            Todo(0, "Do homework", true),
            Todo(1, "Do exercise", false)
        )
    )
    SimpleTodoTheme {
        TodoListScreen(
            state = state,
            onAction = {}
        )
    }
}