package sk.ferinaf.secrethitler.adapters

import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import sk.ferinaf.secrethitler.App
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.common.SettingsCategory
import sk.ferinaf.secrethitler.common.asColor
import sk.ferinaf.secrethitler.common.getSettingsFor
import sk.ferinaf.secrethitler.models.GovernmentRole
import sk.ferinaf.secrethitler.models.Player

class PlayersListItem(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_player_cell, parent, false)) {

    private val darkColor by lazy { R.color.addPlayer_item.asColor() }
    private val orange by lazy { R.color.backgroundYellow.asColor() }
    private val notHitlerAllowed by lazy { App.context.getSettingsFor(SettingsCategory.NOT_HITLER) }

    var roleImage: ImageView? = null
    var title: TextView? = null
    var notHitlerImage: ImageView? = null
    private var cardView: CardView? = null

    init {
        roleImage = itemView.findViewById(R.id.item_player_roleImage)
        title = itemView.findViewById(R.id.item_player_playerName)
        notHitlerImage = itemView.findViewById(R.id.item_player_notHitler)
        cardView = itemView.findViewById(R.id.item_playerCell_cardView)
    }

    fun bind(player: Player, selected: Boolean, onClick: ()->Unit = {}) {
        title?.text = player.name

        itemView.setOnClickListener {
            onClick()
        }

        when (player.governmentRole) {
            GovernmentRole.PRESIDENT -> {
                roleImage?.visibility = View.VISIBLE
                roleImage?.setImageResource(R.drawable.img_president)
            }
            GovernmentRole.CHANCELLOR -> {
                roleImage?.visibility = View.VISIBLE
                roleImage?.setImageResource(R.drawable.img_chancellor)
            }
            null -> roleImage?.visibility = View.GONE
        }

        notHitlerImage?.visibility = if (player.notHitlerReveal && notHitlerAllowed) View.VISIBLE else View.GONE
    }

    fun select(selected: Boolean) {
        if (selected) {
            cardView?.setCardBackgroundColor(darkColor)
            roleImage?.setColorFilter(orange)
            notHitlerImage?.setColorFilter(orange)
            title?.setTextColor(orange)
        } else {
            cardView?.setCardBackgroundColor(orange)
            roleImage?.setColorFilter(darkColor)
            notHitlerImage?.setColorFilter(darkColor)
            title?.setTextColor(darkColor)
        }
    }

}