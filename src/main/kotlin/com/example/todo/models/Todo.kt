package com.example.todo.models

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "todos")
class Todo (
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)  var id: Long? = null,
        @Column(name = "addedAt")  var addedAt: LocalDateTime = LocalDateTime.now(),
        @Column(name = "title") var title: String,
        @Column(name = "description") var description: String,
        @Column(name = "done") var done: Boolean = false
)