package com.cy.practice.todo.ui.screen.todo_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cy.practice.todo.domain.model.Todo


@Composable
fun TodoListScreen(modifier: Modifier = Modifier, vm: TodoListViewModel = hiltViewModel()) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    TodoListScreen(uiState, vm::onAction, modifier)
}

@Composable
fun TodoListScreen(
    state: TodoListState,
    onAction: (TodoListAction) -> Unit,
    modifier: Modifier = Modifier
) {
    var title by rememberSaveable { mutableStateOf("") }

    Column(modifier = modifier.imePadding()) {
        TodoList(
            state.todos,
            { todo, isChecked ->
                onAction(TodoListAction.EditTodo(todo.copy(isDone = isChecked)))
            },
            { todo -> onAction(TodoListAction.DeleteTodo(todo)) },
            modifier = Modifier.weight(1f)
        )
        Row {
            TextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text("Enter new task") },
                textStyle = LocalTextStyle.current.copy(),
                trailingIcon = {
                    IconButton(
                        enabled = title.isNotBlank(),
                        onClick = {
                            val newTodo = Todo(title = title)
                            onAction(TodoListAction.AddTodo(newTodo))
                        }
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add Task",
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(100.dp))
                    .weight(1f),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                ),
            )
        }
    }
}

@Composable
private fun TodoList(
    todos: List<Todo>,
    onCheckChanged: (Todo, Boolean) -> Unit,
    onDelete: (Todo) -> Unit,
    modifier: Modifier = Modifier
) {
    if (todos.isEmpty()) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text(text = "No task found")
        }
    } else {
        LazyColumn(modifier = modifier) {
            items(todos, key = { it.id }) {
                SwipeableTodoItem(it, onCheckChanged, onDelete, modifier = Modifier.animateItem())
//                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun TodoItem(
    todo: Todo,
    onCheckChanged: (Todo, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val textStyle = if (!todo.isDone) {
        LocalTextStyle.current
    } else {
        LocalTextStyle.current.copy(textDecoration = TextDecoration.LineThrough)
    }
    ListItem(
        leadingContent = {
            Checkbox(
                checked = todo.isDone,
                onCheckedChange = { check ->
                    onCheckChanged(todo, check)
                },
            )
        },
        headlineContent = {
            Text(
                text = todo.title, style = textStyle, maxLines = 2, overflow = TextOverflow.Ellipsis
            )
        },
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.background)
    )
}

@Composable
private fun SwipeableTodoItem(
    todo: Todo,
    onCheckChanged: (Todo, Boolean) -> Unit,
    onDelete: (Todo) -> Unit,
    modifier: Modifier = Modifier
) {
    val dismissState = rememberSwipeToDismissBoxState(
        positionalThreshold = { totalDistance -> totalDistance * 0.3f }
    )

    LaunchedEffect(dismissState.currentValue) {
        when (dismissState.currentValue) {
            SwipeToDismissBoxValue.StartToEnd -> {
                onCheckChanged(todo, true)
                dismissState.snapTo(SwipeToDismissBoxValue.Settled)
            }

            SwipeToDismissBoxValue.EndToStart -> {
                onDelete(todo)
                dismissState.snapTo(SwipeToDismissBoxValue.Settled)
            }

            SwipeToDismissBoxValue.Settled -> Unit
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        backgroundContent = { TodoItemDismissBackground(dismissState) },
        enableDismissFromStartToEnd = true,
        enableDismissFromEndToStart = true
    ) {
        TodoItem(
            todo,
            onCheckChanged,
        )
    }
}

@Composable
private fun TodoItemDismissBackground(
    dismissState: SwipeToDismissBoxState,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (dismissState.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.errorContainer
        SwipeToDismissBoxValue.StartToEnd -> MaterialTheme.colorScheme.primaryContainer
        SwipeToDismissBoxValue.Settled -> Color.Transparent
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(12.dp, 8.dp),
    ) {

        when (dismissState.dismissDirection) {
            SwipeToDismissBoxValue.StartToEnd -> {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Check completed",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
            }

            SwipeToDismissBoxValue.EndToStart -> {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete task",
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.align(Alignment.CenterEnd)

                )
            }

            SwipeToDismissBoxValue.Settled -> Unit
        }
    }
}