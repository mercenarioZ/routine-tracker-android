package com.nai.routinetracker.ui.login

sealed interface LoginEffect {
    data object LoginSuccess : LoginEffect
}
