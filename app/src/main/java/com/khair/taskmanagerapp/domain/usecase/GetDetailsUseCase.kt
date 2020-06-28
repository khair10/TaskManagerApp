package com.khair.taskmanagerapp.domain.usecase

import com.khair.taskmanagerapp.domain.model.Task
import io.reactivex.rxjava3.core.Flowable

interface GetDetailsUseCase {

    fun execute(dayStartTimestamp: Long): Flowable<Task>
}