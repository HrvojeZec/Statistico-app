package com.example.statistico.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.statistico.R
import com.example.statistico.adapters.TeamsRecyclerAdapter
import com.example.statistico.data.Firebase
import com.example.statistico.databinding.ActivityMainBinding
import com.example.statistico.models.Team

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var teamAdapter: TeamsRecyclerAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        initializeUI()
    }

    private fun initializeUI() {
        teamAdapter = TeamsRecyclerAdapter(mutableListOf())
        mainBinding.rVTeams.adapter = teamAdapter
        mainBinding.rVTeams.layoutManager = LinearLayoutManager(this)
        Firebase.fetchTeams()

        mainBinding.btnRating.setOnClickListener {
            teamAdapter = TeamsRecyclerAdapter(sortByRating(teamAdapter.getDataList()))
            mainBinding.rVTeams.adapter = teamAdapter
        }
        mainBinding.btnTournament.setOnClickListener {
            teamAdapter = TeamsRecyclerAdapter(sortByTournament(teamAdapter.getDataList()))
            mainBinding.rVTeams.adapter = teamAdapter
        }
        mainBinding.btnTeam.setOnClickListener {
            teamAdapter = TeamsRecyclerAdapter(sortByName(teamAdapter.getDataList()))
            mainBinding.rVTeams.adapter = teamAdapter
        }
    }

    private fun sortByName(items: MutableList<Team>): MutableList<Team> {
        items.sortBy { it.name }
        return items
    }

    private fun sortByRating(items: MutableList<Team>): MutableList<Team> {
        items.sortBy { it.rank }
        return items
    }

    private fun sortByTournament(items: MutableList<Team>): MutableList<Team> {
        items.sortBy { it.tournament }
        return items
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == R.id.LogoutOption) {
            val logoutIntent = Intent(this, StartActivity::class.java)
            startActivity(logoutIntent)
            Toast.makeText(this, "You have successfully logged out", Toast.LENGTH_SHORT).show()
            finish()
        }

        return true
    }
}