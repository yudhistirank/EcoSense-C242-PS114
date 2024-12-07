package com.example.ecosense.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ecosense.databinding.ActivitySplashBinding
import com.example.ecosense.MainActivity
import com.example.ecosense.auth.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE)

        binding.logo.alpha = 0f
        binding.logo.animate().setDuration(1500).alpha(1f).withEndAction {
            checkLoginStatus()
        }
    }

    // Cek status login pengguna
    private fun checkLoginStatus() {
        val email = sharedPreferences.getString("email", null)

        // Jika email ada (berarti pengguna sudah login), arahkan ke MainActivity
        if (email != null) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            // Jika tidak ada email (belum login), arahkan ke LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
        }

        finish() // Tutup SplashActivity setelah proses selesai
    }
}
