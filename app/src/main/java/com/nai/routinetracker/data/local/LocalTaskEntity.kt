package com.nai.routinetracker.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = LocalRoutineEntity::class,
            parentColumns = ["id"],
            childColumns = ["routine_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("routine_id")
    ]
)
data class LocalTaskEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "routine_id") val routineId: String?,
    val title: String,
    @ColumnInfo(name = "time_label") val timeLabel: String,
    @ColumnInfo(name = "due_label") val dueLabel: String,
    @ColumnInfo(name = "category_id") val categoryId: String,
    @ColumnInfo(name = "category_label") val categoryLabel: String,
    @ColumnInfo(name = "category_is_system") val categoryIsSystem: Boolean,
    val status: String,
    val description: String,
    @ColumnInfo(name = "sort_order") val sortOrder: Int
)
