package com.sammidev.entity

import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "student")
data class Student(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(updatable = false)
        var id: Long?,

        @Column(name = "identifier" , nullable = false, updatable = false)
        var identifier: String,

        @Column(name = "name" ,nullable = false)
        var name: String,

        @Column(name = "email" ,nullable = false, unique = true)
        var email: String,

        @Column(name = "phone" ,nullable = false, unique = true)
        var phone: String,

        @Column(name = "major" ,nullable = false)
        var major: String,

        @Column(name = "image_url" ,nullable = false, unique = true)
        var imageUrl: String,

        @Column(name = "created_at")
        var createdAt: LocalDateTime,

        @Column(name = "updated_at")
        var updatedAt: LocalDateTime?
)