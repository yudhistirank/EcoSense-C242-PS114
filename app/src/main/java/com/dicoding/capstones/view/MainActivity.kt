package com.dicoding.capstones.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.capstones.R
import com.dicoding.capstones.fragment.HistoryFragment
import com.dicoding.capstones.fragment.HomeFragment
import com.dicoding.capstones.fragment.NewsFragment
import com.dicoding.capstones.fragment.SettingsFragment
import com.dicoding.capstones.viewModel.MainViewModel
import com.dicoding.capstones.viewModel.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(this)
        )[MainViewModel::class.java]

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    openFragment(HomeFragment())
                    true
                }
                R.id.nav_news -> {
                    openFragment(NewsFragment())
                    true
                }
                R.id.nav_history -> {
                    openFragment(HistoryFragment())
                    true
                }
                R.id.nav_settings -> {
                    openFragment(SettingsFragment())
                    true
                }
                else -> false
            }
        }

        // Memulai dengan fragmen Home
        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.nav_home
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .addToBackStack(null)
            .commit()
    }
}
