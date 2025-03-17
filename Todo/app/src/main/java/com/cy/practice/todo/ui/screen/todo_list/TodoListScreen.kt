package com.cy.practice.todo.ui.screen.todo_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
    var title by rememberSaveable {  mutableStateOf("") }

    Column( modifier=modifier.imePadding()) {
        TodoList(
            state.todos,
            {todo, isChecked ->
                onAction(TodoListAction.EditTodo(todo.copy(isDone = isChecked)))
            },
            modifier=Modifier.weight(1f)
        )
        Row {
            TextField(
                value = title,
                onValueChange = { title=it },
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
    modifier: Modifier = Modifier
) {
    if (todos.isEmpty()) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text(text = "No task found")
        }
    } else {
        LazyColumn(modifier = modifier) {
            items(todos, key = { it.id }) {
                TodoItem(it, onCheckChanged, modifier = Modifier.animateItem())
                HorizontalDivider()
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