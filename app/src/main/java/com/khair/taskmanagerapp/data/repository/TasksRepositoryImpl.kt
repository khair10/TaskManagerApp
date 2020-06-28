package com.khair.taskmanagerapp.data.repository

import android.content.Context
import com.google.firebase.iid.FirebaseInstanceId
import com.khair.taskmanagerapp.data.dto.TaskNet
import com.khair.taskmanagerapp.data.local.*
import com.khair.taskmanagerapp.data.mapper.TaskDomainMapper
import com.khair.taskmanagerapp.data.mapper.TaskNetMapper
import com.khair.taskmanagerapp.data.remote.ApiFactory
import com.khair.taskmanagerapp.data.remote.RetrofitHelper
import com.khair.taskmanagerapp.data.util.SingleTaskObservableOnSubscribe
import com.khair.taskmanagerapp.domain.dto.TaskCreation
import com.khair.taskmanagerapp.domain.model.Task
import com.khair.taskmanagerapp.domain.repository.TasksRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.realm.Realm
import retrofit2.Retrofit

class TasksRepositoryImpl(context: Context): TasksRepository {

    val taskDomainMapper = TaskDomainMapper()
    val taskNetMapper = TaskNetMapper()
    val tasksService = ApiFactory.tasksService

    override fun fetchTasks(): Flowable<List<Task>> {
        return tasksService.fetchTasks(FirebaseInstanceId.getInstance().id)
            .flatMap ( LocalListWriter() )
            .onErrorResumeNext ( LocalListReader() )
            .flatMap { Flowable.fromIterable(it) }
            .filter { it.id != null && it.id != -1L }
            .map { taskDomainMapper.map(it) }
            .toList()
            .toFlowable()
    }

    override fun fetchTask(id: Long): Flowable<Task> {
        return tasksService.fetchTask(FirebaseInstanceId.getInstance().id, id)
            .flatMap ( LocalTaskWriter() )
            .onErrorResumeNext ( LocalTaskReader(id) )
            .map { taskDomainMapper.map(it) }
    }

    override fun addTask(task: TaskCreation): Completable {
        val taskNet = taskNetMapper.map(task)
        var outputElement: TaskNet?
        var realm = Realm.getDefaultInstance()
        var e: Exception? = null
        realm.executeTransaction {
            val maxValue: Number? = it.where(TaskNet::class.java).max("id")
            val pk = if (maxValue != null) maxValue.toLong() + 1 else 0.toLong()
            val taskTemp: TaskNet? =
                it.where(TaskNet::class.java).equalTo("dateStart", taskNet.dateStart)
                    .equalTo("dateFinish", taskNet.dateFinish)
                    .findFirst()
            if (taskTemp != null) {
                e = AlreadyHaveTaskException()
            }else{
                taskNet.id = pk
                outputElement = it.copyToRealm(taskNet)
            }
        }
        if(e != null)
            return Completable.error(e)
        return tasksService.addTask(FirebaseInstanceId.getInstance().id, taskNet)
    }
}