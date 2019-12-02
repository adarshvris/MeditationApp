package com.adarsh.adarshmeditationapp.di

import android.app.Application
import com.adarsh.adarshmeditationapp.MeditationApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class,
        AppModule::class, ActivityModule::class]
)
interface AppComponent : AndroidInjector<MeditationApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun bind(application: Application): Builder

        fun build(): AppComponent
    }
}