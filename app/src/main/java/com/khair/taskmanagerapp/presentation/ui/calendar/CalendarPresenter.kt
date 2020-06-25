package com.khair.taskmanagerapp.presentation.ui.calendar

import com.khair.taskmanagerapp.domain.mapper.Mapper
import com.khair.taskmanagerapp.domain.model.Task
import com.khair.taskmanagerapp.domain.repository.TasksRepository
import com.khair.taskmanagerapp.domain.usecase.GetDayTasksUseCase
import com.khair.taskmanagerapp.domain.usecase.UseCase
import com.khair.taskmanagerapp.presentation.dto.TaskItemDto
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class CalendarPresenter(
    private val view: CalendarContract.View,
    private val repository: TasksRepository,
    private val mapper: Mapper<Task, TaskItemDto>
): CalendarContract.Presenter{

    private val destroyDisposable = CompositeDisposable()

    override fun getTasks(timeInSec: Long){
        val useCase =  GetDayTasksUseCase(repository, timeInSec)
        useCase.execute()
            .flatMap { Flowable.fromIterable(it)
                .map { task -> mapper.map(task) }
                .toList()
                .toFlowable()
                .subscribeOn(Schedulers.io())
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { view.showLoading() }
            .doOnTerminate { view.hideLoading() }
            .subscribe (
                { tasks -> view.showTasks(tasks, timeInSec) },
                { e -> view.showError(e?.message ?: "Undefined exception") }
            ).disposeWhenDestroy()
    }

    override fun handleTaskClick(id: Long) {
        view.showTaskDetails(id)
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