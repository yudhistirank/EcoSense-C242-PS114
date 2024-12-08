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
    private var currentFragmentTag: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", null)

        if (email == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val isDarkMode = sharedPreferences.getBoolean("isDarkMode", false)
        setDarkMode(isDarkMode)

        val lastFragmentTag = savedInstanceState?.getString("currentFragmentTag")
        if (lastFragmentTag != null) {
            when (lastFragmentTag) {
                HomeFragment::class.java.simpleName -> replaceFragment(HomeFragment(), lastFragmentTag)
                HistoryFragment::class.java.simpleName -> replaceFragment(HistoryFragment(), lastFragmentTag)
                InfoFragment::class.java.simpleName -> replaceFragment(InfoFragment(), lastFragmentTag)
                SettingsFragment::class.java.simpleName -> replaceFragment(SettingsFragment(), lastFragmentTag)
            }
        } else {
            replaceFragment(HomeFragment(), HomeFragment::class.java.simpleName)
        }

        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> replaceFragment(HomeFragment(), HomeFragment::class.java.simpleName)
                R.id.nav_history -> replaceFragment(HistoryFragment(), HistoryFragment::class.java.simpleName)
                R.id.nav_info -> replaceFragment(InfoFragment(), InfoFragment::class.java.simpleName)
                R.id.nav_settings -> replaceFragment(SettingsFragment(), SettingsFragment::class.java.simpleName)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, tag)
            .commit()
        currentFragmentTag = tag
    }

    private fun setDarkMode(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("currentFragmentTag", currentFragmentTag)
    }
}
