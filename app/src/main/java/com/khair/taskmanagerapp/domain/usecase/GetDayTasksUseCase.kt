package com.khair.taskmanagerapp.domain.usecase

import com.khair.taskmanagerapp.dayInSec
import com.khair.taskmanagerapp.domain.model.Task
import com.khair.taskmanagerapp.domain.repository.TasksRepository
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers

open class GetDayTasksUseCase(val tasksRepository: TasksRepository): GetTasksUseCase{

    override fun execute(dayStartTimestamp: Long): Flowable<List<Task>> {
        return tasksRepository.fetchTasks()
            .flatMap { Flowable.fromIterable(it)
                .filter { it.dateStart >= dayStartTimestamp && it.dateFinish <= dayStartTimestamp + dayInSec }
                .toList()
                .toFlowable()
            }
    }
}