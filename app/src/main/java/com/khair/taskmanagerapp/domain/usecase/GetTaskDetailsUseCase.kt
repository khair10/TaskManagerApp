package com.khair.taskmanagerapp.domain.usecase

import com.khair.taskmanagerapp.domain.model.Task
import com.khair.taskmanagerapp.domain.repository.TasksRepository
import io.reactivex.rxjava3.core.Flowable

open class GetTaskDetailsUseCase(val tasksRepository: TasksRepository): GetDetailsUseCase {

    override fun execute(id: Long): Flowable<Task> {
        return tasksRepository.fetchTask(id)
    }
}