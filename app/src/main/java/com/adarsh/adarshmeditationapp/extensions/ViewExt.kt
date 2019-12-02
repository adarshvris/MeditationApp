package com.adarsh.adarshmeditationapp.extensions

import android.view.View
import android.view.View.*

fun View.visible() {
    this.visibility = VISIBLE
}

fun View.gone() {
    this.visibility = GONE
}

fun View.invisible() {
    this.visibility = INVISIBLE
}