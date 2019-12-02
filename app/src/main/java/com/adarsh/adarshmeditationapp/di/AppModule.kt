package com.adarsh.adarshmeditationapp.di

import android.app.Application
import com.adarsh.adarshmeditationapp.R
import com.adarsh.adarshmeditationapp.db.MusicDatabase
import com.adarsh.adarshmeditationapp.meditationApplication
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module()
class AppModule {

    @Singleton
    @Provides
    fun providesGoogleSignInOptions() =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(meditationApplication?.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

    @Singleton
    @Provides
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun providesFirebaseFireStoreInstance() = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun providesMusicDatabase(application: Application) = MusicDatabase.invoke(application)

    @Singleton
    @Provides
    fun provideMusicDao(musicDatabase: MusicDatabase) = musicDatabase.getMusicDao()
}