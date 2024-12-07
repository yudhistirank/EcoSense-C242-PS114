package com.example.ecosense.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ecosense.database.AppDatabase
import com.example.ecosense.models.User
import com.example.ecosense.repository.UserRepository
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository
    private val _registrationStatus = MutableLiveData<Boolean>()
    val registrationStatus: LiveData<Boolean> get() = _registrationStatus

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        userRepository = UserRepository(userDao)
    }

    // Method to register the user
    fun registerUser(username: String, email: String, password: String) {
        viewModelScope.launch {
            val userExists = userRepository.checkUserExists(email)
            if (userExists == null) {
                // User doesn't exist, proceed with registration
                userRepository.registerUser(username, email, password)
                _registrationStatus.postValue(true) // Success
            } else {
                // User already exists
                _registrationStatus.postValue(false) // Registration failed
            }
        }
    }

    // Method to login the user
    fun loginUser(email: String, password: String): LiveData<User?> {
        val result = MutableLiveData<User?>()
        viewModelScope.launch {
            val user = userRepository.loginUser(email, password)
            result.postValue(user) // Post the result of login
        }
        return result
    }
}

