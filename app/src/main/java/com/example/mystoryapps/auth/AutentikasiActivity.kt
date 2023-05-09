package com.example.mystoryapps.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mystoryapps.R
import com.example.mystoryapps.databinding.ActivityAutentikasiBinding

class AutentikasiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAutentikasiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivityAutentikasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val loginFragment = LoginFragment()
        transaction.add(R.id.auth, loginFragment)
        transaction.commit()

    }

}