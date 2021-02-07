package com.sammidev.service.impl

import com.sammidev.entity.Student
import com.sammidev.error.NotFoundException
import com.sammidev.model.service.CreateStudentRequest
import com.sammidev.model.service.StudentResponse
import com.sammidev.model.service.UpdateStudentRequestById
import com.sammidev.repository.StudentRepository
import com.sammidev.service.StudentService
import org.springframework.stereotype.Service
import com.sammidev.validation.ValidatorUtil
import org.springframework.data.repository.findByIdOrNull
import org.springframework.validation.annotation.Validated
import java.time.LocalDateTime
import java.util.*

@Service
@Validated
class StudentServiceImpl(
        val studentRepository: StudentRepository,
        val validatorUtil: ValidatorUtil
) : StudentService {

    override fun addStudent(request: CreateStudentRequest): StudentResponse {
        validatorUtil.validate(request)

        var student = Student(
                id = null,
                name = request.name,
                identifier = UUID.randomUUID().toString(),
                email = request.email,
                phone = request.phone,
                major = request.major,
                imageUrl = request.imageUrl,
                createdAt = LocalDateTime.now(),
                updatedAt = null
        )

        val req = studentRepository.save(student)
        return convertStudentToStudentResponse(req)
    }

    override fun findAllStudents(): MutableList<StudentResponse> {
        return studentRepository.findAll().map { convertStudentToStudentResponse(it)
        }.toMutableList()
    }

    override fun updateStudentById(id: Long, updateStudentRequest: UpdateStudentRequestById): StudentResponse {
        val student = findStudentByIdOrThrowNotFound(id)
        validatorUtil.validate(student)

        student.apply {
            name = updateStudentRequest.name!!
            email = updateStudentRequest.email!!
            phone = updateStudentRequest.phone!!
            major = updateStudentRequest.major!!
            imageUrl = updateStudentRequest.imageUrl!!
            updatedAt = LocalDateTime.now()
        }

        studentRepository.save(student)
        return convertStudentToStudentResponse(student)
    }

    override fun findStudentById(id: Long): Student? {
        return studentRepository.findStudentById(id)
    }

    override fun deleteStudentById(id: Long) {
        val student = findStudentByIdOrThrowNotFound(id)
        studentRepository.delete(student)
    }

    private fun findStudentByIdOrThrowNotFound(id: Long): Student {
        val student = studentRepository.findByIdOrNull(id)
        if (student == null) {
            throw NotFoundException()
        } else {
            return student;
        }
    }

    private fun convertStudentToStudentResponse(student: Student) : StudentResponse{
        return StudentResponse(
                id = student.id,
                identifier = student.identifier!!,
                name = student.name,
                email = student.email,
                phone = student.phone,
                major = student.major,
                imageUrl = student.imageUrl,
                createdAt = student.createdAt,
                updatedAt = student.updatedAt
        )
    }
}