package com.sammidev.model.service

import java.time.LocalDateTime


data class StudentResponse (
        var id: Long?,
        var name: String,
        var identifier: String,
        var email: String,
        var phone: String,
        var major: String,
        var imageUrl: String,
        var createdAt: LocalDateTime,
        var updatedAt: LocalDateTime?
)