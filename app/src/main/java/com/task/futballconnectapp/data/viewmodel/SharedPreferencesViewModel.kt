package com.task.futballconnectapp.data.viewmodel

import android.content.SharedPreferences
import android.util.Log
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

    // Función para obtener el ID del usuario almacenado
    fun getIdUser(): Int {
        return sharedPreferences.getInt("idUser", 0) // Valor por defecto es 0
    }

    // Función para guardar el ID del usuario
    fun setIdUser(idUser: Int) {
        sharedPreferences.edit().putInt("idUser", idUser).apply()
        Log.d("idUserView", "$idUser")
        _idUser.value = idUser  // Actualizar el valor en _idUser
    }

    fun getUserName(): String {
        return sharedPreferences.getString("userName", "NoName")
            ?: "NoName" // Valor por defecto es "NoName"
    }

    // Función para guardar el ID del usuario
    fun setUserName(userName: String) {
        sharedPreferences.edit().putString("userName", userName).apply()
        _userName.value = userName  // Actualizar el valor en _idUser
    }

    fun getUserImage(): String {
        return sharedPreferences.getString("userImageProfile", "NoImage") ?: "NoImage"
    }

    // Función para guardar el ID del usuario
    fun setUserImage(userImageProfile: String) {
        sharedPreferences.edit().putString("userImageProfile", userImageProfile).apply()
        _userImageProfile.value = userImageProfile // Debe ser _userImageProfile
    }


    fun clearUserId() {
        sharedPreferences.edit().remove("idUser").apply() // Eliminar el valor de "idUser"
        _idUser.value = 0  // Resetear el valor de _idUser
    }


}
