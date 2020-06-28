package com.khair.taskmanagerapp.presentation.ui.taskcreation

import com.khair.taskmanagerapp.presentation.dto.TaskCreationDto
import com.khair.taskmanagerapp.presentation.dto.TaskItemDto

interface TaskCreationContract {

    interface View{

        fun showLoading()
        fun hideLoading()
        fun showError(errorMessage: String)
        fun finish()
    }

    interface Presenter{

        fun createTask(taskDto: TaskCreationDto)
        fun clear()
    }
}