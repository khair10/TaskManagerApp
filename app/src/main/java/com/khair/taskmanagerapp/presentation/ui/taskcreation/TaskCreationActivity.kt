package com.khair.taskmanagerapp.presentation.ui.taskcreation

import android.app.Activity
import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ProgressBar
import android.widget.Toast
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.khair.taskmanagerapp.R
import com.khair.taskmanagerapp.data.repository.TasksRepositoryImpl
import com.khair.taskmanagerapp.dayInMillis
import com.khair.taskmanagerapp.dayInSec
import com.khair.taskmanagerapp.domain.usecase.CreateTaskUseCase
import com.khair.taskmanagerapp.presentation.dto.TaskCreationDto
import com.khair.taskmanagerapp.presentation.dto.TaskItemDto
import com.khair.taskmanagerapp.presentation.mapper.TaskDomainMapper
import com.khair.taskmanagerapp.presentation.mapper.TaskItemMapper
import com.khair.taskmanagerapp.presentation.ui.calendar.CalendarContract
import com.khair.taskmanagerapp.presentation.ui.calendar.util.TasksAdapter
import com.khair.taskmanagerapp.presentation.ui.taskdetails.TaskDetailsActivity
import com.khair.taskmanagerapp.presentation.util.SchedulerProvider

class TaskCreationActivity : AppCompatActivity(), TaskCreationContract.View {

    companion object{
        fun start(context: Context){
            val intent = Intent(context, TaskCreationActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var tietName: TextInputEditText
    private lateinit var tietDescription: TextInputEditText
    private lateinit var tietTime : AutoCompleteTextView
    private lateinit var cvDate : CalendarView
    private lateinit var btnCreate : MaterialButton
    private lateinit var pbLoading: ProgressBar
    private lateinit var spinAdapter: ArrayAdapter<String>
    private lateinit var presenter: TaskCreationContract.Presenter
    private var position: Int? = null
    private var timeInSec: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_creation)

        initViews()
        initViewListeners()
        val repository = TasksRepositoryImpl(this)
        presenter = TaskCreationPresenter(
            this,
            TaskDomainMapper(),
            SchedulerProvider(),
            CreateTaskUseCase(repository)
        )
    }

    private fun initViews() {
        tietName = findViewById(R.id.tiet_name)
        tietDescription = findViewById(R.id.tiet_description)
        tietTime = findViewById(R.id.tiet_time)
        cvDate = findViewById(R.id.cv_date)
        btnCreate = findViewById(R.id.btn_create)
        pbLoading = findViewById(R.id.pb_loading)
        spinAdapter = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.time)
        )
        tietTime.setAdapter(spinAdapter)
    }

    private fun initViewListeners() {
        btnCreate.setOnClickListener {
            hideKeyboard()
            presenter.createTask(
                TaskCreationDto(
                    tietName.text.toString(),
                    tietDescription.text.toString(),
                    position,
                    timeInSec
                )
            )
        }
        tietTime.setOnItemClickListener { adapterView, _, position, _ ->
            this.position = position
        }
        cvDate.setOnDayClickListener(object : OnDayClickListener{
            override fun onDayClick(eventDay: EventDay) {
                val tempTime = eventDay.calendar.timeInMillis
                val timeInMillis = tempTime - tempTime % (dayInMillis) - eventDay.calendar.timeZone.rawOffset
                val timeInSec = timeInMillis / 1000 + dayInSec
                Log.d("TaskCreation", timeInSec.toString())
                this@TaskCreationActivity.timeInSec = timeInSec
            }
        })
    }

    override fun showLoading() {
        pbLoading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        pbLoading.visibility = View.GONE
    }

    override fun showError(errorMessage: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Ошибка")
            .setMessage(errorMessage)
            .setCancelable(true)
            .setPositiveButton("Закрыть", null)
            .show()
    }

    private fun hideKeyboard() {
        val view: View? = currentFocus
        view?.let {
            val imm: InputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            view.clearFocus()
        }
    }
}