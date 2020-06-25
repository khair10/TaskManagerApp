package com.khair.taskmanagerapp.data.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TaskNet(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("date_start")
    val dateStart: String?,
    @SerializedName("date_finish")
    val dateFinish: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("description")
    val description: String?
) : Serializable