package com.khair.taskmanagerapp.data.local

class AlreadyHaveTaskException: Exception() {
    override val message: String?
        get() = "Already have task in that time"
}