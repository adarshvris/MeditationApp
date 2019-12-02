package com.adarsh.adarshmeditationapp.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class FirebaseModel(
    @PrimaryKey @SerializedName("key") var key: String = "",
    @SerializedName("name") var name: String = "",
    @SerializedName("doing_right_now") var doing_right_now: Int = 0,
    @SerializedName("musicModel") @Embedded var musicModel: MusicModel? = null
): Serializable

data class MusicModel(
    @SerializedName("imageLink") var imageLink: String = "",
    @SerializedName("link") var link: String = ""
): Serializable