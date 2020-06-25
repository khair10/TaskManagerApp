package com.khair.taskmanagerapp.domain.usecase

import com.khair.taskmanagerapp.domain.model.Task
import com.khair.taskmanagerapp.domain.repository.TasksRepository
import io.reactivex.rxjava3.core.Flowable

class GetTaskDetailsUseCase(val tasksRepository: TasksRepository, val id: Long): UseCase<Task> {

    override fun execute(): Flowable<Task> {
        return tasksRepository.fetchTask(id)
    }
}