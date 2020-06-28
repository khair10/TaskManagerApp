package com.khair.taskmanagerapp.presentation.mapper

import com.khair.taskmanagerapp.domain.dto.TaskCreation
import com.khair.taskmanagerapp.domain.mapper.Mapper
import com.khair.taskmanagerapp.domain.model.Task
import com.khair.taskmanagerapp.presentation.dto.TaskCreationDto

class TaskDomainMapper: Mapper<TaskCreationDto, TaskCreation>{

    override fun map(item: TaskCreationDto): TaskCreation {
        val startTime = item.dateTimestamp!! + item.timeIndex!! * 3600
        val finishTime = startTime + 3600
        return TaskCreation(
            startTime,
            finishTime,
            item.name!!,
            item.description!!
        )
    }
}