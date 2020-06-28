package com.khair.taskmanagerapp.data.remote

object ApiFactory {

    val tasksService: TasksService by lazy {
        RetrofitHelper.retrofit.create(TasksService::class.java)
    }
}