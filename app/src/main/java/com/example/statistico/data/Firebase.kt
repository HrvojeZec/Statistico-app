package com.example.statistico.data

import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.statistico.StatisticoApplication
import com.example.statistico.models.Player
import com.example.statistico.models.Team
import com.example.statistico.models.User
import com.example.statistico.ui.MainActivity
import com.example.statistico.ui.TeamActivity
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

object Firebase {
    private val dbInstance = FirebaseDatabase.getInstance("https://statistico-621c9-default-rtdb.europe-west1.firebasedatabase.app/")
    private val usersDbReference = dbInstance.getReference("Users")
    private val teamsDbReference = dbInstance.getReference("Teams")
    private val playersDbReference = dbInstance.getReference("Players")

    private fun register(username: String, password: String) {
        val id = usersDbReference.push().key as String
        val user = User(id, username, password)

        usersDbReference.child(id).setValue(user).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(StatisticoApplication.ApplicationContext, "${username}, Welcome to our little community!", Toast.LENGTH_LONG).show()

                val intent = Intent(StatisticoApplication.ApplicationContext, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(StatisticoApplication.ApplicationContext, intent, null)
            } else {
                Toast.makeText(StatisticoApplication.ApplicationContext, "Registration was unsuccessful, please try again or contact support if problem still occurs.", Toast.LENGTH_LONG).show()
                Log.e("Registration Error", it.result.toString())
            }
        }
    }

    fun preRegisterCheck(username: String, password: String) {
        val userListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = snapshot.children
                var isExists = false

                users.forEach {
                    val user = it.getValue<User>()
                    if (user != null) {
                        if (user.username == username) {
                            isExists = true
                            Toast.makeText(StatisticoApplication.ApplicationContext, "User is already registered, please choose a new nickname.", Toast.LENGTH_LONG).show()
                            return@forEach
                        }
                    }
                }

                if (!isExists) {
                    register(username, password)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Database Error", "loadPost:onCancelled", error.toException())
            }
        }

        usersDbReference.addListenerForSingleValueEvent(userListener)
    }

    fun login(username: String, password: String) {
        val userListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = snapshot.children
                var isExists = false

                users.forEach {
                    val user = it.getValue<User>()
                    if (user != null) {
                        if (user.username == username && user.password == password) {
                            isExists = true
                            Toast.makeText(StatisticoApplication.ApplicationContext, "${username}, Welcome to our little community!", Toast.LENGTH_LONG).show()

                            val intent = Intent(StatisticoApplication.ApplicationContext, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(StatisticoApplication.ApplicationContext, intent, null)
                            return@forEach
                        } else if (user.username == username && user.password != password) {
                            isExists = true
                            Toast.makeText(StatisticoApplication.ApplicationContext, "Password is incorrect, please try again!", Toast.LENGTH_LONG).show()
                            return@forEach
                        }
                    }
                }

                if (!isExists) {
                    Toast.makeText(StatisticoApplication.ApplicationContext, "User isn't registered, please register to use application.", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Database Error", "loadPost:onCancelled", error.toException())
            }
        }

        usersDbReference.addListenerForSingleValueEvent(userListener)
    }

    fun pushTeam(name: String, tournament: String, rank: Int, rating: Double, goals: Int, image: String) {
        //Firebase.pushTeam("Manchester City", "Premier League", 1, 1.0, 1, "image_link")
        val team: Team = Team(name, tournament, rank, rating, goals, image)

        teamsDbReference.push().setValue(team).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.e("pushed team to database", team.toString())
            } else {
                Log.e("failed push to database", team.toString())
            }
        }
    }

    fun pushPlayer(name: String, team: String, nationality: String, position: String, age: Int, img: String) {
        //Firebase.pushPlayer("João Cancelo", "Manchester City", "Portugal", "Defender (Left, Right), Midfielder (Right)", 27, "https://d2zywfiolv4f83.cloudfront.net/img/players/128967.jpg")
        val player: Player = Player(name, team, nationality, position, age, img)

        playersDbReference.push().setValue(player).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.e("pushed player to db", player.toString())
            } else {
                Log.e("failed push to database", player.toString())
            }
        }
    }

    fun fetchTeams() {
        val teamsListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val teams = mutableListOf<Team>()

                val teamData = snapshot.value as HashMap<*, *>
                val task = Team(
                    teamData["name"].toString(),
                    teamData["tournament"].toString(),
                    teamData["rank"].toString().toInt(),
                    teamData["rating"].toString().toDouble(),
                    teamData["goals"].toString().toInt(),
                    teamData["image"].toString()
                )

                teams.add(task)
                MainActivity.teamAdapter.dataAdded(teams)
            }

            override fun onCancelled(error: DatabaseError) {}
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
        }

        val query: Query = teamsDbReference.orderByChild("rank")
        query.addChildEventListener(teamsListener as ChildEventListener)
    }

    fun fetchPlayers(mTeam: String?) {
        val playersListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val players = dataSnapshot.children
                val playersList = mutableListOf<Player>()

                players.forEach {
                    val player = it.getValue<Player>()
                    if (player != null && player.team == mTeam) {
                        playersList.add(player)
                        TeamActivity.playerAdapter.dataAdded(playersList)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }

        val query: Query = playersDbReference.orderByChild("team")
        query.addListenerForSingleValueEvent(playersListener)
    }
}