package com.example.goodreceipt

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import com.example.goodreceipt.databinding.ActivityMainBinding
import com.example.goodreceipt.ui.home.HomeFragment
import com.example.goodreceipt.ui.login.LoginFragment
import com.example.goodreceipt.utils.PrefsHelper
import com.example.goodreceipt.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var prefsHelper: PrefsHelper
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = getColor(R.color.accent_blue)
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.fragmentContainer) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, 0)
            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomNavigation) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(v.paddingLeft, v.paddingTop, v.paddingRight, systemBars.bottom)
            insets
        }

        setupBottomNavigation()

        if (savedInstanceState == null) {
            if(prefsHelper.isLogin()){
                HomeFragment.navigateTo(supportFragmentManager)
                    showBottomNavigation(true)
                    setSelectedNavigationItem(R.id.nav_home)
            }else{
                LoginFragment.navigateTo(supportFragmentManager)

            }

        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            showBottomNavigation(true)
            when (item.itemId) {
                R.id.nav_home -> {
                    navigateToHome()
                    true
                }
               R.id.nav_history -> {
                    showToast("History clicked")
                    true
                }
               R.id.nav_stats -> {
                    showToast("Stats clicked")
                    true
                }
                R.id.nav_settings -> {
                    showToast("Settings clicked")
                    true
                }
                else -> false
            }
        }
    }

    private fun navigateToHome() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment !is HomeFragment) {
            HomeFragment.navigateTo(supportFragmentManager)
        }
    }

    fun showBottomNavigation(show: Boolean) {
        if (show) {
            binding.bottomNavigation.visibility = View.VISIBLE
            binding.bottomNavigation.bringToFront()
        } else {
            binding.bottomNavigation.visibility = View.GONE
        }
        
        val bottomPadding = if (show) {
            resources.getDimensionPixelSize(R.dimen.bottom_nav_height)
        } else {
            0
        }
        
        val currentTopPadding = binding.fragmentContainer.paddingTop
        binding.fragmentContainer.setPadding(
            binding.fragmentContainer.paddingLeft,
            currentTopPadding,
            binding.fragmentContainer.paddingRight,
            bottomPadding
        )
    }

    fun setSelectedNavigationItem(itemId: Int) {
        binding.bottomNavigation.selectedItemId = itemId
    }
}