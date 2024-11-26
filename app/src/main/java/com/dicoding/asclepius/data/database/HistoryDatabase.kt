package com.dicoding.asclepius.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.asclepius.data.model.History

@Database(entities = [History::class], version = 1, exportSchema = false)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: HistoryDatabase? = null

        @JvmStatic
        fun getData(context: Context): HistoryDatabase {
            if (INSTANCE == null) {
                synchronized(HistoryDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        HistoryDatabase::class.java, "history_database"
                    ).build()
                }
            }
            return INSTANCE as HistoryDatabase
        }
    }
}