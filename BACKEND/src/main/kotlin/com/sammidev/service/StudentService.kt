package com.sammidev.service

import com.sammidev.entity.Student
import com.sammidev.model.service.CreateStudentRequest
import com.sammidev.model.service.StudentResponse
import com.sammidev.model.service.UpdateStudentRequestById
import javax.validation.Valid

interface StudentService {
    fun addStudent(@Valid student: CreateStudentRequest): StudentResponse
    fun findAllStudents(): MutableList<StudentResponse>
    fun updateStudentById(id: Long, updateStudentRequest: UpdateStudentRequestById): StudentResponse
    fun findStudentById(id: Long): Student?
    fun deleteStudentById(id: Long)
}