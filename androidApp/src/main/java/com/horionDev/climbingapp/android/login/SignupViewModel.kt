//package com.horion.murbex.android.signup
//
//import androidx.lifecycle.SavedStateHandle
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.horion.murbex.data.ResultWrapper
//import com.horion.murbex.domain.model.User
//import com.horion.murbex.domain.usecases.managingUser.SignupUseCase
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.combine
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.launch
//
//
//// TODO: warning the private var are not in savestatehandle thus they can be destroyed when app goes in background
//class SignupViewModel(
//    private var savedStateHandle: SavedStateHandle,
//    private var signupUseCase: SignupUseCase
//) : ViewModel() {
//
//    private lateinit var username: String
//    private lateinit var mail: String
//    private lateinit var pass: String
//
//    val signupIsComplete = savedStateHandle.getStateFlow(
//        "signupIsComplete", false
//    )
//
//    private val usernameIsCorrect = savedStateHandle.getStateFlow("usernameIsCorrect", false)
//    private val mailIsCorrect = savedStateHandle.getStateFlow("mailIsCorrect", false)
//    private val passIsCorrect = savedStateHandle.getStateFlow("passIsCorrect", false)
//
//
//    val signupIsValid = combine(
//        usernameIsCorrect, mailIsCorrect, passIsCorrect
//    ) { usernameIsCorrect, mailIsCorrect, passIsCorrect ->
//        usernameIsCorrect && mailIsCorrect && passIsCorrect
//    }.stateIn(
//        viewModelScope, SharingStarted.WhileSubscribed(5000), false
//    )
//
//    fun controlUsername(username: String) {
//        if (username.isNotEmpty()) {
//            this.username = username
//            savedStateHandle["usernameIsCorrect"] = true
//        }else {
//            savedStateHandle["usernameIsCorrect"] = false
//        }
//
//    }
//
//    fun controlMail(mail: String) {
//        if (mail.isNotEmpty()) {
//            this.mail = mail
//            savedStateHandle["mailIsCorrect"] = true
//        }else {
//            savedStateHandle["mailIsCorrect"] = false
//        }
//    }
//
//    fun controlPass(pass: String) {
//        if (pass.isNotEmpty()) {
//            this.pass = pass
//            savedStateHandle["passIsCorrect"] = true
//        }else {
//            savedStateHandle["passIsCorrect"] = false
//        }
//    }
//
//    fun signup() {
//        viewModelScope.launch {
//            val user = User(0, username, pass, mail, "U")
//            when (signupUseCase(user)) {
//                is ResultWrapper.Failure -> {}
//                is ResultWrapper.Success -> {
//                    savedStateHandle["signupIsComplete"] = true
//                }
//            }
//        }
//    }
//}