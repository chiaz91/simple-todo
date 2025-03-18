package com.cy.practice.todo.ui.screen.add_todo

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cy.practice.todo.ui.theme.SimpleTodoTheme
import com.cy.practice.todo.util.ObserveAsEvents


@Composable
fun AddTodoScreen(
    onSaved: (Boolean) -> Unit,
    vm: AddTodoViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(vm.event) { event ->
        when (event) {
            is AddTodoEvent.TodoSaved -> { onSaved(true) }
            is AddTodoEvent.Error -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    AddTodoScreen(uiState, vm::onAction, onSaved)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodoScreen(
    uiState: AddTodoState,
    onAction: (AddTodoAction) -> Unit,
    onSaved: (Boolean) -> Unit,
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Task") },
                navigationIcon = {
                    IconButton(onClick = { onSaved(false) }) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .systemBarsPadding()
                .imePadding(),
            ) {
            TextField(
                value = uiState.title,
                onValueChange = { onAction(AddTodoAction.UpdateTitle(it)) },
                placeholder = { Text("Enter new task") },
                textStyle = LocalTextStyle.current.copy(),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                ),
            )
            TextButton(
                onClick = { onAction(AddTodoAction.Save) },
                enabled = uiState.title.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun TodoScreenPreview(modifier: Modifier = Modifier) {
    val state = AddTodoState()
    SimpleTodoTheme {
        AddTodoScreen(uiState = state, onAction = {}, onSaved = {})
    }
}

