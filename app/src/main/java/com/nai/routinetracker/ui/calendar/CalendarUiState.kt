package com.nai.routinetracker.ui.calendar

import com.nai.routinetracker.model.RoutineItem
import com.nai.routinetracker.model.RoutineWeekday
import com.nai.routinetracker.model.TaskItem
import com.nai.routinetracker.model.isDone
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale

data class CalendarDate(
    val year: Int,
    val month: Int,
    val day: Int
) : Comparable<CalendarDate> {
    override fun compareTo(other: CalendarDate): Int {
        return compareValuesBy(this, other, CalendarDate::year, CalendarDate::month, CalendarDate::day)
    }

    fun toMonth(): CalendarMonth {
        return CalendarMonth(year = year, month = month)
    }

    fun plusDays(days: Int): CalendarDate {
        val calendar = toCalendar()
        calendar.add(Calendar.DAY_OF_MONTH, days)
        return calendar.toCalendarDate()
    }

    fun formatDayLabel(locale: Locale = Locale.getDefault()): String {
        return SimpleDateFormat("EEE, MMM d", locale).format(toCalendar().time)
    }

    fun formatFullLabel(locale: Locale = Locale.getDefault()): String {
        return SimpleDateFormat("EEEE, MMMM d", locale).format(toCalendar().time)
    }

    internal fun mondayFirstDayOfWeekIndex(): Int {
        val calendarDay = toCalendar().get(Calendar.DAY_OF_WEEK)
        return (calendarDay + 5) % 7
    }

    internal fun toCalendar(): Calendar {
        return GregorianCalendar(year, month - 1, day).apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
    }

    companion object {
        fun today(): CalendarDate {
            return Calendar.getInstance().toCalendarDate()
        }
    }
}

data class CalendarMonth(
    val year: Int,
    val month: Int
) {
    fun previous(): CalendarMonth {
        return plusMonths(-1)
    }

    fun next(): CalendarMonth {
        return plusMonths(1)
    }

    fun contains(date: CalendarDate): Boolean {
        return date.year == year && date.month == month
    }

    fun dateForDayClamped(day: Int): CalendarDate {
        return CalendarDate(
            year = year,
            month = month,
            day = day.coerceIn(1, daysInMonth())
        )
    }

    fun formatLabel(locale: Locale = Locale.getDefault()): String {
        val monthName = DateFormatSymbols.getInstance(locale).months[month - 1]
        return "$monthName $year"
    }

    internal fun daysInMonth(): Int {
        return GregorianCalendar(year, month - 1, 1).getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    private fun plusMonths(delta: Int): CalendarMonth {
        val calendar = GregorianCalendar(year, month - 1, 1)
        calendar.add(Calendar.MONTH, delta)
        return CalendarMonth(
            year = calendar.get(Calendar.YEAR),
            month = calendar.get(Calendar.MONTH) + 1
        )
    }
}

data class CalendarDayUi(
    val date: CalendarDate,
    val isInVisibleMonth: Boolean,
    val isToday: Boolean,
    val isSelected: Boolean,
    val taskCount: Int,
    val routineCount: Int
)

data class CalendarUiState(
    val isLoading: Boolean,
    val today: CalendarDate,
    val visibleMonth: CalendarMonth,
    val selectedDate: CalendarDate,
    val monthDays: List<CalendarDayUi>,
    val selectedRoutines: List<RoutineItem>,
    val selectedTasks: List<TaskItem>
) {
    val visibleMonthLabel: String
        get() = visibleMonth.formatLabel()

    val selectedDateLabel: String
        get() = selectedDate.formatFullLabel()

    companion object {
        fun initial(today: CalendarDate = CalendarDate.today()): CalendarUiState {
            return CalendarStateFactory.build(
                today = today,
                visibleMonth = today.toMonth(),
                selectedDate = today,
                routines = emptyList(),
                tasks = emptyList(),
                isLoading = true
            )
        }
    }
}

object CalendarStateFactory {
    fun build(
        today: CalendarDate,
        visibleMonth: CalendarMonth,
        selectedDate: CalendarDate,
        routines: List<RoutineItem>,
        tasks: List<TaskItem>,
        isLoading: Boolean = false
    ): CalendarUiState {
        val selectedDateInMonth = visibleMonth.dateForDayClamped(selectedDate.day)
        val activeRoutines = routines
            .filter { it.isActive }
            .sortedWith(compareBy<RoutineItem> { it.scheduleLabel }.thenBy { it.title })
        val selectedRoutines = activeRoutines.filter { routine ->
            routine.recurrence.matches(selectedDateInMonth.toRoutineWeekday())
        }
        val tasksByDate = mapTasksToDates(tasks = tasks, today = today)
        val selectedTasks = tasksByDate[selectedDateInMonth].orEmpty()
            .sortedWith(compareBy<TaskItem> { it.isDone }.thenBy { it.timeLabel }.thenBy { it.title })

        return CalendarUiState(
            isLoading = isLoading,
            today = today,
            visibleMonth = visibleMonth,
            selectedDate = selectedDateInMonth,
            monthDays = buildMonthDays(
                today = today,
                visibleMonth = visibleMonth,
                selectedDate = selectedDateInMonth,
                taskCounts = tasksByDate.mapValues { it.value.size },
                activeRoutines = activeRoutines
            ),
            selectedRoutines = selectedRoutines,
            selectedTasks = selectedTasks
        )
    }

    fun mapTasksToDates(
        tasks: List<TaskItem>,
        today: CalendarDate
    ): Map<CalendarDate, List<TaskItem>> {
        return tasks
            .mapNotNull { task ->
                val date = task.mappedDueDate(today) ?: return@mapNotNull null
                date to task
            }
            .groupBy(
                keySelector = { it.first },
                valueTransform = { it.second }
            )
    }

    private fun buildMonthDays(
        today: CalendarDate,
        visibleMonth: CalendarMonth,
        selectedDate: CalendarDate,
        taskCounts: Map<CalendarDate, Int>,
        activeRoutines: List<RoutineItem>
    ): List<CalendarDayUi> {
        val firstOfMonth = CalendarDate(
            year = visibleMonth.year,
            month = visibleMonth.month,
            day = 1
        )
        val gridStart = firstOfMonth.plusDays(-firstOfMonth.mondayFirstDayOfWeekIndex())

        return List(42) { index ->
            val date = gridStart.plusDays(index)
            val isInVisibleMonth = visibleMonth.contains(date)
            CalendarDayUi(
                date = date,
                isInVisibleMonth = isInVisibleMonth,
                isToday = date == today,
                isSelected = date == selectedDate,
                taskCount = taskCounts[date].orEmptyCount(),
                routineCount = if (isInVisibleMonth) {
                    val weekday = date.toRoutineWeekday()
                    activeRoutines.count { it.recurrence.matches(weekday) }
                } else {
                    0
                }
            )
        }
    }

    private fun TaskItem.mappedDueDate(today: CalendarDate): CalendarDate? {
        return when (dueLabel.trim().lowercase(Locale.getDefault())) {
            "today" -> today
            "tomorrow" -> today.plusDays(1)
            else -> null
        }
    }

    private fun Int?.orEmptyCount(): Int {
        return this ?: 0
    }
}

private fun Calendar.toCalendarDate(): CalendarDate {
    return CalendarDate(
        year = get(Calendar.YEAR),
        month = get(Calendar.MONTH) + 1,
        day = get(Calendar.DAY_OF_MONTH)
    )
}

private fun CalendarDate.toRoutineWeekday(): RoutineWeekday {
    return RoutineWeekday.fromMondayFirstIndex(mondayFirstDayOfWeekIndex())
}
