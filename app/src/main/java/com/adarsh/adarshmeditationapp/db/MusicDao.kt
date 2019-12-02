package com.adarsh.adarshmeditationapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.adarsh.adarshmeditationapp.data.FirebaseModel

@Dao
interface MusicDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertData(listOfFirebaseModel: List<FirebaseModel>)

    @Query("Select * from FirebaseModel")
    fun getAllMusicDetails(): List<FirebaseModel>

    @Query("Delete from FirebaseModel")
    fun clearAllMusicDetails()

}