package com.khair.taskmanagerapp.data.dto

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

open class TaskNet(
    @PrimaryKey
    @SerializedName("id")
    var id: Long? = null,
    @SerializedName("date_start")
    var dateStart: String? = null,
    @SerializedName("date_finish")
    var dateFinish: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("description")
    var description: String? = null
) : RealmObject()