package com.horionDev.climbingapp.android.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horionDev.climbingapp.data.ResultWrapper
import com.horionDev.climbingapp.data.message
import com.horionDev.climbingapp.domain.repositories.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SignupViewModel(
    private var savedStateHandle: SavedStateHandle,
    private var users: UserRepository
) : ViewModel() {

    private lateinit var username: String
    private lateinit var mail: String
    private lateinit var pass: String

    val signupIsComplete = savedStateHandle.getStateFlow(
        "signupIsComplete", false
    )

    private val usernameIsCorrect = savedStateHandle.getStateFlow("usernameIsCorrect", false)
    private val mailIsCorrect = savedStateHandle.getStateFlow("mailIsCorrect", false)
    private val passIsCorrect = savedStateHandle.getStateFlow("passIsCorrect", false)
    val errorMessage = savedStateHandle.getStateFlow("errorMessage", "")


    val signupIsValid = combine(
        usernameIsCorrect, mailIsCorrect, passIsCorrect
    ) { usernameIsCorrect, mailIsCorrect, passIsCorrect ->
        usernameIsCorrect && mailIsCorrect && passIsCorrect
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), false
    )

    fun controlUsername(username: String) {
        if (username.isNotEmpty()) {
            this.username = username
            savedStateHandle["usernameIsCorrect"] = true
        }else {
            savedStateHandle["usernameIsCorrect"] = false
        }

    }

    fun controlMail(mail: String) {
        if (mail.isNotEmpty()) {
            this.mail = mail
            savedStateHandle["mailIsCorrect"] = true
        }else {
            savedStateHandle["mailIsCorrect"] = false
        }
    }

    fun controlPass(pass: String) {
        if (pass.isNotEmpty()) {
            this.pass = pass
            savedStateHandle["passIsCorrect"] = true
        }else {
            savedStateHandle["passIsCorrect"] = false
        }
    }

    fun signup() {
        viewModelScope.launch {
            when (val result = users.signup(username, pass, mail)) {
                is ResultWrapper.Failure -> {
                    savedStateHandle["errorMessage"] = result.message()
                }
                is ResultWrapper.Success -> {
                    savedStateHandle["signupIsComplete"] = true
                }
            }
        }
    }
}