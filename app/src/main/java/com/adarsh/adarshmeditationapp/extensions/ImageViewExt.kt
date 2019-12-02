package com.adarsh.adarshmeditationapp.extensions

import android.widget.ImageView
import com.adarsh.adarshmeditationapp.R
import com.squareup.picasso.Picasso

fun ImageView.loadImage(imageUrl: String, placeholder: Int = R.drawable.default_thumbnail) {

    Picasso.get()
        .load(imageUrl)
        .fit()
        .centerCrop()
        .placeholder(placeholder)
        .error(placeholder)
        .into(this)

}