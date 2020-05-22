package sk.ferinaf.secrethitler.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sk.ferinaf.secrethitler.common.PlayersInfo
import sk.ferinaf.secrethitler.models.Player

class SelectPlayersAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onPlayerSelected: (Player) -> Unit = {}

    var players: ArrayList<Player> = arrayListOf()
    private var selected = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PlayersListItem(inflater, parent)
    }

    override fun getItemCount(): Int {
        return players.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val player = players[position]
        val item = (holder as? PlayersListItem)
        item?.select(selected == position)
        item?.bind(player, selected == position) {
            onPlayerSelected(player)
            selected = position
            notifyDataSetChanged()
        }
    }

}