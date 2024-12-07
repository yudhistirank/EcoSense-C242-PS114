package com.example.ecosense.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ecosense.R
import com.example.ecosense.viewmodels.AuthViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        val etUsername: EditText = findViewById(R.id.etUsername)
        val etEmail: EditText = findViewById(R.id.etEmail)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val btnRegister: Button = findViewById(R.id.btnRegister)
        val linkToLogin: TextView = findViewById(R.id.linkToLogin)

        // Observe registration status
        authViewModel.registrationStatus.observe(this, Observer { isRegistered ->
            if (isRegistered) {
                // Registration successful
                Toast.makeText(this, "Registration successful! Please login.", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish() // Close the register activity
            } else {
                // User already exists
                Toast.makeText(this, "User already exists. Please login.", Toast.LENGTH_SHORT).show()
            }
        })

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            // Register the user via ViewModel
            authViewModel.registerUser(username, email, password)
        }

        // Link to LoginActivity
        linkToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish() // Close register activity
        }
    }
}




