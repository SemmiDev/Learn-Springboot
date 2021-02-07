package com.sammidev.model.web

data class WebResponse<T>(
        val code: Int,
        val status: String,
        val data: T
)