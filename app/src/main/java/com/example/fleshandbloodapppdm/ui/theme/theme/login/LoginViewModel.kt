package com.example.fleshandbloodapppdm.ui.theme.theme.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.fleshandbloodapppdm.repositories.LoginRepository
import com.example.fleshandbloodapppdm.repositories.ResultWrapper
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginState(
    var username: String? = null,
    var password: String?  = null,
    var error: String? = null,
    var isLoading: Boolean? = null
)
@HiltViewModel
class LoginViewModel @Inject constructor(
    val loginRepository: LoginRepository
): ViewModel() {

    var uiState = mutableStateOf(LoginState())
        private set

    fun setUsername(username: String) {
        uiState.value = uiState.value.copy(username = username)
    }

    fun setPassword(password: String) {
        uiState.value = uiState.value.copy(password = password)
    }

    fun login(onLoginSuccess : () -> Unit) {
        uiState.value = uiState.value.copy(
            isLoading = true,
            error = null
        )

        val username = uiState.value.username
        val password = uiState.value.password

        if (username.isNullOrEmpty()) {
            uiState.value = uiState.value.copy(
                error = "Username is required",
                isLoading = false
            )
            return
        }

        if (password.isNullOrEmpty()) {
            uiState.value = uiState.value.copy(
                error = "Password is required",
                isLoading = false
            )
            return
        }

        // do login
        viewModelScope.launch {
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(username, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        uiState.value = uiState.value.copy(
                            error = null,
                            isLoading = false
                        )
                        onLoginSuccess()
                    } else {
                        uiState.value = uiState.value.copy(
                            error = task.exception?.message ?: "Login failed",
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun register(onRegisterSuccess: () -> Unit) {
        uiState.value = uiState.value.copy(
            isLoading = true,
            error = null
        )

        val username = uiState.value.username
        val password = uiState.value.password

        if (username.isNullOrEmpty()) {
            uiState.value = uiState.value.copy(
                error = "Username is required",
                isLoading = false
            )
            return
        }

        if (password.isNullOrEmpty()) {
            uiState.value = uiState.value.copy(
                error = "Password is required",
                isLoading = false
            )
            return
        }

        loginRepository.register(
            username,
            password
        ).onEach { result ->
            when (result) {
                is ResultWrapper.Success -> {
                    onRegisterSuccess()
                    uiState.value = uiState.value.copy(
                        error = null,
                        isLoading = false
                    )
                }
                is ResultWrapper.Loading -> {
                    uiState.value = uiState.value.copy(
                        isLoading = true
                    )
                }
                is ResultWrapper.Error -> {
                    uiState.value = uiState.value.copy(
                        error = result.message,
                        isLoading = false
                    )
                }
            }
        }.launchIn(viewModelScope)
    }



}
