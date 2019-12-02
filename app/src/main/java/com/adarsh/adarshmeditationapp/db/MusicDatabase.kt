package com.adarsh.adarshmeditationapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.adarsh.adarshmeditationapp.constants.DATABASE_NAME
import com.adarsh.adarshmeditationapp.data.FirebaseModel

@Database(exportSchema = false, version = 1, entities = [FirebaseModel::class])
abstract class MusicDatabase : RoomDatabase() {

    abstract fun getMusicDao(): MusicDao

    companion object {
        private var musicDatabase: MusicDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = musicDatabase ?: synchronized(LOCK) {
            musicDatabase ?: createDataBase(context).also { musicDatabase = it }
        }

        private fun createDataBase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MusicDatabase::class.java,
                DATABASE_NAME
            ).build()
    }

}