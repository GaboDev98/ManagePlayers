package com.gabodev.manageplayers.model

import android.view.View
import android.widget.Toast
import android.content.Intent
import android.view.ViewGroup
import android.content.Context
import android.view.LayoutInflater
import com.gabodev.manageplayers.R
import com.gabodev.manageplayers.DetailActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.player_item_layout.view.*

internal class PlayerItemAdapter(private val playerList: List<Player>, private val context: Context) : RecyclerView.Adapter<PlayerItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.player_item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(playerList[position])
    }

    override fun getItemCount(): Int {
        return playerList.size
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(player: Player) {
            itemView.player_label.text = (player.first_name + " " + player.last_name)
            itemView.setOnClickListener {
                Toast.makeText(itemView.context, "Name: " + player.first_name + " LastName: " + player.last_name, Toast.LENGTH_LONG).show()
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("id", player.id)
                intent.putExtra("id_player", player.id_player)
                context.startActivity(intent)
            }
        }
    }
}