package com.example.statistico.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.statistico.R
import com.example.statistico.StatisticoApplication
import com.example.statistico.models.Team
import com.example.statistico.ui.TeamActivity
import com.squareup.picasso.Picasso


class TeamsRecyclerAdapter(private val itemList: MutableList<Team>) :
    RecyclerView.Adapter<TeamsRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_teams, parent, false)
        )
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        holder.layout_main.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.scale)
        super.onViewAttachedToWindow(holder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = itemList[position]
        holder.layout_main.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.scale)

        holder.layout.setOnLongClickListener {
            val url = "https://en.wikipedia.org/wiki/" + holder.name.text.toString()
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            holder.itemView.context.startActivity(i)
            true
        }

        holder.name.text = data.name
        holder.tournament.text = data.tournament
        holder.rank.text = "#" + data.rank.toString()
        if (data.rank == 1) {
            holder.rank.background = AppCompatResources.getDrawable(StatisticoApplication.ApplicationContext, R.drawable.bg_circle_yellow)
        } else if (data.rank == 2) {
            holder.rank.background = AppCompatResources.getDrawable(StatisticoApplication.ApplicationContext, R.drawable.bg_circle_blue)
        } else {
            holder.rank.background = AppCompatResources.getDrawable(StatisticoApplication.ApplicationContext, R.drawable.bg_default)
        }

        if (getTournamentIMG(data.tournament) == "") {
            holder.tournament_img.setImageDrawable(AppCompatResources.getDrawable(StatisticoApplication.ApplicationContext, R.drawable.ic_launcher_foreground))
        } else {
            Picasso.get().load(getTournamentIMG(data.tournament)).into(holder.tournament_img)
        }

        if (position % 2 == 0) {
            holder.layout.background = AppCompatResources.getDrawable(StatisticoApplication.ApplicationContext, R.drawable.bg_team_dark)
        } else {
            holder.layout.background = AppCompatResources.getDrawable(StatisticoApplication.ApplicationContext, R.drawable.bg_team_light)
        }

        holder.team_details.setOnClickListener {
            val intent = Intent(StatisticoApplication.ApplicationContext, TeamActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("rating", data.rating)
            intent.putExtra("league", data.tournament)
            intent.putExtra("rank", data.rank)
            intent.putExtra("team", data.name)
            intent.putExtra("image", data.image)
            StatisticoApplication.ApplicationContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val name: TextView = item.findViewById(R.id.tV_team_name)
        val tournament: TextView = item.findViewById(R.id.tV_team_tournament)
        val rank: TextView = item.findViewById(R.id.tV_rank)
        val tournament_img: ImageView = item.findViewById(R.id.iV_team_tournament)
        val layout: ConstraintLayout = item.findViewById(R.id.layout_team)
        val layout_main: ConstraintLayout = item.findViewById(R.id.layout_team_main)
        val team_details: ImageView = item.findViewById(R.id.img_team_details)
    }

    fun dataAdded(subject: MutableList<Team>) {
        val difference = subject.find { !itemList.contains(it) }

        if (difference != null) {
            itemList.add(difference)
            notifyItemInserted(itemCount - 1)
        }
    }

    fun getDataList(): MutableList<Team> {
        return itemList
    }

    private fun getTournamentIMG(tournament: String?): String {
        return when (tournament) {
            "Premier League" -> {
                "https://image.similarpng.com/very-thumbnail/2020/06/United-Kingdom-flag-icon-on-transparent-background-PNG.png"
            }
            "Bundesliga" -> {
                "https://image.similarpng.com/thumbnail/2020/06/Germany-flag-icon-on-transparent-background-PNG.png"
            }
            "LaLiga" -> {
                "https://image.similarpng.com/very-thumbnail/2020/06/Spain-flag-icon-on-transparent-background-PNG.png"
            }
            "Ligue 1" -> {
                "https://image.similarpng.com/very-thumbnail/2020/06/France-flag-icon-on-transparent-background-PNG.png"
            }
            "Serie A" -> {
                "https://image.similarpng.com/very-thumbnail/2020/06/Portugal-flag-icon-on-transparent-background-PNG.png"
            }
            else -> {
                ""
            }
        }
    }
}