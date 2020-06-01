package com.example.core.utils

import android.content.res.Resources
import android.util.TypedValue
import android.widget.Toast
import com.example.core.BaseApplication

private val displayMetrics by lazy {
    Resources.getSystem().displayMetrics
}

val Float.dp: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, displayMetrics)

@JvmOverloads
fun toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(BaseApplication.currentApplication, message, duration).show()
}