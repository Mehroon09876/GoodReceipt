package com.example.goodreceipt.data.repository

import com.example.goodreceipt.data.model.LoginRequest
import com.example.goodreceipt.data.model.LoginResponse
import com.example.goodreceipt.utils.PrefsHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val prefsHelper: PrefsHelper
) {
    
    suspend fun login(loginRequest: LoginRequest): Result<LoginResponse> {
        return try {
            kotlinx.coroutines.delay(1000)
            Result.success(
                LoginResponse(
                    success = true,
                    token = "mock_token_${System.currentTimeMillis()}",
                    message = "Login successful"
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun saveEmail(email:String){
        prefsHelper.saveUserEmail(email)
    }

    fun setLoggedIn(isLogin: Boolean){
        prefsHelper.saveIsLogin(isLogin)
    }
}
