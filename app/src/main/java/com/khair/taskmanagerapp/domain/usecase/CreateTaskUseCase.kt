package com.khair.taskmanagerapp.domain.usecase

import com.khair.taskmanagerapp.domain.dto.TaskCreation
import com.khair.taskmanagerapp.domain.model.Task
import com.khair.taskmanagerapp.domain.repository.TasksRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import java.lang.IllegalArgumentException

open class CreateTaskUseCase(val tasksRepository: TasksRepository): CreateUseCase{

    override fun execute(task: TaskCreation): Completable {
        return tasksRepository.addTask(task)
    }
}