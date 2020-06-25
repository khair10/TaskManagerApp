package com.khair.taskmanagerapp.presentation.mapper

import com.khair.taskmanagerapp.domain.mapper.Mapper
import com.khair.taskmanagerapp.domain.model.Task
import com.khair.taskmanagerapp.presentation.dto.TaskDetailsDto
import com.khair.taskmanagerapp.presentation.util.DateFormatter

class TaskDetailsMapper: Mapper<Task, TaskDetailsDto> {

    private val dateFormatter = DateFormatter()

    override fun map(task: Task): TaskDetailsDto {
        val date = dateFormatter.dateFormat(task.dateStart)
        return TaskDetailsDto(
            task.id,
            date,
            task.name,
            task.description
        )
    }
}