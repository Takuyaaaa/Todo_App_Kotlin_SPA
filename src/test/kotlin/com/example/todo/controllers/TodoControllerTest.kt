package com.example.todo.controllers

import com.beust.klaxon.*
import com.example.todo.repositories.TodoRepository
import com.example.todo.repositories.TodoRepositoryTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TodoControllerTest @Autowired constructor(
        val mockMvc: MockMvc,
        val todoRepository: TodoRepository) {

    @Test
    fun testIndex() {
        // エンティティ登録
        todoRepository.save(TodoRepositoryTest.todoEntity())
        todoRepository.save(TodoRepositoryTest.todoEntity2())

        // --------------------------------------

        // 取得処理
        val response = this.mockMvc.perform(get("/todo"))
                .andExpect(status().isOk)
                .andReturn()
                .response
                .contentAsString

        // --------------------------------------

        // 2件取得できること
        val result = Parser.default().parse(response.byteInputStream()) as JsonArray<*>
        val count = result.long("id").size
        assertThat(count).isEqualTo(2)
    }

    @Test
    fun testShow() {
        // エンティティ登録
        val entity = todoRepository.save(TodoRepositoryTest.todoEntity())
        val entityId = entity.id

        // --------------------------------------

        // 取得処理
        val response = this.mockMvc.perform(get("/todo/$entityId"))
                .andExpect(status().isOk)
                .andReturn()
                .response
                .contentAsString

        // --------------------------------------

        // 正しく取得できること
        val result = Parser.default().parse(response.byteInputStream()) as JsonObject
        val fetchedId = result.toMap()["id"] as Int
        assertThat(entityId).isEqualTo(fetchedId.toLong())
    }

    @Test
    fun testStore() {
        // 登録処理
        val response = this.mockMvc.perform(post("/todo")
                .param("title", "Posted Title")
                .param("description", "Posted description"))
                .andExpect(status().isOk)
                .andReturn()
                .response
                .contentAsString

        // --------------------------------------

        // 正しく保存されていること
        val result = Parser.default().parse(response.byteInputStream()) as JsonObject
        val fetchedId = result.toMap()["id"] as Int
        assertThat(todoRepository.findByIdOrNull(fetchedId.toLong())).isNotNull
    }

    @Test
    fun testUpdate() {
        // エンティティ登録
        val entity = todoRepository.save(TodoRepositoryTest.todoEntity())
        val entityId = entity.id

        // --------------------------------------

        // 更新用パラメータ
        val newTitle = TodoRepositoryTest.todoEntity2().title
        val newDescription = TodoRepositoryTest.todoEntity2().description

        // --------------------------------------

        // 更新処理
        val response = this.mockMvc.perform(put("/todo/$entityId")
                .param("title", newTitle)
                .param("description", newDescription))
                .andExpect(status().isOk)
                .andReturn()
                .response
                .contentAsString

        // --------------------------------------

        // 正しく更新されていること
        val result = Parser.default().parse(response.byteInputStream()) as JsonObject
        val fetchedId = result.toMap()["id"] as Int
        val fetched = todoRepository.findByIdOrNull(fetchedId.toLong())
        if (fetched != null) {
            assertThat(fetched.title).isEqualTo(newTitle)
            assertThat(fetched.description).isEqualTo(newDescription)
        }

    }


}