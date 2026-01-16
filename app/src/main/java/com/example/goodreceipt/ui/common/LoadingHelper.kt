package com.example.goodreceipt.ui.common

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.example.goodreceipt.R
import com.example.goodreceipt.databinding.DialogLoadingBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadingHelper @Inject constructor() {

    private var loadingDialog: Dialog? = null

    fun showLoading(context: Context, message: String? = null) {
        hideLoading()

        loadingDialog = Dialog(context)
        loadingDialog?.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            
            val binding = DialogLoadingBinding.inflate(LayoutInflater.from(context))
            setContentView(binding.root)
            
            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
            }
            
            setCancelable(false)
            setCanceledOnTouchOutside(false)

            message?.let {
                binding.tvLoadingText.text = it
                binding.tvLoadingText.visibility = View.VISIBLE
            } ?: run {
                binding.tvLoadingText.visibility = View.GONE
            }
        }

        loadingDialog?.show()
    }

    fun showLoading(fragment: Fragment, message: String? = null) {
        fragment.context?.let {
            showLoading(it, message)
        }
    }

    fun hideLoading() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    fun isShowing(): Boolean {
        return loadingDialog?.isShowing == true
    }
}
