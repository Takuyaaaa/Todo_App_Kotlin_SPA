package com.example.todo.repositories

import com.example.todo.models.Todo
import org.springframework.data.repository.CrudRepository

interface TodoRepository: CrudRepository<Todo, Long> {
    fun findAllByOrderByAddedAtDesc(): Iterable<Todo>
}