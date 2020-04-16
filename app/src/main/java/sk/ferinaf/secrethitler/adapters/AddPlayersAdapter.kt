package sk.ferinaf.secrethitler.adapters

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.common.SavedPlayers
import sk.ferinaf.secrethitler.common.asColor

class AddPlayersAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    /**
     * View holder for player name item
     */
    class PlayerVH(inflater: LayoutInflater, parent: ViewGroup) :
            RecyclerView.ViewHolder(inflater.inflate(R.layout.item_player_list, parent, false)) {

        private var mUserTextView: TextView? = null
        var cardView: CardView? = null

        init {
            mUserTextView = itemView.findViewById(R.id.item_player_textView)
            cardView = itemView.findViewById(R.id.item_player_cardView)
        }

        fun bind(name: String, onClick: ()->Unit) {
            mUserTextView?.text = name
            itemView.setOnClickListener {
                onClick()
            }
        }
    }

    /**
     * View holder for add player item
     */
    class AddPlayerListItem(inflater: LayoutInflater, parent: ViewGroup) :
            RecyclerView.ViewHolder(inflater.inflate(R.layout.item_add_player, parent, false)) {
        private var mAddButton: Button? = null

        init {
            mAddButton = itemView.findViewById(R.id.add_player_button)
        }

        fun bind(onClick: ()->Unit) {
            mAddButton?.setOnClickListener {
                onClick()
            }
        }
    }

    companion object {
        const val ITEM = 0
        const val ADD_ITEM = 1
    }

    private var sp: SharedPreferences? = null
    private var userList = SavedPlayers.players
    var onItemSelected: (position: Int) -> Unit = {}
    var onAddPlayerSelected: ()->Unit = {}

    fun updateList() {
        userList = SavedPlayers.players
        notifyDataSetChanged()
    }

    fun saveCurrentList() {
        SavedPlayers.relpaceList(userList)
    }

    fun swipe(origin: Int, target: Int) {
        val tmp = userList[origin]
        userList[origin] = userList[target]
        userList[target] = tmp
        notifyItemMoved(origin, target)
    }

    override fun getItemViewType(position: Int): Int {
        if (userList.size < 10) {
            return if (position < userList.size) ITEM else ADD_ITEM
        }
        return ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == ITEM) PlayerVH(inflater, parent) else AddPlayerListItem(inflater, parent)
    }

    override fun getItemCount(): Int {
        return if (userList.size < 10) userList.size + 1 else userList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PlayerVH) {
            holder.bind(userList[position]) { onItemSelected(position) }
        } else if (holder is AddPlayerListItem) {
            holder.bind(onAddPlayerSelected)
        }
    }

}