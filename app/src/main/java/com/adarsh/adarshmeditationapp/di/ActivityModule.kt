package com.adarsh.adarshmeditationapp.di

import com.adarsh.adarshmeditationapp.main.MainActivity
import com.adarsh.adarshmeditationapp.meditation.MeditationActivity
import com.adarsh.adarshmeditationapp.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [VMMoulde::class])
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeSplashActivity(): SplashActivity

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeMeditationActivity(): MeditationActivity
}