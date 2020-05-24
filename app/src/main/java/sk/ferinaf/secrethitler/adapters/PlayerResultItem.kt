package sk.ferinaf.secrethitler.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.models.Player

class PlayerResultItem(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_player_results, parent, false)) {

    var textView: TextView? = null

    init {
        textView = itemView.findViewById(R.id.item_playerResults_textView)
    }

    fun bind(player: Player?) {
        textView?.text = player?.name
    }

}