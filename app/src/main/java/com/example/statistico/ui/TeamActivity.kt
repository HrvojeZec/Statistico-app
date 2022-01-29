package com.example.statistico.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.statistico.R
import com.example.statistico.adapters.PlayersRecyclerAdapter
import com.example.statistico.data.Firebase
import com.example.statistico.databinding.ActivityTeamBinding
import com.squareup.picasso.Picasso


class TeamActivity : AppCompatActivity() {
    lateinit var teamBinding: ActivityTeamBinding

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var playerAdapter: PlayersRecyclerAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        teamBinding = ActivityTeamBinding.inflate(layoutInflater)
        setContentView(teamBinding.root)

        supportActionBar?.hide()
        initializeUI()
    }

    private fun initializeUI() {
        playerAdapter = PlayersRecyclerAdapter(mutableListOf())
        teamBinding.rVPlayers.adapter = playerAdapter
        teamBinding.rVPlayers.layoutManager = LinearLayoutManager(this)
        Firebase.fetchPlayers(intent.getStringExtra("team"))

        Picasso.get().load(intent.getStringExtra("image")).into(object : com.squareup.picasso.Target {
            override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
                teamBinding.iVTeam.setImageResource(R.drawable.unknown_flag)
                teamBinding.iVTeam.scaleType = ImageView.ScaleType.FIT_XY
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                teamBinding.iVTeam.setImageResource(R.drawable.unknown_flag)
                teamBinding.iVTeam.scaleType = ImageView.ScaleType.FIT_XY
            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                teamBinding.iVTeam.setImageBitmap(bitmap)
                teamBinding.iVTeam.scaleType = ImageView.ScaleType.CENTER
            }
        })

        teamBinding.tVName.text = intent.getStringExtra("team")
        teamBinding.tVTournament.text = intent.getStringExtra("league")
        teamBinding.tVRank.text = "Rank: " + intent.getIntExtra("rank", 0).toString()
        teamBinding.tVRating.text = "Rating: " + intent.getDoubleExtra("rating", 0.0).toString()

        teamBinding.iVBack.setOnClickListener {
            finish()
        }
    }
}