package com.khair.taskmanagerapp.data.repository

import android.content.Context
import com.khair.taskmanagerapp.data.dto.TaskNet
import com.khair.taskmanagerapp.data.local.SingleLocalWriter
import com.khair.taskmanagerapp.data.mapper.TaskDomainMapper
import com.khair.taskmanagerapp.data.mapper.TaskNetMapper
import com.khair.taskmanagerapp.data.util.SingleTaskObservableOnSubscribe
import com.khair.taskmanagerapp.data.util.TasksObservableOnSubscribe
import com.khair.taskmanagerapp.domain.dto.TaskCreation
import com.khair.taskmanagerapp.domain.model.Task
import com.khair.taskmanagerapp.domain.repository.TasksRepository
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class TasksRepositoryImpl(context: Context): TasksRepository {

    val taskDomainMapper = TaskDomainMapper()
    val taskNetMapper = TaskNetMapper()

    override fun fetchTasks(): Flowable<List<Task>> {
        return Observable.create(
            TasksObservableOnSubscribe(TaskNet::class.java)
        )
            .flatMap { Observable.fromIterable(it).subscribeOn(Schedulers.io()) }
            .filter { it.id != null && it.id != -1L }
            .map { taskDomainMapper.map(it) }
            .toList()
            .toFlowable()
    }

    override fun fetchTask(id: Long): Flowable<Task> {
        return Observable.create(
            SingleTaskObservableOnSubscribe(TaskNet::class.java, id)
        )
            .map { taskDomainMapper.map(it) }
            .toFlowable(BackpressureStrategy.BUFFER)
    }

    override fun addTask(task: TaskCreation): Completable {
        val taskNet = taskNetMapper.map(task)
        return Flowable.just(taskNet)
            .flatMapCompletable ( SingleLocalWriter(TaskNet::class.java) )
    }
}