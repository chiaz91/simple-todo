package com.cy.practice.todo.di

import android.content.Context
import com.cy.practice.todo.data.local.TodoDatabase
import com.cy.practice.todo.data.repository.DefaultTodoRepository
import com.cy.practice.todo.domain.repository.TodoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideTodoDatabase(@ApplicationContext context: Context) : TodoDatabase{
        return TodoDatabase.getInstance(context)
    }


    @Provides
    @Singleton
    fun provideRepository(todoDatabase: TodoDatabase): TodoRepository {
        return DefaultTodoRepository(todoDatabase.getTodoDao())
    }
}