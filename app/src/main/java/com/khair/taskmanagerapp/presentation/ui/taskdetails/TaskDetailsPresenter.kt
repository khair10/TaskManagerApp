package com.khair.taskmanagerapp.presentation.ui.taskdetails

import com.khair.taskmanagerapp.domain.mapper.Mapper
import com.khair.taskmanagerapp.domain.model.Task
import com.khair.taskmanagerapp.domain.usecase.GetDetailsUseCase
import com.khair.taskmanagerapp.domain.usecase.GetTaskDetailsUseCase
import com.khair.taskmanagerapp.presentation.dto.TaskDetailsDto
import com.khair.taskmanagerapp.presentation.util.BaseSchedulerProvider
import com.khair.taskmanagerapp.unknownError
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

class TaskDetailsPresenter(
    private val view: TaskDetailsContract.View,
    private val mapper: Mapper<Task, TaskDetailsDto>,
    private val schedulerProvider: BaseSchedulerProvider,
    private val useCase: GetDetailsUseCase
): TaskDetailsContract.Presenter{

    private val destroyDisposable = CompositeDisposable()

    override fun getTaskDetails(id: Long) {
        if (id < 0L) {
            view.showError("Bad 'id'")
            return
        }
        useCase
            .execute(id)
            .map { task -> mapper.map(task) }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .doOnSubscribe { view.showLoading() }
            .doOnTerminate { view.hideLoading() }
            .subscribe (
                { task -> view.showTaskDetails(task) },
                { e -> view.showError(e?.message ?: unknownError) }
            ).disposeWhenDestroy()
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