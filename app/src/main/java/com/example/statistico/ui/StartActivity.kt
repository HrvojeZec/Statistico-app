package com.example.statistico.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.statistico.adapters.FragmentAdapter
import com.example.statistico.databinding.ActivityStartBinding
import com.google.android.material.tabs.TabLayoutMediator

class StartActivity : AppCompatActivity() {
    private lateinit var startBinding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startBinding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(startBinding.root)

        supportActionBar?.hide()
        initializeUI()
    }

    private fun initializeUI() {
        val fragmentTitles = arrayOf("Login", "Register")
        val adapter = FragmentAdapter(supportFragmentManager, lifecycle)

        val viewPager = startBinding.vPStart
        val tabLayout = startBinding.tabLayoutStart
        viewPager.adapter = adapter
        viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
        viewPager.isUserInputEnabled = false

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = fragmentTitles[position]
        }.attach()
    }
}