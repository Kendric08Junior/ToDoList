package com.example.notepad

interface TaskItemClickListener
{
    fun editTaskItem(taskItem: Taskitem)
    fun completeTaskItem(taskItem: Taskitem)
}