package com.cy.practice.todo.ui.screen.todo_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cy.practice.todo.R
import com.cy.practice.todo.domain.model.Todo
import com.cy.practice.todo.ui.screen.todo_list.component.TodoList
import com.cy.practice.todo.ui.theme.SimpleTodoTheme
import com.cy.practice.todo.util.ObserveAsEvents
import kotlinx.coroutines.launch


@Composable
fun TodoListScreen(
    onAddTodo: () -> Unit,
    modifier: Modifier = Modifier,
    vm: TodoListViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    ObserveAsEvents(vm.event) { event ->
        when(event) {
            is TodoListEvent.TodoDeleted -> scope.launch {
                snackbarHostState.currentSnackbarData?.dismiss()
                val lastDeleted = event.todo
                val result = snackbarHostState.showSnackbar(
                    message = "Task '${lastDeleted.title}' deleted",
                    actionLabel = "Undo",
                    duration = SnackbarDuration.Short
                )
                if (result == SnackbarResult.ActionPerformed) {
                    vm.onAction(TodoListAction.AddTodo(lastDeleted))
                }
            }
            else -> Unit
        }
    }
    TodoListScreen(uiState, onAddTodo, vm::onAction, modifier, snackbarHostState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
    state: TodoListState,
    onAddTodo: () -> Unit,
    onAction: (TodoListAction) -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TodoList(
                state.todos,
                { todo, isChecked ->
                    onAction(TodoListAction.EditTodo(todo.copy(isDone = isChecked)))
                },
                { todo -> onAction(TodoListAction.DeleteTodo(todo)) },
                modifier = Modifier.weight(1f)
            )

            TextButton(
                onClick = onAddTodo,
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
            onAddTodo = {},
            onAction = {}
        )
    }
}