package com.adarsh.adarshmeditationapp.extensions

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

inline fun <reified VM : ViewModel> Fragment.vmProvider(
    viewModelFactory: ViewModelProvider.Factory, provider: (VM) -> Unit
) {
    provider(
        ViewModelProviders.of(this, viewModelFactory).get(VM::class.java)
    )
}

inline fun <reified VM : ViewModel> FragmentActivity.vmProvider(
    viewModelFactory: ViewModelProvider.Factory, provider: (VM) -> Unit
) {
    provider(
        ViewModelProviders.of(this, viewModelFactory).get(VM::class.java)
    )
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}