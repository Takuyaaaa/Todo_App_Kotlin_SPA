package com.example.todo.controllers

import com.example.todo.models.Todo
import com.example.todo.repositories.TodoRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/todo")
class TodoController(private val repository: TodoRepository) {

    @GetMapping("")
    fun index(): Iterable<Todo> {
        return repository.findAllByOrderByAddedAtDesc()
    }

    @GetMapping("/{id}")
    fun show(@PathVariable id: Long): Todo {
        val entity = repository.findByIdOrNull(id)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "This todo does not exist")

        return entity
    }

    @PostMapping("")
    fun store(@RequestParam("title") title: String,
              @RequestParam("description") description: String): Todo {
        val entity = Todo(
                title = title,
                description = description
        )

        return repository.save(entity)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long,
               @RequestParam("title") title: String?,
               @RequestParam("description") description: String?): Todo {
        val entity = repository.findByIdOrNull(id)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "This todo does not exist")

        if (title != null) {
            entity.title = title
        }
        if (description != null) {
            entity.description = description
        }

        return repository.save(entity)
    }
}