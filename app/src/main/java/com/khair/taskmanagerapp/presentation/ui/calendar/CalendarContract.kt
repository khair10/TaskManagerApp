package com.khair.taskmanagerapp.presentation.ui.calendar

import com.khair.taskmanagerapp.presentation.dto.TaskItemDto

interface CalendarContract {

    interface View{

        fun showLoading()
        fun hideLoading()
        fun showTasks(taskDtos: List<TaskItemDto>, timeInSec: Long)
        fun showError(errorMessage: String)
        fun showTaskDetails(id: Long)
    }

    interface Presenter{
        fun getTasks(timeInSec: Long)
        fun handleTaskClick(id: Long)
        fun clear()
    }
}