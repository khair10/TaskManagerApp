package com.khair.taskmanagerapp.domain.usecase

import io.reactivex.rxjava3.core.Flowable

interface UseCase<T> {

    fun execute(): Flowable<T>
}