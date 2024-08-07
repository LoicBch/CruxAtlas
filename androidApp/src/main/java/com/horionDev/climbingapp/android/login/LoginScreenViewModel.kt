//package com.horionDev.climbingapp.android.login
//
//import androidx.lifecycle.SavedStateHandle
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.horion.murbex.data.ResultWrapper
//import com.horion.murbex.data.datasources.remote.BackendApi
//import com.horion.murbex.domain.model.AuthRequest
//import com.horion.murbex.domain.usecases.managingUser.LoginUseCase
//import com.horion.murbex.utils.SessionManager.user
//import kotlinx.coroutines.flow.*
//import kotlinx.coroutines.launch
//
//class LoginScreenViewModel(
//    private val savedStateHandle: SavedStateHandle,
//    private val loginUseCase: LoginUseCase
//) : ViewModel() {
//
//    private val _event = MutableSharedFlow<LoginScreenEvent>()
//    val event = _event.asSharedFlow()
//
//    private lateinit var username: String
//    private lateinit var pass: String
//
//    val loginIsComplete = savedStateHandle.getStateFlow(
//        "loginIsComplete", false
//    )
//    private val usernameIsCorrect = savedStateHandle.getStateFlow("usernameIsCorrect", false)
//    private val passIsCorrect = savedStateHandle.getStateFlow("passIsCorrect", false)
//    val loginFailed = savedStateHandle.getStateFlow("loginFailed", false)
//
//
//    val loginValid = combine(
//        usernameIsCorrect, passIsCorrect
//    ) { usernameIsCorrect, passIsCorrect ->
//        usernameIsCorrect && passIsCorrect
//    }.stateIn(
//        viewModelScope, SharingStarted.WhileSubscribed(5000), false
//    )
//
//    fun controlUsername(username: String) {
//        if (username.isNotEmpty()) {
//            this.username = username
//            savedStateHandle["usernameIsCorrect"] = true
//        } else {
//            savedStateHandle["usernameIsCorrect"] = false
//        }
//    }
//
//    fun controlPass(pass: String) {
//        if (pass.isNotEmpty()) {
//            this.pass = pass
//            savedStateHandle["passIsCorrect"] = true
//        } else {
//            savedStateHandle["passIsCorrect"] = false
//        }
//    }
//
//    fun login() {
//        viewModelScope.launch {
//            _event.emit(LoginScreenEvent.ShowLoading)
//            when (loginUseCase(AuthRequest(username, pass))) {
//                is ResultWrapper.Success -> {
//                    savedStateHandle["loginIsComplete"] = true
//                    _event.emit(LoginScreenEvent.HideLoading)
//                }
//
//                is ResultWrapper.Failure -> {
//                    _event.emit(LoginScreenEvent.HideLoading)
//                    savedStateHandle["loginFailed"] = true
//                }
//            }
//        }
//    }
//
//    sealed class LoginScreenEvent {
//        data object ShowLoading : LoginScreenEvent()
//        data object HideLoading : LoginScreenEvent()
//    }
//}