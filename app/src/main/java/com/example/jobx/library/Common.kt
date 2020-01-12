package com.example.jobx.library

import android.content.Context
import android.widget.Button
import androidx.core.content.ContextCompat
import com.example.jobx.R
import com.example.jobx.database.User

class Common {
    companion object {
        lateinit var user: User

        fun buttonDisable(button: Button, context: Context) {
            button.isEnabled = false
            button.isClickable = false
            button.setTextColor(ContextCompat.getColor(context, R.color.white))
            button.setBackgroundColor(ContextCompat.getColor(context, R.color.greyish))
        }

        fun buttonEnable(button: Button, context: Context) {
            button.isEnabled = true
            button.isClickable = true
            button.setTextColor(ContextCompat.getColor(context, R.color.white))
            button.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
        }
    }
}