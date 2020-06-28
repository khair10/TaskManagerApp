package com.khair.taskmanagerapp.domain.dto

data class TaskCreation(
    val dateStart: Long,
    val dateFinish: Long,
    val name: String,
    val description: String
) {
}