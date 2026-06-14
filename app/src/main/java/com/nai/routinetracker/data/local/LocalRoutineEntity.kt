package com.nai.routinetracker.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routines")
data class LocalRoutineEntity(
    @PrimaryKey val id: String,
    val title: String,
    @ColumnInfo(name = "schedule_label") val scheduleLabel: String,
    @ColumnInfo(name = "category_id") val categoryId: String,
    @ColumnInfo(name = "category_label") val categoryLabel: String,
    @ColumnInfo(name = "category_is_system") val categoryIsSystem: Boolean,
    @ColumnInfo(name = "streak_days") val streakDays: Int,
    @ColumnInfo(name = "is_active") val isActive: Boolean,
    val description: String,
    @ColumnInfo(name = "sort_order") val sortOrder: Int
)
