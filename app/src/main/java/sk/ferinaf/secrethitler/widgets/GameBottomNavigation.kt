package sk.ferinaf.secrethitler.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.widget_game_navigation.view.*
import sk.ferinaf.secrethitler.R

class GameBottomNavigation @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    enum class NavigationItem {
        GAME, POLICY, PLAYERS
    }

    var onSelect: (item: NavigationItem)->Unit = { }

    init {
        View.inflate(context, R.layout.widget_game_navigation, this)

        navigation_gameButton?.cardView?.setOnClickListener { selectItem(NavigationItem.GAME) }
        navigation_policyButton?.cardView?.setOnClickListener { selectItem(NavigationItem.POLICY) }
        navigation_playersButton?.cardView?.setOnClickListener { selectItem(NavigationItem.PLAYERS) }
    }

    private fun setDefault() {
        navigation_gameButton?.setItemSelected(false)
        navigation_policyButton?.setItemSelected(false)
        navigation_playersButton?.setItemSelected(false)
    }

    fun selectItem(item: NavigationItem) {
        setDefault()
        when (item) {
            NavigationItem.GAME -> {
                navigation_gameButton?.setItemSelected(true)
            }
            NavigationItem.POLICY -> {
                navigation_policyButton?.setItemSelected(true)
            }
            NavigationItem.PLAYERS -> {
                navigation_playersButton?.setItemSelected(true)
            }
        }
        onSelect(item)
    }

}