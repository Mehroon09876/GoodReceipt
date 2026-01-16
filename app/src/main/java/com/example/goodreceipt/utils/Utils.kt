package com.example.goodreceipt.utils

import android.content.Context
import android.widget.Toast

fun Context.showToast(message:String){
    if(message.isNotEmpty()) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.show()
    }
}