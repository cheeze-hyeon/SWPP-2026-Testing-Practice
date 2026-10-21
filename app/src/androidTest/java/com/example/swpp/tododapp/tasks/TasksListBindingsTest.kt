package com.example.swpp.tododapp.tasks

import android.view.View
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.swpp.tododapp.data.Task
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class TasksListBindingsTest {

    private fun newTextView(): TextView =
        TextView(ApplicationProvider.getApplicationContext())

    @Test
    fun setDueDateLabel_completedTaskWithDueDateAndWithoutDueDate_showsClear() {
        // GIVEN - A completed task with a due date and one without a due date
        val textView = newTextView()
        val tasks = listOf(
            Task("Task Title", "Task Description", true, dueDate = LocalDate.of(2026, 10, 21)),
            Task("Task Title", "Task Description", true, dueDate = null)
        )

        // WHEN - Bind each task to a clean, hidden label view
        tasks.forEach { task ->
            textView.text = ""
            textView.visibility = View.GONE
            setDueDateLabel(textView, task)

            // THEN - Each completed task displays CLEAR
            assertEquals("CLEAR", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    @Test
    fun setDueDateLabel_activeTaskDueToday_showsDDay() {
        // GIVEN - An active task due on today's captured local date
        val textView = newTextView()
        val today = LocalDate.now()
        val task = Task("Task Title", "Task Description", false, dueDate = today)

        // WHEN - Bind the task immediately after capturing today's date
        setDueDateLabel(textView, task)

        // THEN - The label displays D-Day
        assertEquals("D-Day", textView.text.toString())
        assertEquals(View.VISIBLE, textView.visibility)
    }

    @Test
    fun setDueDateLabel_nullTask_clearsAndHidesLabel() {
        // GIVEN - A recycled label view displaying CLEAR
        val textView = newTextView().apply {
            text = "CLEAR"
            visibility = View.VISIBLE
        }

        // WHEN - Bind a null task
        setDueDateLabel(textView, null)

        // THEN - The old text is removed and the view is hidden
        assertEquals("", textView.text.toString())
        assertEquals(View.GONE, textView.visibility)
    }

    @Test
    fun setDueDateLabel_activeTaskWithoutDueDate_clearsAndHidesRecycledLabel() {
        // GIVEN - A recycled label view displaying CLEAR and an active task without a due date
        val textView = newTextView().apply {
            text = "CLEAR"
            visibility = View.VISIBLE
        }
        val task = Task("Task Title", "Task Description", false, dueDate = null)

        // WHEN - Bind the active task
        setDueDateLabel(textView, task)

        // THEN - The old text is removed and the view is hidden
        assertEquals("", textView.text.toString())
        assertEquals(View.GONE, textView.visibility)
    }

    @Test
    fun setDueDateLabel_completedThenActiveTaskWithoutDueDate_clearsStaleClear() {
        // GIVEN - A completed task without a due date and a recycled label view
        val textView = newTextView()
        val completedTask = Task("Task Title", "Task Description", true, dueDate = null)

        // WHEN - Bind the completed task, then bind the same task as active
        setDueDateLabel(textView, completedTask)
        val completedText = textView.text.toString()
        val completedVisibility = textView.visibility
        setDueDateLabel(textView, completedTask.copy(isCompleted = false))

        // THEN - CLEAR is shown first, then removed from the recycled view
        assertEquals("CLEAR", completedText)
        assertEquals(View.VISIBLE, completedVisibility)
        assertEquals("", textView.text.toString())
        assertEquals(View.GONE, textView.visibility)
    }
}
