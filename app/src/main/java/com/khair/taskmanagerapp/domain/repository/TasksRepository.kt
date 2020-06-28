package com.khair.taskmanagerapp.domain.repository

import com.khair.taskmanagerapp.domain.dto.TaskCreation
import com.khair.taskmanagerapp.domain.model.Task
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface TasksRepository {

    fun fetchTasks(): Flowable<List<Task>>
    fun fetchTask(id: Long): Flowable<Task>
    fun addTask(task: TaskCreation): Completable
}