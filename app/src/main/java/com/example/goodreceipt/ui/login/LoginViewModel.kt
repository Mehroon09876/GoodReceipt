package com.example.goodreceipt.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goodreceipt.data.model.LoginRequest
import com.example.goodreceipt.data.model.LoginResponse
import com.example.goodreceipt.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginSuccess = MutableLiveData<LoginResponse?>()
    val loginSuccess: LiveData<LoginResponse?> = _loginSuccess

    private val _loginError = MutableLiveData<String?>()
    val loginError: LiveData<String?> = _loginError

    private val _emailError = MutableLiveData<String?>()
    val emailError: LiveData<String?> = _emailError

    private val _passwordError = MutableLiveData<String?>()
    val passwordError: LiveData<String?> = _passwordError

    fun login(email: String, password: String) {
        _emailError.value = null
        _passwordError.value = null
        _loginError.value = null
        _loginSuccess.value = null

        if (email.isBlank()) {
            _emailError.value = "Email is required"
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailError.value = "Invalid email format"
            return
        }

        if (password.isBlank()) {
            _passwordError.value = "Password is required"
            return
        }

        if (password.length < 6) {
            _passwordError.value = "Password must be at least 6 characters"
            return
        }

        _isLoading.value = true

        viewModelScope.launch {
            val result = authRepository.login(LoginRequest(email, password))
            _isLoading.value = false
            
            if (result.isSuccess) {
                saveEmail(email)
                _loginSuccess.value = result.getOrNull()
            } else {
                val exception = result.exceptionOrNull()
                _loginError.value = exception?.message ?: "Login failed"
            }
        }
    }

    fun clearErrors() {
        _emailError.value = null
        _passwordError.value = null
        _loginError.value = null
    }

    fun saveEmail(email:String){
        authRepository.saveEmail(email)
        authRepository.setLoggedIn(true)
    }

}
