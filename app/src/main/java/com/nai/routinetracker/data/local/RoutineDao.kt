package com.nai.routinetracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {
    @Query("SELECT COUNT(*) FROM routines")
    suspend fun count(): Int

    @Query("SELECT * FROM routines ORDER BY sort_order ASC, title ASC")
    fun observeRoutines(): Flow<List<LocalRoutineEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(routine: LocalRoutineEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(routines: List<LocalRoutineEntity>)

    @Query("DELETE FROM routines")
    suspend fun deleteAll()
}
