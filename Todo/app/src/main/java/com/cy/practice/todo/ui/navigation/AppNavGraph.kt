package com.cy.practice.todo.ui.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cy.practice.todo.ui.screen.add_todo.AddTodoScreen
import com.cy.practice.todo.ui.screen.todo_list.TodoListScreen

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Routes.TodoList
    ) {
        composable<Routes.TodoList> {
            TodoListScreen(onAddTodo = {
                navController.navigate(Routes.AddTodo)
            })
        }

        composable<Routes.AddTodo> {
            AddTodoScreen(onSaved = {
                navController.navigateUp()
            })
        }
    }

}