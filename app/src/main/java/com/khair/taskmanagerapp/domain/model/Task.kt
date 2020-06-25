package com.khair.taskmanagerapp.domain.model

import com.google.gson.annotations.SerializedName

data class Task (
    val id: Long,
    val dateStart: Long,
    val dateFinish: Long,
    val name: String,
    val description: String
)