package sk.ferinaf.secrethitler.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sk.ferinaf.secrethitler.common.PlayersInfo

class PlayersListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PlayersListItem(inflater, parent)
    }

    override fun getItemCount(): Int {
        return PlayersInfo.getPlayersCount()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? PlayersListItem)?.bind(PlayersInfo.getPlayer(position))
    }

}