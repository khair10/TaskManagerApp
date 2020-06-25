package com.khair.taskmanagerapp.domain.usecase

import com.khair.taskmanagerapp.domain.model.Task
import com.khair.taskmanagerapp.domain.repository.TasksRepository
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers

class GetDayTasksUseCase(val tasksRepository: TasksRepository, val dayStartTimestamp: Long): UseCase<List<Task>>{

    val secondsInDay = 86400

    override fun execute(): Flowable<List<Task>> {
        return tasksRepository.fetchTasks()
            .flatMap { Flowable.fromIterable(it)
                .filter { it.dateStart >= dayStartTimestamp && it.dateFinish <= dayStartTimestamp + secondsInDay }
                .toList()
                .toFlowable()
                .subscribeOn(Schedulers.io())
            }
    }
}