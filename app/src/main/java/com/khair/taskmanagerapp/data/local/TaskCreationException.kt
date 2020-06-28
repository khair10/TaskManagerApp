package com.khair.taskmanagerapp.data.local

class TaskCreationException(): Exception() {

    override val message: String?
        get() = "Task creation error"
}
