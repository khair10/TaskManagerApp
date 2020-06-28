package com.khair.taskmanagerapp.presentation.util

import io.reactivex.rxjava3.core.Scheduler

interface BaseSchedulerProvider {
    fun io(): Scheduler
    fun computation(): Scheduler
    fun ui(): Scheduler
}