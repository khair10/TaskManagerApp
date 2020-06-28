package com.khair.taskmanagerapp.domain.usecase

import com.khair.taskmanagerapp.domain.dto.TaskCreation
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface CreateUseCase {

    fun execute(task: TaskCreation): Completable
}