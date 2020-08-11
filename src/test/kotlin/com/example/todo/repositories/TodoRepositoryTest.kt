package com.example.todo.repositories

import com.example.todo.models.Todo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.findByIdOrNull

@DataJpaTest
class TodoRepositoryTest @Autowired constructor(
        val entityManager: TestEntityManager,
        val todoRepository: TodoRepository) {

    @Test
    fun `When findByIdOrNull`() {
        val todo = todoEntity()
        entityManager.persist(todo)
        entityManager.flush()

        val found = todoRepository.findByIdOrNull(todo.id!!)
        println(found)
        assertThat(found).isEqualTo(todo)
    }

    @Test
    fun `When findAllByOrderByAddedAtDesc`() {
        val todo = todoEntity()
        entityManager.persist(todo)
        entityManager.flush()

        val found = todoRepository.findAllByOrderByAddedAtDesc()
        assertThat(found.iterator().next()).isEqualTo(todo)
    }

    // --------------------------------------

    companion object {
        fun todoEntity(): Todo = Todo (
                title = "Test Title",
                description = "This is a test description"
        )

        fun todoEntity2(): Todo = Todo (
                title = "Test Title2",
                description = "This is a test description2"
        )

        fun todoAttributes(): Map<String, String> {
            return mapOf(
                    "title" to "Posted title",
                    "description" to "Posted description"
            )
        }
    }
}