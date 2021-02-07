package com.sammidev.repository

import com.sammidev.entity.Student
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StudentRepository : JpaRepository<Student, Long> {
    fun deleteStudentById(id: Long)
    fun findStudentById(id: Long): Student?
}