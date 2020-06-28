package com.khair.taskmanagerapp.presentation.ui.calendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.khair.taskmanagerapp.R
import com.khair.taskmanagerapp.data.repository.TasksRepositoryImpl
import com.khair.taskmanagerapp.dayInMillis
import com.khair.taskmanagerapp.dayInSec
import com.khair.taskmanagerapp.domain.usecase.GetDayTasksUseCase
import com.khair.taskmanagerapp.presentation.dto.TaskItemDto
import com.khair.taskmanagerapp.presentation.mapper.TaskItemMapper
import com.khair.taskmanagerapp.presentation.ui.calendar.util.TasksAdapter
import com.khair.taskmanagerapp.presentation.ui.taskcreation.TaskCreationActivity
import com.khair.taskmanagerapp.presentation.ui.taskdetails.TaskDetailsActivity
import com.khair.taskmanagerapp.presentation.util.SchedulerProvider
import java.util.*

class CalendarActivity : AppCompatActivity(), CalendarContract.View {

    private lateinit var cvCalendar: CalendarView
    private lateinit var rvTasks: RecyclerView
    private lateinit var pbLoading: ProgressBar
    private lateinit var fabCreate: FloatingActionButton
    private lateinit var tasksAdapter: TasksAdapter
    private lateinit var presenter: CalendarContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        initViews()
        initListeners()
        val calendar = Calendar.getInstance()
        val time = calendar.timeInMillis - calendar.timeInMillis % (dayInMillis) - calendar.timeZone.rawOffset
        val repository = TasksRepositoryImpl(this)
        presenter = CalendarPresenter(
            this,
            TaskItemMapper(),
            SchedulerProvider(),
            GetDayTasksUseCase(repository)
        )
        presenter.getTasks(time / 1000)
    }

    private fun initViews() {
        cvCalendar = findViewById(R.id.cv_calendar)
        rvTasks = findViewById(R.id.rv_tasks)
        pbLoading = findViewById(R.id.pb_loading)
        fabCreate = findViewById(R.id.fab_create)
        rvTasks.apply {
            isNestedScrollingEnabled = false
            tasksAdapter =
                TasksAdapter { taskId ->
                    presenter.handleTaskClick(taskId)
                }
            adapter = tasksAdapter
            layoutManager = LinearLayoutManager(this@CalendarActivity)
        }
    }

    private fun initListeners() {
        cvCalendar.setOnDayClickListener(object: OnDayClickListener{
            override fun onDayClick(eventDay: EventDay) {
                val tempTime = eventDay.calendar.timeInMillis
                val timeInSec = tempTime - tempTime % (dayInMillis) - eventDay.calendar.timeZone.rawOffset
                presenter.getTasks(timeInSec / 1000 + dayInSec)
            }
        })
        fabCreate.setOnClickListener { presenter.handleTaskCreateClick() }
    }

    override fun showLoading() {
        rvTasks.visibility = View.GONE
        pbLoading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        rvTasks.visibility = View.VISIBLE
        pbLoading.visibility = View.GONE
    }

    override fun showTasks(taskDtos: List<TaskItemDto>, timeInSec: Long) {
        tasksAdapter.timeInSec = timeInSec
        tasksAdapter.taskDtos = taskDtos
    }

    override fun showError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun showTaskDetails(id: Long) {
        TaskDetailsActivity.start(this, id)
    }

    override fun showTaskCreation() {
        TaskCreationActivity.start(this)
    }

    override fun onDestroy() {
        presenter.clear()
        super.onDestroy()
    }
}