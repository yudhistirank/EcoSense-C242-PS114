package com.example.ecosense.fragments


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import com.example.ecosense.R

class SettingsFragment : Fragment() {

    private lateinit var switchDarkMode: SwitchCompat
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        switchDarkMode = view.findViewById(R.id.switchDarkMode)
        sharedPreferences = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)

        // Set the current mode based on the saved preference
        val isDarkMode = sharedPreferences.getBoolean("isDarkMode", false)
        switchDarkMode.isChecked = isDarkMode
        setDarkMode(isDarkMode)

        // Set the listener for the switch
        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            setDarkMode(isChecked)
        }

        return view
    }

    private fun setDarkMode(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        // Save the preference for dark mode
        val editor = sharedPreferences.edit()
        editor.putBoolean("isDarkMode", isDarkMode)
        editor.apply()
    }
}

