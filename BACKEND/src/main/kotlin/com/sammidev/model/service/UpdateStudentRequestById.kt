package com.sammidev.model.service

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class UpdateStudentRequestById (

        @field:NotBlank(message = "NOT BLANK")
        var name: String,

        @field:NotBlank(message = "NOT BLANK")
        @field:Email(message = "MustValid")
        var email: String,

        @field:NotBlank(message = "NOT BLANK")
        @Pattern(regexp = "[0-9]+", message = "MustValid")
        var phone: String,

        @field:NotBlank(message = "NOT BLANK")
        var major: String,

        @field:NotBlank(message = "NOT BLANK")
        var imageUrl: String
)