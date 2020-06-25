package com.khair.taskmanagerapp.data.repository

import android.content.Context
import com.khair.taskmanagerapp.data.dto.TaskNet
import com.khair.taskmanagerapp.data.mapper.TaskDomainMapper
import com.khair.taskmanagerapp.data.util.TaskObservableOnSubscribe
import com.khair.taskmanagerapp.domain.model.Task
import com.khair.taskmanagerapp.domain.repository.TasksRepository
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class TasksRepositoryImpl(context: Context): TasksRepository {

    val assetFileName = "tasks.json"
    val assets = context.assets
    val taskNets: MutableList<TaskNet> = ArrayList()
    val taskDomainMapper = TaskDomainMapper()

    override fun fetchTasks(): Flowable<List<Task>> {
        return Observable.create(
            TaskObservableOnSubscribe(
                assetFileName,
                assets
            )
        )
            .flatMap { Observable.fromIterable(it).subscribeOn(Schedulers.io()) }
            .filter { it.id != null && it.id != -1L }
            .filter {
                it.dateStart != null && it.dateFinish != null
                        && it.dateFinish.isNotEmpty() && it.dateStart.isNotEmpty()}
            .map { taskDomainMapper.map(it) }
            .toList()
            .toFlowable()
    }

    override fun fetchTask(id: Long): Flowable<Task> {
        return Observable.create(
            TaskObservableOnSubscribe(
                assetFileName,
                assets
            )
        )
            .flatMap { Observable.fromIterable(it).subscribeOn(Schedulers.io()) }
            .filter { task -> task.id != null && task.id != -1L }
            .filter { task -> task.id == id }
            .filter {
                it.dateStart != null && it.dateFinish != null
                        && it.dateFinish.isNotEmpty() && it.dateStart.isNotEmpty()}
            .firstOrError()
            .map { taskDomainMapper.map(it) }
            .toFlowable()
    }
}