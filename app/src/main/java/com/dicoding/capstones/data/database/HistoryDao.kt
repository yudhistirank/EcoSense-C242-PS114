package com.dicoding.capstones.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.capstones.data.model.History

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHistory(historyDB: History): Long

    @Delete
    suspend fun deleteHistory(historyDB: History)

    @Query("SELECT * from history_database")
    fun getAllHistory(): LiveData<List<History>>

    @Query("SELECT * from history_database WHERE id= :id")
    fun getHistoryById(id: String?): LiveData<History>

    @Query("SELECT COUNT(*) FROM history_database")
    suspend fun getHistoryCount(): Int

    @Query("SELECT * FROM history_database ORDER BY id ASC LIMIT 1")
    suspend fun getOldestHistory(): History?

}