package com.khair.taskmanagerapp.presentation.ui.taskdetails

import com.khair.taskmanagerapp.domain.mapper.Mapper
import com.khair.taskmanagerapp.domain.model.Task
import com.khair.taskmanagerapp.domain.repository.TasksRepository
import com.khair.taskmanagerapp.domain.usecase.GetTaskDetailsUseCase
import com.khair.taskmanagerapp.domain.usecase.UseCase
import com.khair.taskmanagerapp.presentation.dto.TaskDetailsDto
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class TaskDetailsPresenter(
    private val view: TaskDetailsContract.View,
    private val repository: TasksRepository,
    private val mapper: Mapper<Task, TaskDetailsDto>
): TaskDetailsContract.Presenter{

    private val destroyDisposable = CompositeDisposable()

    override fun getTaskDetails(id: Long) {
        if (id == -1L) {
            view.showError("Bad 'id'")
            return
        }
        val useCase = GetTaskDetailsUseCase(repository, id)
        useCase.execute()
            .map { task -> mapper.map(task) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { view.showLoading() }
            .doOnTerminate { view.hideLoading() }
            .subscribe (
                { task -> view.showTaskDetails(task) },
                { e -> view.showError(e?.message ?: "Undefined exception") }
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