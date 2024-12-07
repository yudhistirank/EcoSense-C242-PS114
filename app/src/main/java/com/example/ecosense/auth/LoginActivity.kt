package com.example.ecosense.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ecosense.MainActivity
import com.example.ecosense.R
import com.example.ecosense.viewmodels.AuthViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        val etEmail: EditText = findViewById(R.id.etEmail)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        val linkToRegister: TextView = findViewById(R.id.linkToRegister)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            authViewModel.loginUser(email, password).observe(this, Observer { user ->
                if (user != null) {
                    // Login berhasil
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()

                    // Simpan status login di SharedPreferences
                    val sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("email", email) // Menyimpan email pengguna
                    editor.apply()

                    // Pindah ke halaman utama
                    startActivity(Intent(this, MainActivity::class.java))
                    finish() // Tutup halaman login
                } else {
                    // Login gagal
                    Toast.makeText(this, "Invalid login. Please check your email and password.", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // Link ke RegisterActivity
        linkToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
