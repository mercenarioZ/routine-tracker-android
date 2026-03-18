package com.nai.routinetracker.ui.home.preview

import com.nai.routinetracker.model.RoutineCategory
import com.nai.routinetracker.model.RoutineDashboardState
import com.nai.routinetracker.model.RoutineItem

object HomePreviewData {
    val dashboardState = RoutineDashboardState(
        userName = "Builder",
        dateLabel = "Thursday, March 18",
        highlight = "Small routines create stable days. Finish the next task before 8:00 AM.",
        routines = listOf(
            RoutineItem(
                id = "hydration",
                title = "Hydrate and stretch",
                scheduleLabel = "06:30 AM",
                category = RoutineCategory.Health,
                streakDays = 12,
                completed = true,
                description = "Drink water, stretch for five minutes, and wake the body up."
            ),
            RoutineItem(
                id = "planning",
                title = "Plan top 3 tasks",
                scheduleLabel = "07:00 AM",
                category = RoutineCategory.Planning,
                streakDays = 8,
                completed = false,
                description = "Write the three outcomes that must happen today."
            ),
            RoutineItem(
                id = "focus",
                title = "Deep work block",
                scheduleLabel = "09:00 AM",
                category = RoutineCategory.Focus,
                streakDays = 15,
                completed = true,
                description = "Start with 45 minutes of distraction-free work on the hardest task."
            ),
            RoutineItem(
                id = "learning",
                title = "Android learning sprint",
                scheduleLabel = "08:30 PM",
                category = RoutineCategory.Learning,
                streakDays = 5,
                completed = false,
                description = "Spend 20 minutes improving one native Android skill."
            )
        )
    )
}
