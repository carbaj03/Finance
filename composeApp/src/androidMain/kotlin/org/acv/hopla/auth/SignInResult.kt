package org.acv.hopla.auth

sealed interface SignInResult

data class UserData(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?
) : SignInResult

data class SignInError(val message :String) : SignInResult