package com.khair.taskmanagerapp.presentation.ui.calendar

import com.khair.taskmanagerapp.domain.model.Task
import com.khair.taskmanagerapp.domain.usecase.GetTasksUseCase
import com.khair.taskmanagerapp.presentation.dto.TaskItemDto
import com.khair.taskmanagerapp.presentation.mapper.TaskItemMapper
import com.khair.taskmanagerapp.presentation.util.TrampolineSchedulerProvider
import com.khair.taskmanagerapp.unknownError
import io.reactivex.rxjava3.core.Flowable
import org.junit.Test
import org.mockito.BDDMockito.*

class CalendarPresenterTest {

    private var view = mock(CalendarContract.View::class.java)
    private var useCase = mock(GetTasksUseCase::class.java)
    private var schedulerProvider = TrampolineSchedulerProvider()

    private val id = 1L
    private val name = "Task"
    private val description = "Description"
    private val dateTimestamp = 1593032400L
    private val date = arrayOf(
        "09:00 - 10:00",
        "10:00 - 11:00",
        "11:00 - 12:00"
    )
    private val dateStart = 1593064800L
    private val dateFinish = 1593068400L
    private val tasks = ArrayList<Task>().apply {
        add(Task(id, dateStart, dateFinish, "$name $id", "$description $id"))
        add(Task(id + 1, dateStart + 3600L, dateFinish + 3600L, name + " " + (id + 1), description + " " + (id + 1)))
        add(Task(id + 2, dateStart + 7200L, dateFinish + 7200L, name + " " + (id + 2), description + " " + (id + 2)))
    }
    private val tasksPreview = ArrayList<TaskItemDto>().apply {
        add(TaskItemDto(id, dateStart, date[0], "$name $id"))
        add(TaskItemDto(id + 1, dateStart + 3600L, date[1], "$name ${id + 1}"))
        add(TaskItemDto(id + 2, dateStart + 7200L, date[2], "$name ${id + 2}"))
    }

    @Test
    fun testGetDetailsShouldShowTaskDetails(){
        val presenter = CalendarPresenter(view, TaskItemMapper(), schedulerProvider, useCase)
        given(useCase.execute(dateTimestamp))
            .willReturn(Flowable.just(tasks))

        presenter.getTasks(dateTimestamp)

        then(view).should().showTasks(tasksPreview, dateTimestamp)
    }

    @Test
    fun testGetTaskDetailsNoSuchElementShouldShowError(){
        val ex = NoSuchElementException()
        val presenter = CalendarPresenter(view, TaskItemMapper(), schedulerProvider, useCase)
        given(useCase.execute(dateTimestamp))
            .willReturn(Flowable.error(ex))

        presenter.getTasks(dateTimestamp)

        then(view).should().showError(ex.message ?: unknownError)
    }
}