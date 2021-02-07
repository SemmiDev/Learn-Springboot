package com.sammidev

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import java.util.*

@SpringBootApplication
class BackendApplication

fun main(args: Array<String>) {
	runApplication<BackendApplication>(*args)
}
//
//class StudentNotFoundException(override val message: String) : RuntimeException(message)
//
//@Service
//class StudentService(private val studentRepository: StudentRepository) {
//
//	fun addStudent(student: Student): Student {
//		student.identifier = UUID.randomUUID().toString()
//		return studentRepository.save(student)
//	}
//
//	fun findAllStudents(): MutableList<Student> = studentRepository.findAll()
//	fun updateStudent(student: Student): Student = studentRepository.save(student)
//	fun findStudentById(id: Long): Student? {
//		return when {
//			studentRepository.findStudentById(id) != null -> {
//				studentRepository.findStudentById(id)
//			}
//			else -> throw StudentNotFoundException("STUDENT WITH $id NOT FOUND")
//		}
//	}
//
//	fun deleteStudent(id: Long): Void = studentRepository.deleteStudentById(id)
//}
//
//@RestController
//@RequestMapping("/api/v1/student")
//class StudentController(private val studentService: StudentService) {
//
//	@GetMapping("/find/{id}")
//	fun getStudent(@PathVariable id: Long): ResponseEntity<Student> {
//		val student = this.studentService.findStudentById(id)
//		return ResponseEntity(student, HttpStatus.OK)
//	}
//
//	@GetMapping("/all")
//	fun getAllStudents(): ResponseEntity<List<Student>> {
//		val students = this.studentService.findAllStudents()
//		return ResponseEntity(students, HttpStatus.OK)
//	}
//
//	@PostMapping("/add")
//	fun addStudent(@RequestBody student: Student): ResponseEntity<Student> {
//		val newStudent = this.studentService.addStudent(student)
//		return ResponseEntity(newStudent, HttpStatus.CREATED)
//	}
//
//	@PutMapping("/update")
//	fun updateStudent(@RequestBody student: Student): ResponseEntity<Student> {
//		val studentUpdate = this.studentService.updateStudent(student)
//		return ResponseEntity(studentUpdate, HttpStatus.OK)
//	}
//
//	@DeleteMapping("/delete/{id}")
//	fun deleteStudent(@PathVariable id: Long): ResponseEntity<Void> {
//		val student = this.studentService.deleteStudent(id)
//		return ResponseEntity(student, HttpStatus.OK)
//	}
//}