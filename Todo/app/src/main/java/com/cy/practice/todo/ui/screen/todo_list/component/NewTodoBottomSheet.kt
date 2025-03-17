package com.cy.practice.todo.ui.screen.todo_list.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cy.practice.todo.domain.model.Todo
import com.cy.practice.todo.ui.theme.SimpleTodoTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTodoBottomSheet(
    onDismissRequest: () -> Unit,
    onSave: (Todo) -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(),
) {
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        dragHandle = {},
    ) {
        var title by rememberSaveable { mutableStateOf("") }

        Column(
            modifier = Modifier
                .padding(8.dp, 12.dp)
                .wrapContentSize()
        ) {
            Text(
                "New task",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            TextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text("Enter new task") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth(),
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
                onClick = {
                    val todp = Todo(title = title)
                    title = ""
                    onSave(todp)
                    onDismissRequest()
                },
                enabled = title.isNotBlank(),
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.End)
            ) {
                Text("Save")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AddNewTodoPreview(modifier: Modifier = Modifier) {
    val state = rememberStandardBottomSheetState(
        initialValue = SheetValue.Expanded,
    )

    SimpleTodoTheme {
        NewTodoBottomSheet({}, {}, state)
    }
}