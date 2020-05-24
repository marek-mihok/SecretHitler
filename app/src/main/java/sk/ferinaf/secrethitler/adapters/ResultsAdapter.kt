package sk.ferinaf.secrethitler.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sk.ferinaf.secrethitler.common.PlayersInfo
import sk.ferinaf.secrethitler.models.Player

class ResultsAdapter : RecyclerView.Adapter<PlayerResultItem>() {

    enum class Voted {
        JA, NEIN
    }

    private var players: ArrayList<Player>? = null

    fun setType(voted: Voted) {
        players = when (voted) {
            Voted.JA -> {
                val p = PlayersInfo.players.filter { player ->
                    player.lastVote == true
                }
                ArrayList(p)
            }
            Voted.NEIN -> {
                val p = PlayersInfo.players.filter { player ->
                    player.lastVote == false
                }
                ArrayList(p)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerResultItem {
        val inflater = LayoutInflater.from(parent.context)
        return PlayerResultItem(inflater, parent)
    }

    override fun getItemCount(): Int {
        return players?.size ?: 0
    }

    override fun onBindViewHolder(holder: PlayerResultItem, position: Int) {
        holder.bind(players?.get(position))
    }

}