package com.khair.taskmanagerapp.presentation.ui.calendar.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khair.taskmanagerapp.R
import com.khair.taskmanagerapp.hoursInDay
import com.khair.taskmanagerapp.presentation.dto.TaskItemDto
import java.util.*
import kotlin.collections.ArrayList

class TasksAdapter(private val onItemClickListener: (Long) -> Unit):
    RecyclerView.Adapter<TaskViewHolder>() {

    var timeInSec: Long = 0
    var taskDtos: List<TaskItemDto> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(
            view,
            onItemClickListener
        )
    }

    override fun getItemCount(): Int {
        return hoursInDay
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        var task: TaskItemDto? = null
        val time = timeInSec + (position * 3600)
        for(item in taskDtos){
            if(item.startDate == time){
                task = item
                break
            }
        }
        holder.bind(task, time)
    }
}