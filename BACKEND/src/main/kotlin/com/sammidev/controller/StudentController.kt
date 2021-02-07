package com.sammidev.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.sammidev.entity.Student
import com.sammidev.model.service.CreateStudentRequest
import com.sammidev.model.service.StudentResponse
import com.sammidev.model.service.UpdateStudentRequestById
import com.sammidev.model.web.WebResponse
import com.sammidev.service.StudentService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.jms.core.JmsTemplate
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/student")
class StudentController(val studentService: StudentService,
                        val jmsTemplate: JmsTemplate) {

    @Value("\${product.jms.destination}")
    private val jmsQueue: String? = null

    @PostMapping(
            value = ["/add"],
            produces = ["application/json"],
            consumes = ["application/json"]
    )
    fun createStudent(@RequestBody body: CreateStudentRequest): WebResponse<StudentResponse> {
        val productResponse = studentService.addStudent(body)
        return WebResponse(
                code = 200,
                status = "OK",
                data = productResponse
        )
    }

    @GetMapping(
            value = ["/all"],
            produces = ["application/json"]
    )
    fun findAllStudent(): WebResponse<MutableList<StudentResponse>> {
        return WebResponse(
                code = 200,
                status = "OK",
                data = studentService.findAllStudents()
        )
    }

    @GetMapping(
            value = ["/find/{id}"],
            produces = ["application/json"]
    )
    fun findStudentById(@PathVariable(value = "id") id: Long): WebResponse<Student?> {
        return WebResponse(
                code = 200,
                status = "OK",
                data = studentService.findStudentById(id)
        )
    }

    @DeleteMapping(value = ["/delete/{id}"])
    fun deleteStudentById(@PathVariable(value = "id") id: Long): WebResponse<Long> {
        studentService.deleteStudentById(id)
        return WebResponse(
                code = 200,
                status = "OK",
                data = id
        )
    }

    @PutMapping(
            value = ["/update/{id}"],
            produces = ["application/json"],
            consumes = ["application/json"]
    )
    fun updateStudentById(@PathVariable("id") id: Long,
                      @RequestBody request: UpdateStudentRequestById): WebResponse<StudentResponse> {
        val studentResponse = studentService.updateStudentById(id, request)
        return WebResponse(
                code = 200,
                status = "OK",
                data = studentResponse
        )
    }

    @GetMapping(value = ["/sendToBroker/{id}"])
	fun sendToCart(@PathVariable id: Long): ResponseEntity<Student> {
        val student = studentService.findStudentById(id)
        if (student != null) {
            var mapper = ObjectMapper()
            var jsonInString = mapper.writeValueAsString(student);
            jmsTemplate.convertAndSend(jmsQueue!!, jsonInString)
            return ResponseEntity(student, HttpStatus.OK)
        }
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }
}