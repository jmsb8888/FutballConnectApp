package com.task.futballconnectapp.data.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SharedPreferencesViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _idUser = MutableStateFlow(0)
    val idUser: StateFlow<Int> = _idUser.asStateFlow()

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _userImageProfile = MutableStateFlow("")
    val userImageProfile: StateFlow<String> = _userImageProfile.asStateFlow()

    fun getIdUser(): Int {
        return sharedPreferences.getInt("idUser", 0)
    }

    fun setIdUser(idUser: Int) {
        sharedPreferences.edit().putInt("idUser", idUser).apply()
        _idUser.value = idUser
    }

    fun getUserName(): String {
        return sharedPreferences.getString("userName", "NoName")
            ?: "NoName"
    }

    fun setUserName(userName: String) {
        sharedPreferences.edit().putString("userName", userName).apply()
        _userName.value = userName
    }

    fun getUserImage(): String {
        return sharedPreferences.getString("userImageProfile", "NoImage") ?: "NoImage"
    }

    fun setUserImage(userImageProfile: String) {
        sharedPreferences.edit().putString("userImageProfile", userImageProfile).apply()
        _userImageProfile.value = userImageProfile
    }

    fun clearUserId() {
        sharedPreferences.edit().remove("idUser").apply()
        _idUser.value = 0
    }


}
