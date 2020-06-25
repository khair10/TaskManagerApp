package com.khair.taskmanagerapp.presentation.mapper

import com.khair.taskmanagerapp.domain.mapper.Mapper
import com.khair.taskmanagerapp.domain.model.Task
import com.khair.taskmanagerapp.presentation.dto.TaskItemDto
import com.khair.taskmanagerapp.presentation.util.DateFormatter
import java.text.SimpleDateFormat
import java.util.*

class TaskItemMapper: Mapper<Task, TaskItemDto> {

    private val dateFormatter = DateFormatter()

    override fun map(task: Task): TaskItemDto {
        val date = dateFormatter.timeRangeFormat(task.dateStart)
        return TaskItemDto(
            task.id,
            task.dateStart,
            date,
            task.name
        )
    }
}