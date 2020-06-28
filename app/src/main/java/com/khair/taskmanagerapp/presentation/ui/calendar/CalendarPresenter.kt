package com.khair.taskmanagerapp.presentation.ui.calendar

import com.khair.taskmanagerapp.domain.mapper.Mapper
import com.khair.taskmanagerapp.domain.model.Task
import com.khair.taskmanagerapp.domain.usecase.GetDayTasksUseCase
import com.khair.taskmanagerapp.domain.usecase.GetTasksUseCase
import com.khair.taskmanagerapp.presentation.dto.TaskItemDto
import com.khair.taskmanagerapp.presentation.util.BaseSchedulerProvider
import com.khair.taskmanagerapp.unknownError
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

class CalendarPresenter(
    private val view: CalendarContract.View,
    private val mapper: Mapper<Task, TaskItemDto>,
    private val schedulerProvider: BaseSchedulerProvider,
    private val useCase: GetTasksUseCase
): CalendarContract.Presenter{

    private val destroyDisposable = CompositeDisposable()

    override fun getTasks(timeInSec: Long){
        useCase.execute(timeInSec)
            .flatMap { Flowable.fromIterable(it)
                .map { task -> mapper.map(task) }
                .toList()
                .toFlowable()
            }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .doOnSubscribe { view.showLoading() }
            .doOnTerminate { view.hideLoading() }
            .subscribe (
                { tasks -> view.showTasks(tasks, timeInSec) },
                { e -> view.showError(e?.message ?: unknownError) }
            ).disposeWhenDestroy()
    }

    override fun handleTaskClick(id: Long) {
        view.showTaskDetails(id)
    }

    override fun handleTaskCreateClick() {
        view.showTaskCreation()
    }

    override fun clear() {
        if (!destroyDisposable.isDisposed) {
            destroyDisposable.dispose()
        }
    }

    private fun Disposable.disposeWhenDestroy() {
        destroyDisposable.add(this)
    }
}