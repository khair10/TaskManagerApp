package com.khair.taskmanagerapp.presentation.ui.calendar.util

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.khair.taskmanagerapp.R
import com.khair.taskmanagerapp.presentation.dto.TaskItemDto
import com.khair.taskmanagerapp.presentation.util.DateFormatter

class TaskViewHolder(
    view: View,
    private var onItemClickListener: (Long) -> Unit
) : RecyclerView.ViewHolder(view) {

    private val tvTime = view.findViewById<TextView>(R.id.tv_time)
    private val tvTitle = view.findViewById<TextView>(R.id.tv_title)
    private val dateFormatter = DateFormatter()

    fun bind(taskDto: TaskItemDto?, time: Long){
        taskDto?.let {
            tvTime.text = it.date
            tvTitle.text = it.title
            itemView.setOnClickListener {
                onItemClickListener(taskDto.id)
            }
        } ?: kotlin.run {
            tvTime.text = dateFormatter.timeRangeFormat(time)
            tvTitle.text = ""
        }
    }
}