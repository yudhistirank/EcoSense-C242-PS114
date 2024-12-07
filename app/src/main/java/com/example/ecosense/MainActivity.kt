package com.example.ecosense

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.ecosense.auth.LoginActivity
import com.example.ecosense.databinding.ActivityMainBinding
import com.example.ecosense.fragments.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cek status login pengguna
        val sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", null)

        if (email == null) {
            // Jika email tidak ada (belum login), arahkan ke LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
            finish() // Tutup MainActivity
        }

        // Cek status Dark Mode dari SharedPreferences dan terapkan
        val isDarkMode = sharedPreferences.getBoolean("isDarkMode", false)
        setDarkMode(isDarkMode)

        replaceFragment(HomeFragment())

        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> replaceFragment(HomeFragment())
                R.id.nav_history -> replaceFragment(HistoryFragment())
                R.id.nav_info -> replaceFragment(InfoFragment())
                R.id.nav_settings -> {
                    // Pastikan hanya mengganti fragment jika belum di SettingsFragment
                    if (supportFragmentManager.findFragmentByTag(SettingsFragment::class.java.simpleName) == null) {
                        replaceFragment(SettingsFragment())
                    }
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun setDarkMode(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}
