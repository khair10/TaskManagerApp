package com.khair.taskmanagerapp.presentation.ui.taskdetails

import com.khair.taskmanagerapp.presentation.dto.TaskDetailsDto

interface TaskDetailsContract {

    interface View{

        fun showLoading()
        fun hideLoading()
        fun showTaskDetails(task: TaskDetailsDto)
        fun showError(errorMessage: String)
    }

    interface Presenter{
        fun getTaskDetails(id: Long)
        fun clear()
    }
}