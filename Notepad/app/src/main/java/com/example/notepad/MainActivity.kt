package com.example.notepad

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notepad.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), TaskItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Get or create userId
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        userId = prefs.getString("user_id", null) ?: UUID.randomUUID().toString().also {
            prefs.edit().putString("user_id", it).apply()
        }

        taskViewModel = ViewModelProvider(this, TaskViewModelFactory(userId)).get(TaskViewModel::class.java)

        binding.newTaskButton.setOnClickListener {
            NewTaskSheet(null).show(supportFragmentManager, "newTaskTag")
        }

        setCurrentDate()
        setRecyclerView()

        val calendarIcon = findViewById<ImageView>(R.id.calendarIcon)
        calendarIcon.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay)

                val dayNumber = SimpleDateFormat("dd", Locale.getDefault()).format(selectedCalendar.time)
                val dayName = SimpleDateFormat("EEEE", Locale.getDefault()).format(selectedCalendar.time)
                val monthYear = SimpleDateFormat("MMM, yyyy", Locale.getDefault()).format(selectedCalendar.time)

                findViewById<TextView>(R.id.numberdate).text = dayNumber
                findViewById<TextView>(R.id.weekdays).text = dayName
                findViewById<TextView>(R.id.monthyear).text = monthYear

            }, year, month, day)

            datePicker.show()
        }
    }

    private fun setRecyclerView() {
        val mainActivity = this
        taskViewModel.taskitems.observe(this) { taskList ->
            binding.todoListRecyclerView.apply {
                layoutManager = LinearLayoutManager(applicationContext)
                adapter = TaskItemAdapter(taskList, mainActivity)
            }
        }
    }

    override fun editTaskItem(taskItem: Taskitem) {
        NewTaskSheet(taskItem).show(supportFragmentManager, "newTaskTag")
    }

    override fun completeTaskItem(taskItem: Taskitem) {
        taskViewModel.setCompleted(taskItem)
    }

    private fun setCurrentDate() {
        val calendar = Calendar.getInstance()

        val dayNumber = SimpleDateFormat("dd", Locale.getDefault()).format(calendar.time)
        val dayName = SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.time)
        val monthYear = SimpleDateFormat("MMM, yyyy", Locale.getDefault()).format(calendar.time)

        findViewById<TextView>(R.id.numberdate).text = dayNumber
        findViewById<TextView>(R.id.weekdays).text = dayName
        findViewById<TextView>(R.id.monthyear).text = monthYear
    }
}