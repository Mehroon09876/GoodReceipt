package com.example.goodreceipt.utils

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit
import com.example.goodreceipt.utils.Constants.BLANK_STRING

@Singleton
class PrefsHelper @Inject constructor(@ApplicationContext private val ctx: Context) {
    companion object {
        const val PREF_NAME = "secured_gr_pref"
        const val PREF_IS_LOGIN = "is_login"
        const val PREF_USER_EMAIL = "user_email"
        const val PREF_USER_ID = "user_id"

    }

    val prefs: SharedPreferences by lazy {
        ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
    


    fun saveIsLogin(isLogin: Boolean) {
        prefs.edit { putBoolean(PREF_IS_LOGIN, isLogin) }
    }

    fun isLogin(): Boolean {
        return prefs.getBoolean(PREF_IS_LOGIN, false)
    }


    fun clearSharedPrefs() {
        prefs.edit { clear() }
    }

    fun saveUserEmail(email: String) {
        prefs.edit { putString(PREF_USER_EMAIL, email) }
    }

    fun getUserEmail(): String? {
        return prefs.getString(PREF_USER_EMAIL, BLANK_STRING)
    }

}
