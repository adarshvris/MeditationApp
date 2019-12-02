package com.adarsh.adarshmeditationapp

import android.app.Activity
import android.app.Application
import com.adarsh.adarshmeditationapp.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

val meditationApplication by lazy { MeditationApplication.instance }

class MeditationApplication : DaggerApplication(), HasActivityInjector {

    companion object {
        var instance: MeditationApplication? = null
    }

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Activity>

    override fun applicationInjector() = DaggerAppComponent.builder().bind(this).build()

    override fun activityInjector() = androidInjector

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}