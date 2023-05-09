package com.example.mystoryapps.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapps.ViewModelFactory
import com.example.mystoryapps.auth.AutentikasiActivity
import com.example.mystoryapps.databinding.ActivitySplashBinding
import com.example.mystoryapps.main.MainActivity
import com.example.mystoryapps.preferences.UserPreferences
import com.example.mystoryapps.preferences.UserPreferences.Companion.preferenceDefaultValue
import com.example.mystoryapps.preferences.dataStore
import com.example.mystoryapps.viewModel.AutentikasiViewModel
import java.util.*
import kotlin.concurrent.schedule

class SplashActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val pref = UserPreferences.getInstance(dataStore)
        val autentikasiViewModel = ViewModelProvider(this, ViewModelFactory(pref))[AutentikasiViewModel::class.java]

        autentikasiViewModel.getUserPreferences("Token").observe(this) { token ->
            Log.e("AutentikasiActivity", "Token changed to $token")

            if (token == preferenceDefaultValue) {
                startActivity(Intent(this@SplashActivity, AutentikasiActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }
        }
    }

}