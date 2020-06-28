package com.khair.taskmanagerapp.presentation.ui.taskdetails

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.khair.taskmanagerapp.R
import com.khair.taskmanagerapp.data.repository.TasksRepositoryImpl
import com.khair.taskmanagerapp.domain.usecase.GetTaskDetailsUseCase
import com.khair.taskmanagerapp.presentation.dto.TaskDetailsDto
import com.khair.taskmanagerapp.presentation.mapper.TaskDetailsMapper
import com.khair.taskmanagerapp.presentation.util.SchedulerProvider

class TaskDetailsActivity : AppCompatActivity(), TaskDetailsContract.View {

    companion object{
        const val ID_KEY = "com.khair.taskmanagerapp.ui.taskdetails.ID_KEY"
        fun start(context: Context, id: Long){
            val intent = Intent(context, TaskDetailsActivity::class.java).apply {
                putExtra(ID_KEY, id)
            }
            context.startActivity(intent)
        }
    }

    private lateinit var tvTitle: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvDescription: TextView
    private lateinit var pbLoading: ProgressBar
    private lateinit var presenter: TaskDetailsContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_details)

        initViews()
        val repository = TasksRepositoryImpl(this)
        presenter = TaskDetailsPresenter(
            this,
            TaskDetailsMapper(),
            SchedulerProvider(),
            GetTaskDetailsUseCase(repository)
        )
        presenter.getTaskDetails(intent.getLongExtra(ID_KEY, -1))
    }

    private fun initViews() {
        tvTitle = findViewById(R.id.tv_title)
        tvDate = findViewById(R.id.tv_date)
        tvDescription = findViewById(R.id.tv_description)
        pbLoading = findViewById(R.id.pb_loading)
    }

    override fun showLoading() {
        pbLoading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        pbLoading.visibility = View.GONE
    }

    override fun showTaskDetails(task: TaskDetailsDto) {
        tvTitle.text = task.name
        tvDate.text = task.date
        tvDescription.text = task.description
    }

    override fun showError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        presenter.clear()
        super.onDestroy()
    }
}