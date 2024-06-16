package com.rohman.nutriscanapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rohman.nutriscanapp.data.repository.ResultDeteksiRepository


@Database(entities = [ResultDeteksi::class], version = 2)
@TypeConverters(Converters::class)
abstract class ResultDeteksiRoomDatabase : RoomDatabase() {
    abstract fun resultDatabaseDao(): ResultDeteksiDao

    companion object {
        @Volatile
        private var INSTANCE: ResultDeteksiRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): ResultDeteksiRoomDatabase {
            if (INSTANCE == null) {
                synchronized(ResultDeteksiRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        ResultDeteksiRoomDatabase::class.java, "note_database"
                    )
                        .build()
                }
            }
            return INSTANCE as ResultDeteksiRoomDatabase
        }
    }

    fun getResultDeteksiRepository(): ResultDeteksiRepository {
        return ResultDeteksiRepository(resultDatabaseDao())
    }
}