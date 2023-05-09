package com.example.mystoryapps.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapps.R
import com.example.mystoryapps.ViewModelFactory
import com.example.mystoryapps.adapter.LoadingAdapter
import com.example.mystoryapps.adapter.StoryAdapter
import com.example.mystoryapps.auth.AutentikasiActivity
import com.example.mystoryapps.camera.CameraActivity
import com.example.mystoryapps.databinding.ActivityMainBinding
import com.example.mystoryapps.map.MapTrackingActivity
import com.example.mystoryapps.preferences.UserPreferences
import com.example.mystoryapps.preferences.dataStore
import com.example.mystoryapps.viewModel.AutentikasiViewModel
import com.example.mystoryapps.viewModel.MainViewFactory
import com.example.mystoryapps.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels {
        MainViewFactory(this)
    }
    private lateinit var autentikasiViewModel: AutentikasiViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserPreferences.getInstance(this.dataStore)
        autentikasiViewModel = ViewModelProvider(this, ViewModelFactory(pref))[AutentikasiViewModel::class.java]

        val layoutManager = LinearLayoutManager(this)
        binding.rvStories.layoutManager = layoutManager

        val adapter = StoryAdapter()
        binding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingAdapter{
                adapter.retry()
            }
        )

        mainViewModel.story.observe(this) { pagingData ->
            adapter.submitData(lifecycle, pagingData)
        }

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStories.addItemDecoration(itemDecoration)

        binding.camera.setOnClickListener {
            val intent = Intent(this@MainActivity, CameraActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.logout -> {
                autentikasiViewModel.clearUserPreferences()
                val intent = Intent(this, AutentikasiActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.map ->{
                val intent = Intent(this, MapTrackingActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finishAffinity()
    }

}