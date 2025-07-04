package com.example.notepad

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.notepad.databinding.FragmentNewTaskSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class NewTaskSheet(private var taskItem: Taskitem?) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNewTaskSheetBinding
    private lateinit var taskViewModel: TaskViewModel
    private var dueTime: LocalTime? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNewTaskSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()

        taskViewModel = ViewModelProvider(activity)[TaskViewModel::class.java]

        if (taskItem != null) {
            binding.taskTitle.text = "Edit Task"
            val editable = Editable.Factory.getInstance()
            binding.name.text = editable.newEditable(taskItem!!.name)
            binding.desc.text = editable.newEditable(taskItem!!.description)
            taskItem!!.dueTime?.let {
                dueTime = LocalTime.parse(it, DateTimeFormatter.ofPattern("HH:mm"))
                updateTimeButtonText()
            }
        } else {
            binding.taskTitle.text = "New Task"
        }

        binding.saveButton.setOnClickListener {
            saveAction()
        }

        binding.timePickerButton.setOnClickListener {
            openTimePicker()
        }
    }

    private fun openTimePicker() {
        if (dueTime == null) dueTime = LocalTime.now()
        val listener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            dueTime = LocalTime.of(hour, minute)
            updateTimeButtonText()
        }
        TimePickerDialog(activity, listener, dueTime!!.hour, dueTime!!.minute, true).show()
    }

    private fun updateTimeButtonText() {
        binding.timePickerButton.text = dueTime!!.format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    private fun saveAction() {
        val name = binding.name.text.toString()
        val desc = binding.desc.text.toString()
        val timeStr = dueTime?.format(DateTimeFormatter.ofPattern("HH:mm"))

        if (taskItem == null) {
            val newTask = Taskitem(name = name, description = desc, dueTime = timeStr)
            taskViewModel.addTaskItem(newTask)
        } else {
            taskViewModel.updateTaskItem(taskItem!!.id, name, desc, timeStr)
        }

        binding.name.setText("")
        binding.desc.setText("")
        dismiss()
    }
}