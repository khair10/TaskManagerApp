package com.khair.taskmanagerapp.presentation.ui.taskcreation

import com.khair.taskmanagerapp.domain.dto.TaskCreation
import com.khair.taskmanagerapp.domain.mapper.Mapper
import com.khair.taskmanagerapp.domain.model.Task
import com.khair.taskmanagerapp.domain.repository.TasksRepository
import com.khair.taskmanagerapp.domain.usecase.CreateTaskUseCase
import com.khair.taskmanagerapp.domain.usecase.CreateUseCase
import com.khair.taskmanagerapp.presentation.dto.TaskCreationDto
import com.khair.taskmanagerapp.presentation.dto.TaskDetailsDto
import com.khair.taskmanagerapp.presentation.dto.TaskItemDto
import com.khair.taskmanagerapp.presentation.util.BaseSchedulerProvider
import com.khair.taskmanagerapp.unknownError
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class TaskCreationPresenter(
    private val view: TaskCreationContract.View,
    private val mapper: Mapper<TaskCreationDto, TaskCreation>,
    private val schedulerProvider: BaseSchedulerProvider,
    private val useCase: CreateUseCase
): TaskCreationContract.Presenter {

    private val destroyDisposable = CompositeDisposable()

    override fun createTask(taskDto: TaskCreationDto) {
        if(taskDto.isFullFilled()) {
            val task = mapper.map(taskDto)
            useCase
                .execute(task)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { view.showLoading() }
                .doOnTerminate { view.hideLoading() }
                .subscribe(
                    { view.finish() },
                    { view.showError(it?.message ?: unknownError) }
                )
        } else {
            val message: String = when {
                !(taskDto.name != null && taskDto.name.isNotBlank()) -> {
                    "Назввание не может быть пустым"
                }
                !(taskDto.description != null && taskDto.description.isNotBlank()) -> {
                    "Описание не может быть пустым"
                }
                !(taskDto.timeIndex != null && taskDto.timeIndex >= 0 && taskDto.timeIndex <= 23) -> {
                    "Время должно быть выбрано"
                }
                !(taskDto.dateTimestamp != null) -> {
                    "Дата должна быть выбрана"
                }
                else -> return
            }
            view.showError(message)
        }
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