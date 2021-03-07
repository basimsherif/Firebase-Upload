package com.example.sparktest.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.sparktest.R

/**
 * All UI data binding adapters comes under this class
 */
@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
    if (!url.isNullOrEmpty()) {
        Glide.with(view).load(url).placeholder(R.drawable.placeholder).into(view)
    }
}