package com.khair.taskmanagerapp.presentation.dto

class TaskCreationDto(
    val name: String? = null,
    val description: String? = null,
    val timeIndex: Int? = null,
    val dateTimestamp: Long? = null
) {

    fun isFullFilled(): Boolean{
        return name != null && name.isNotBlank() &&
                description != null && description.isNotBlank() &&
                timeIndex != null && timeIndex >= 0 && timeIndex <= 23 &&
                dateTimestamp != null
    }
}