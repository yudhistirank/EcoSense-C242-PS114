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

        // Inisialisasi View dan SharedPreferences
        switchDarkMode = view.findViewById(R.id.switchDarkMode)
        btnLogout = view.findViewById(R.id.btnLogout)
        sharedPreferences = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)

        // Set status awal Switch berdasarkan SharedPreferences
        val isDarkMode = sharedPreferences.getBoolean("isDarkMode", false)
        switchDarkMode.isChecked = isDarkMode
        setDarkMode(isDarkMode) // Terapkan tema sesuai status

        // Listener untuk Switch Dark Mode
        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            setDarkMode(isChecked) // Terapkan tema saat status berubah
        }

        // Listener untuk Tombol Logout
        btnLogout.setOnClickListener {
            logout() // Panggil fungsi logout
        }

        return view
    }

    // Fungsi untuk mengatur tema aplikasi
    private fun setDarkMode(isDarkMode: Boolean) {
        // Terapkan tema sesuai pilihan
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            switchDarkMode.setTextColor(resources.getColor(R.color.white, requireContext().theme))
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            switchDarkMode.setTextColor(resources.getColor(R.color.black, requireContext().theme))
        }

        // Simpan status tema ke SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putBoolean("isDarkMode", isDarkMode)
        editor.apply()
    }

    // Fungsi untuk logout
    private fun logout() {
        // Hapus data login dari SharedPreferences
        val editor = sharedPreferences.edit()
        editor.remove("email")
        editor.apply()

        // Navigasi ke LoginActivity
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish() // Tutup aktivitas utama
    }
}
