package com.khair.taskmanagerapp.data.remote

import com.khair.taskmanagerapp.data.dto.TaskNet
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.*

interface TasksService {

    @GET("{id}/tasks.json")
    fun fetchTasks(@Path("id") id: String): Flowable<List<TaskNet>>

    @GET("{id}/tasks.json")
    fun fetchTask(@Path("id") id: String, @Query("orderBy") equalTo: Long,
        @Query("orderBy") orderBy: String = "id"): Flowable<TaskNet>

    @POST("{id}/tasks.json")
    fun addTask(@Path("id") id: String, @Body taskNet: TaskNet): Completable
}