package com.adarsh.adarshmeditationapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.adarsh.adarshmeditationapp.main.FireBaseVM
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class VMMoulde {

    @Binds
    abstract fun bindsViewModelProvider(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FireBaseVM::class)
    abstract fun bindsFireBaseVM(fireBaseVM: FireBaseVM): ViewModel

}