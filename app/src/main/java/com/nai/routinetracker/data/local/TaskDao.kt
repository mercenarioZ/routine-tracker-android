package com.nai.routinetracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT COUNT(*) FROM tasks")
    suspend fun count(): Int

    @Query("SELECT * FROM tasks ORDER BY sort_order ASC, title ASC")
    fun observeTasks(): Flow<List<LocalTaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: LocalTaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tasks: List<LocalTaskEntity>)

    @Query("DELETE FROM tasks")
    suspend fun deleteAll()

    @Query(
        """
        UPDATE tasks
        SET status = CASE status
            WHEN 'Pending' THEN 'Done'
            ELSE 'Pending'
        END
        WHERE id = :taskId
        """
    )
    suspend fun toggleStatus(taskId: String)
}
