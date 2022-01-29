package com.example.statistico.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.statistico.R
import com.example.statistico.models.Player
import com.squareup.picasso.Picasso

class PlayersRecyclerAdapter(private val itemList: MutableList<Player>) :
    RecyclerView.Adapter<PlayersRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_player, parent, false)
        )
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        holder.layout.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.rotate)
        super.onViewAttachedToWindow(holder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = itemList[position]
        holder.layout.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.rotate)

        holder.name.text = data.name
        holder.age.text = data.age.toString()
        holder.team.text = data.team
        holder.position.text = data.position

        if (data.img == "") {
            holder.image.setImageResource(R.drawable.unknown_flag)
        } else {
            Picasso.get().load(data.img).into(holder.image)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val name: TextView = item.findViewById(R.id.tV_player_name)
        val age: TextView = item.findViewById(R.id.tV_player_age)
        val team: TextView = item.findViewById(R.id.tV_player_team)
        val position: TextView = item.findViewById(R.id.tV_player_position)
        val image: ImageView = item.findViewById(R.id.iV_player_img)
        val layout: ConstraintLayout = item.findViewById(R.id.layout_player_main)
    }

    fun dataAdded(subject: MutableList<Player>) {
        val difference = subject.find { !itemList.contains(it) }

        if (difference != null) {
            itemList.add(difference)
            notifyItemInserted(itemCount - 1)
        }
    }

    fun getDataList(): MutableList<Player> {
        return itemList
    }
}