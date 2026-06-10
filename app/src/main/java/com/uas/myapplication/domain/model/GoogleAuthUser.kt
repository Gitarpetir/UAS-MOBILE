package com.uas.myapplication.domain.model

data class GoogleAuthResult(
    val user: User,
    val isNewUser: Boolean
)