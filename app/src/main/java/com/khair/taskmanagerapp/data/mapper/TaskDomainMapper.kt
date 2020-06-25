package com.khair.taskmanagerapp.data.mapper

import com.khair.taskmanagerapp.data.dto.TaskNet
import com.khair.taskmanagerapp.domain.mapper.Mapper
import com.khair.taskmanagerapp.domain.model.Task

class TaskDomainMapper: Mapper<TaskNet, Task> {

    override fun map(taskNet: TaskNet): Task {
        val task: Task
        taskNet.run {
            task = Task(
                id!!,
                dateStart!!.toLong(),
                dateFinish!!.toLong(),
                name ?: "Empty name",
                description ?: "Empty description"
            )
        }
        return task
    }
}