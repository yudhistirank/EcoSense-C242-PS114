package com.example.ecosense.repository

import com.example.ecosense.database.UserDao
import com.example.ecosense.models.User

class UserRepository(private val userDao: UserDao) {

    // Method to check if the user exists
    suspend fun checkUserExists(email: String): User? {
        return userDao.getUserByEmail(email) // Correct function call
    }

    // Method to register the user
    suspend fun registerUser(username: String, email: String, password: String) {
        val user = User(username = username, email = email, password = password)
        userDao.insert(user)
    }

    // Method to login the user
    suspend fun loginUser(email: String, password: String): User? {
        return userDao.loginUser(email, password)
    }
}
