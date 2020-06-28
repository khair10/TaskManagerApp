package com.khair.taskmanagerapp.presentation.ui.taskcreation

import com.khair.taskmanagerapp.domain.dto.TaskCreation
import com.khair.taskmanagerapp.domain.usecase.CreateUseCase
import com.khair.taskmanagerapp.presentation.dto.TaskCreationDto
import com.khair.taskmanagerapp.presentation.mapper.TaskDomainMapper
import com.khair.taskmanagerapp.presentation.util.TrampolineSchedulerProvider
import com.khair.taskmanagerapp.unknownError
import io.reactivex.rxjava3.core.Completable
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.Mockito.mock

class TaskCreationPresenterTest {

    private var view = mock(TaskCreationContract.View::class.java)
    private var useCase = mock(CreateUseCase::class.java)
    private var schedulerProvider = TrampolineSchedulerProvider()

    private val name = "Task 1"
    private val description = "Description 1"
    private val dateStart = 1593064800L
    private val dateFinish = 1593068400L
    private val timeIndex = 9
    private val dateTimestamp = 1593032400L

    private val nameErrorMessage = "Назввание не может быть пустым"
    private val descriptionErrorMessage = "Описание не может быть пустым"
    private val timeIndexErrorMessage = "Время должно быть выбрано"
    private val dateTimestampErrorMessage = "Дата должна быть выбрана"

    @Test
    fun testCreateTaskShouldFinish(){
        val presenter = TaskCreationPresenter(view, TaskDomainMapper(), schedulerProvider, useCase)
        given(useCase.execute(TaskCreation(
            dateStart,
            dateFinish,
            name,
            description
        )))
            .willReturn(
                Completable.complete()
            )

        presenter.createTask(TaskCreationDto(
            name,
            description,
            timeIndex,
            dateTimestamp
        ))

        then(view).should().finish()
    }

    @Test
    fun testCreateTaskShouldShowError(){
        val ex = NoSuchElementException()
        val presenter = TaskCreationPresenter(view, TaskDomainMapper(), schedulerProvider, useCase)
        given(useCase.execute(
            TaskCreation(
                dateStart,
                dateFinish,
                name,
                description
        )
        ))
            .willReturn(Completable.error(ex))

        presenter.createTask(TaskCreationDto(
            name,
            description,
            timeIndex,
            dateTimestamp
        ))

        then(view).should().showError(ex.message ?: unknownError)
    }

    @Test
    fun testCreateTaskWithEmptyNameShouldShowError(){
        val presenter = TaskCreationPresenter(view, TaskDomainMapper(), schedulerProvider, useCase)

        presenter.createTask(TaskCreationDto(
            "",
            description,
            timeIndex,
            dateTimestamp
        ))

        then(view).should().showError(nameErrorMessage)
    }

    @Test
    fun testCreateTaskWithNullNameShouldShowError(){
        val presenter = TaskCreationPresenter(view, TaskDomainMapper(), schedulerProvider, useCase)

        presenter.createTask(TaskCreationDto(
            null,
            description,
            timeIndex,
            dateTimestamp
        ))

        then(view).should().showError(nameErrorMessage)
    }

    @Test
    fun testCreateTaskWithEmptyDescriptionShouldShowError(){
        val presenter = TaskCreationPresenter(view, TaskDomainMapper(), schedulerProvider, useCase)

        presenter.createTask(TaskCreationDto(
            name,
            "",
            timeIndex,
            dateTimestamp
        ))

        then(view).should().showError(descriptionErrorMessage)
    }

    @Test
    fun testCreateTaskWithNullDescriptionShouldShowError(){
        val presenter = TaskCreationPresenter(view, TaskDomainMapper(), schedulerProvider, useCase)

        presenter.createTask(TaskCreationDto(
            name,
            null,
            timeIndex,
            dateTimestamp
        ))

        then(view).should().showError(descriptionErrorMessage)
    }

    @Test
    fun testCreateTaskWithLowerTimeIndexShouldShowError(){
        val presenter = TaskCreationPresenter(view, TaskDomainMapper(), schedulerProvider, useCase)

        presenter.createTask(TaskCreationDto(
            name,
            description,
            -1,
            dateTimestamp
        ))

        then(view).should().showError(timeIndexErrorMessage)
    }

    @Test
    fun testCreateTaskWithHigherTimeIndexShouldShowError(){
        val presenter = TaskCreationPresenter(view, TaskDomainMapper(), schedulerProvider, useCase)

        presenter.createTask(TaskCreationDto(
            name,
            description,
            24,
            dateTimestamp
        ))

        then(view).should().showError(timeIndexErrorMessage)
    }

    @Test
    fun testCreateTaskWithNullTimeIndexShouldShowError(){
        val presenter = TaskCreationPresenter(view, TaskDomainMapper(), schedulerProvider, useCase)

        presenter.createTask(TaskCreationDto(
            name,
            description,
            null,
            dateTimestamp
        ))

        then(view).should().showError(timeIndexErrorMessage)
    }

    @Test
    fun testCreateTaskWithNullDateTimestampShouldShowError(){
        val presenter = TaskCreationPresenter(view, TaskDomainMapper(), schedulerProvider, useCase)

        presenter.createTask(TaskCreationDto(
            name,
            description,
            9,
            null
        ))

        then(view).should().showError(dateTimestampErrorMessage)
    }
}