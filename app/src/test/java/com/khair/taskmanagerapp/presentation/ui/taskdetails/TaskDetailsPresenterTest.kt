package com.khair.taskmanagerapp.presentation.ui.taskdetails

import com.khair.taskmanagerapp.domain.model.Task
import com.khair.taskmanagerapp.domain.usecase.GetDetailsUseCase
import com.khair.taskmanagerapp.presentation.dto.TaskDetailsDto
import com.khair.taskmanagerapp.presentation.mapper.TaskDetailsMapper
import com.khair.taskmanagerapp.presentation.util.TrampolineSchedulerProvider
import com.khair.taskmanagerapp.unknownError
import io.reactivex.rxjava3.core.Flowable
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.Mockito.mock

class TaskDetailsPresenterTest{

    private var view = mock(TaskDetailsContract.View::class.java)
    private var useCase = mock(GetDetailsUseCase::class.java)
    private var schedulerProvider = TrampolineSchedulerProvider()

    private val id = 1L
    private val name = "Task 1"
    private val description = "Description 1"
    private val date = "09:00 - 10:00, 25 июня, 2020"
    private val dateStart = 1593064800L
    private val dateFinish = 1593068400L

    @Test
    fun testTaskGetDetailsShouldShowTaskDetails(){
        val presenter = TaskDetailsPresenter(view, TaskDetailsMapper(), schedulerProvider, useCase)
        given(useCase.execute(id))
            .willReturn(Flowable.just(Task(
                id,
                dateStart,
                dateFinish,
                name,
                description
            )))

        presenter.getTaskDetails(id)

        then(view).should().showTaskDetails(
            TaskDetailsDto(id,
                date,
                name,
                description)
        )
    }

    @Test
    fun testGetTaskDetailsNoSuchElementShouldShowError(){
        val ex = NoSuchElementException()
        val presenter = TaskDetailsPresenter(view, TaskDetailsMapper(), schedulerProvider, useCase)
        given(useCase.execute(id))
            .willReturn(Flowable.error(ex))

        presenter.getTaskDetails(id)

        then(view).should().showError(ex.message ?: unknownError)
    }


    @Test
    fun testGetTaskDetailsWithBadIdShouldShowError(){
        val presenter = TaskDetailsPresenter(view, TaskDetailsMapper(), schedulerProvider, useCase)

        presenter.getTaskDetails(0)

        then(view).should().showError("Bad 'id'")
    }
}