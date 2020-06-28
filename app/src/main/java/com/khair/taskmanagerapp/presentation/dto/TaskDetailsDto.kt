package com.khair.taskmanagerapp.presentation.dto

import java.text.SimpleDateFormat
import java.util.*

data class TaskDetailsDto (
    val id: Long,
    val date: String,
    val name: String,
    val description: String
)
