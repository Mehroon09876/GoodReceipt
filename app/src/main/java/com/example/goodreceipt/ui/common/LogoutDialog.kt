package com.example.goodreceipt.ui.common

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import com.example.goodreceipt.R
import com.example.goodreceipt.databinding.DialogLogoutConfirmationBinding

object LogoutDialog {

    fun show(
        context: Context,
        onLogoutConfirmed: () -> Unit,
        onCancel: (() -> Unit)? = null
    ): Dialog {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        
        val binding = DialogLogoutConfirmationBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        }
        
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)

        binding.btnCancel.setOnClickListener {
            onCancel?.invoke()
            dialog.dismiss()
        }

        binding.btnLogout.setOnClickListener {
            onLogoutConfirmed()
            dialog.dismiss()
        }

        dialog.show()
        return dialog
    }
}
