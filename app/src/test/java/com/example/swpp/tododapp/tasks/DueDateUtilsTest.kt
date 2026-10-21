package com.example.swpp.tododapp.tasks

import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class DueDateUtilsTest {

    @Test
    fun dueDateLabel_completedTaskWithDueDateAndWithoutDueDate_returnsClear() {
        // GIVEN - A completed task with a due date and a completed task without one
        val today = LocalDate.of(2026, 10, 20)
        val dueDate = LocalDate.of(2026, 10, 21)

        // WHEN - Calculate each task's label
        val withDueDate = dueDateLabel(dueDate, today, true)
        val withoutDueDate = dueDateLabel(null, today, true)

        // THEN - Both completed tasks show CLEAR
        assertEquals("CLEAR", withDueDate)
        assertEquals("CLEAR", withoutDueDate)
    }

    @Test
    fun dueDateLabel_activeTaskWithoutDueDate_returnsEmptyString() {
        // GIVEN - An active task without a due date
        val today = LocalDate.of(2026, 10, 20)

        // WHEN - Calculate the task's label
        val label = dueDateLabel(null, today, false)

        // THEN - No label is returned
        assertEquals("", label)
    }

    @Test
    fun dueDateLabel_activeTaskDueToday_returnsDDay() {
        // GIVEN - An active task due on 2026-10-20
        val today = LocalDate.of(2026, 10, 20)

        // WHEN - Calculate the task's label
        val label = dueDateLabel(today, today, false)

        // THEN - The label is D-Day
        assertEquals("D-Day", label)
    }

    @Test
    fun dueDateLabel_activeTaskDueTomorrow_returnsDMinusOne() {
        // GIVEN - An active task due on 2026-10-21
        val today = LocalDate.of(2026, 10, 20)
        val dueDate = LocalDate.of(2026, 10, 21)

        // WHEN - Calculate the task's label
        val label = dueDateLabel(dueDate, today, false)

        // THEN - The label is D-1
        assertEquals("D-1", label)
    }

    @Test
    fun dueDateLabel_activeTaskOverdueByOneDay_returnsDPlusOne() {
        // GIVEN - An active task that was due on 2026-10-19
        val today = LocalDate.of(2026, 10, 20)
        val dueDate = LocalDate.of(2026, 10, 19)

        // WHEN - Calculate the task's label
        val label = dueDateLabel(dueDate, today, false)

        // THEN - The label is D+1
        assertEquals("D+1", label)
    }

    @Test
    fun dueDateLabel_activeTaskDueAcrossMonthBoundary_countsTotalDays() {
        // GIVEN - An active task due on 2026-11-25, 36 days after 2026-10-20
        val today = LocalDate.of(2026, 10, 20)
        val dueDate = LocalDate.of(2026, 11, 25)

        // WHEN - Calculate the task's label
        val label = dueDateLabel(dueDate, today, false)

        // THEN - The label counts all 36 calendar days
        assertEquals("D-36", label)
    }

    @Test
    fun dueDateLabel_activeTaskDueAcrossYearBoundary_countsTotalDays() {
        // GIVEN - An active task due on 2027-01-02, three days after 2026-12-30
        val today = LocalDate.of(2026, 12, 30)
        val dueDate = LocalDate.of(2027, 1, 2)

        // WHEN - Calculate the task's label
        val label = dueDateLabel(dueDate, today, false)

        // THEN - The label counts all three calendar days
        assertEquals("D-3", label)
    }

    @Test
    fun dueDateLabel_validInputs_returnsLabelWithoutThrowing() {
        // GIVEN - A valid date and a missing due date for active and completed tasks
        val today = LocalDate.of(2026, 10, 20)
        val dueDate = LocalDate.of(2026, 10, 21)

        // WHEN - Calculate labels for each valid input combination
        val activeWithoutDate = dueDateLabel(null, today, false)
        val activeWithDate = dueDateLabel(dueDate, today, false)
        val completedWithoutDate = dueDateLabel(null, today, true)
        val completedWithDate = dueDateLabel(dueDate, today, true)

        // THEN - Each call returns its expected label without throwing
        assertEquals("", activeWithoutDate)
        assertEquals("D-1", activeWithDate)
        assertEquals("CLEAR", completedWithoutDate)
        assertEquals("CLEAR", completedWithDate)
    }

    @Test
    fun dueDateLabel_datesAroundDueDate_returnsAdjacentLabels() {
        // GIVEN - The due date is 2026-10-20
        val dueDate = LocalDate.of(2026, 10, 20)

        // WHEN - Calculate labels for the day before, due date, and day after
        val before = dueDateLabel(dueDate, LocalDate.of(2026, 10, 19), false)
        val onDate = dueDateLabel(dueDate, LocalDate.of(2026, 10, 20), false)
        val after = dueDateLabel(dueDate, LocalDate.of(2026, 10, 21), false)

        // THEN - The labels are D-1, D-Day, and D+1
        assertEquals("D-1", before)
        assertEquals("D-Day", onDate)
        assertEquals("D+1", after)
    }

    @Test
    fun dueDateLabel_datesAroundMonthBoundary_returnsAdjacentLabels() {
        // GIVEN - Today is 2026-10-31 and the due date moves around month end
        val today = LocalDate.of(2026, 10, 31)

        // WHEN - Calculate labels for 2026-10-30, 2026-10-31, and 2026-11-01
        val before = dueDateLabel(LocalDate.of(2026, 10, 30), today, false)
        val onDate = dueDateLabel(LocalDate.of(2026, 10, 31), today, false)
        val after = dueDateLabel(LocalDate.of(2026, 11, 1), today, false)

        // THEN - The labels are D+1, D-Day, and D-1
        assertEquals("D+1", before)
        assertEquals("D-Day", onDate)
        assertEquals("D-1", after)
    }

    @Test
    fun dueDateLabel_datesAroundYearBoundary_returnsAdjacentLabels() {
        // GIVEN - Today is 2026-12-31 and the due date moves around year end
        val today = LocalDate.of(2026, 12, 31)

        // WHEN - Calculate labels for 2026-12-30, 2026-12-31, and 2027-01-01
        val before = dueDateLabel(LocalDate.of(2026, 12, 30), today, false)
        val onDate = dueDateLabel(LocalDate.of(2026, 12, 31), today, false)
        val after = dueDateLabel(LocalDate.of(2027, 1, 1), today, false)

        // THEN - The labels are D+1, D-Day, and D-1
        assertEquals("D+1", before)
        assertEquals("D-Day", onDate)
        assertEquals("D-1", after)
    }

    @Test
    fun dueDateLabel_acrossLeapDay_countsLeapDay() {
        // GIVEN - Today is 2028-02-28 and the task is due on 2028-03-01
        val today = LocalDate.of(2028, 2, 28)
        val dueDate = LocalDate.of(2028, 3, 1)

        // WHEN - Calculate the task's label
        val label = dueDateLabel(dueDate, today, false)

        // THEN - February 29 is counted and the label is D-2
        assertEquals("D-2", label)
    }

    @Test
    fun dueDateLabel_datesAroundLeapDay_returnsAdjacentLabels() {
        // GIVEN - Today is leap day, 2028-02-29
        val today = LocalDate.of(2028, 2, 29)

        // WHEN - Calculate labels for 2028-02-28, 2028-02-29, and 2028-03-01
        val before = dueDateLabel(LocalDate.of(2028, 2, 28), today, false)
        val onDate = dueDateLabel(LocalDate.of(2028, 2, 29), today, false)
        val after = dueDateLabel(LocalDate.of(2028, 3, 1), today, false)

        // THEN - The labels are D+1, D-Day, and D-1
        assertEquals("D+1", before)
        assertEquals("D-Day", onDate)
        assertEquals("D-1", after)
    }
}
