package com.khair.taskmanagerapp.data.mapper

import com.khair.taskmanagerapp.data.dto.TaskNet
import com.khair.taskmanagerapp.domain.dto.TaskCreation
import com.khair.taskmanagerapp.domain.mapper.Mapper
import com.khair.taskmanagerapp.domain.model.Task

class TaskNetMapper: Mapper<TaskCreation, TaskNet> {

    override fun map(item: TaskCreation): TaskNet {
        return TaskNet(
            dateStart = item.dateStart.toString(),
            dateFinish = item.dateFinish.toString(),
            name = item.name,
            description = item.description
        )
    }
}