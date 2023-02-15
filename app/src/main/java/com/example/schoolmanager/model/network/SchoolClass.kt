package com.example.schoolmanager.model.network

data class SchoolClass(
    val schoolName: String,
    val className: String,
    val subject: String,
    val classBackground: String,
) {
    constructor() : this(
        "", "", "", ""
    )
}
