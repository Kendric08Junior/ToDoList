package com.example.notepad

import android.content.Context
import androidx.core.content.ContextCompat


data class Taskitem(
    var id: String = java.util.UUID.randomUUID().toString(),
    var name: String = "",
    var description: String = "",
    var dueTime: String? = null,
    var completedDate: String? = null
)
 {
    fun isCompleted() = completedDate != null
    fun imageResource(): Int = if (isCompleted()) R.drawable.checked_24 else R.drawable.unchecked_24
    fun imageColor(context: Context): Int = if (isCompleted()) purple(context) else black(context)

    private fun purple(context: Context) = ContextCompat.getColor(context, R.color.purple_500)
    private fun black(context: Context) = ContextCompat.getColor(context, R.color.black)
}
