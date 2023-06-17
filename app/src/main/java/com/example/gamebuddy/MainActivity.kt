package com.example.gamebuddy

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.gamebuddy.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragments_container) as NavHostFragment
        navController = navHostFragment.navController

        setupNavigation()

    }

    private fun setupNavigation() {
        bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setupWithNavController(navController)

    }

    override fun displayProgressBar(isLoading: Boolean) {
        if(isLoading) binding.progressBar.visibility = View.VISIBLE else binding.progressBar.visibility = View.GONE
    }
}