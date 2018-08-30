package io.g3m.secrethitler

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.player_list_item.view.*

class PlayersAdapter(val items : ArrayList<String>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.player_list_item, parent, false))
    }
//    https://stackoverflow.com/questions/49086840/class-mainadapter-is-not-abstract-and-does-not-implement-abstract-base-class-m

    // Binds each animal in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.tvPlayerType?.text = items.get(position)
    }

    /**
     * Function called to swap dragged items
     */
    fun swapItems(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition..toPosition - 1) {
                items.set(i, items.set(i+1, items.get(i)));
            }
        } else {
            for (i in fromPosition..toPosition + 1) {
                items.set(i, items.set(i-1, items.get(i)));
            }
        }

        notifyItemMoved(fromPosition, toPosition)
    }

}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val tvPlayerType = view.tv_player_type
}