package com.khair.taskmanagerapp.domain.usecase

import com.khair.taskmanagerapp.domain.model.Task
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface GetTasksUseCase {

    fun execute(dayStartTimestamp: Long): Flowable<List<Task>>
}