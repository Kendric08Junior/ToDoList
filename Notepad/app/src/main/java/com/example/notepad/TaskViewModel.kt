package com.example.notepad

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate

class TaskViewModel(private val userId: String) : ViewModel() {
    var taskitems = MutableLiveData<MutableList<Taskitem>>()

    init {
        taskitems.value = mutableListOf()
        loadTasksFromFirebase()
    }

    fun addTaskItem(newTask: Taskitem) {
        val list = taskitems.value ?: mutableListOf()
        list.add(newTask)
        taskitems.postValue(list)

        val dbRef = FirebaseDatabase.getInstance().getReference("tasks").child(userId)
        dbRef.child(newTask.id).setValue(newTask)
    }

    fun updateTaskItem(id: String, name: String, description: String, dueTime: String?) {
        val list = taskitems.value
        val task = list!!.find { it.id == id }!!
        task.name = name
        task.description = description
        task.dueTime = dueTime
        taskitems.postValue(list)

        val dbRef = FirebaseDatabase.getInstance().getReference("tasks").child(userId)
        dbRef.child(id).setValue(task)
    }

    fun setCompleted(taskItem: Taskitem) {
        val list = taskitems.value
        val task = list!!.find { it.id == taskItem.id }!!
        if (task.completedDate == null)
            task.completedDate = LocalDate.now().toString()
        taskitems.postValue(list)

        val dbRef = FirebaseDatabase.getInstance().getReference("tasks").child(userId)
        dbRef.child(task.id).setValue(task)
    }

    fun loadTasksFromFirebase() {
        val dbRef = FirebaseDatabase.getInstance().getReference("tasks").child(userId)
        dbRef.get().addOnSuccessListener { snapshot ->
            val loadedTasks = mutableListOf<Taskitem>()
            for (taskSnap in snapshot.children) {
                val task = taskSnap.getValue(Taskitem::class.java)
                if (task != null) {
                    loadedTasks.add(task)
                }
            }
            taskitems.postValue(loadedTasks)
        }
    }
}