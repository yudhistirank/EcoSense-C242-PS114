package com.example.ecosense.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import com.example.ecosense.R
import com.example.ecosense.auth.LoginActivity

class SettingsFragment : Fragment() {

    private lateinit var switchDarkMode: SwitchCompat
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var btnLogout: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        switchDarkMode = view.findViewById(R.id.switchDarkMode)
        sharedPreferences = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)
        btnLogout = view.findViewById(R.id.btnLogout)

        // Set the current mode based on the saved preference
        val isDarkMode = sharedPreferences.getBoolean("isDarkMode", false)
        switchDarkMode.isChecked = isDarkMode
        setDarkMode(isDarkMode)

        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            // Hanya ubah mode tanpa navigasi
            setDarkMode(isChecked)
        }

        btnLogout.setOnClickListener {
            logout()
        }

        return view
    }

    private fun setDarkMode(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            switchDarkMode.setTextColor(resources.getColor(R.color.white, requireContext().theme))
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            switchDarkMode.setTextColor(resources.getColor(R.color.black, requireContext().theme))
        }

        val editor = sharedPreferences.edit()
        editor.putBoolean("isDarkMode", isDarkMode)
        editor.apply()
    }

    private fun logout() {
        // Remove session data from SharedPreferences
        val editor = sharedPreferences.edit()
        editor.remove("email") // Remove email from SharedPreferences
        editor.apply()

        // Navigate to LoginActivity after logout
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish() // Close the current activity
    }
}

