package com.cy.practice.todo.ui.screen.todo_list.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cy.practice.todo.domain.model.Todo
import com.cy.practice.todo.ui.theme.SimpleTodoTheme


@Composable
fun TodoList(
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



@Preview(showBackground = true)
@Composable
fun TodoItemPreview(modifier: Modifier = Modifier) {
    val todo = Todo(0, "Do homework", true)

    SimpleTodoTheme {
        SwipeableTodoItem(todo, { _, _ -> }, {})
    }
}