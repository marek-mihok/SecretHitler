package sk.ferinaf.secrethitler.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.common.PlayersInfo

class PlayersTable @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val linearLayout by lazy { findViewById<LinearLayout>(R.id.playersResults_main_linerLayout) }
    private val fascistLayout by lazy { findViewById<LinearLayout>(R.id.playersResults_fascist_linearLayout) }
    private val titleTextView by lazy { findViewById<TextView>(R.id.playersResults_title_textView) }

    init {
        View.inflate(context, R.layout.widget_players_result_table, this)
    }

    fun setFascists() {
        titleTextView.setText(R.string.fascists)
        fascistLayout?.visibility = View.VISIBLE

        val hitler = PlayersInfo.getHitler()
        hitler?.let {
            val hitlerTextView = findViewById<TextView>(R.id.playersResults_hitler_textView)
            hitlerTextView.text = it.name
        }

        val fascists = PlayersInfo.getFascists()
        fascists.forEach {
            addName(it.name)
        }
    }

    fun setLiberals() {
        titleTextView?.setText(R.string.liberals)
        fascistLayout?.visibility = View.GONE
        val liberals = PlayersInfo.getLiberals()
        liberals.forEach {
            addName(it.name)
        }
    }

    fun addName(name: String) {
        val text = TextView(context, null, R.attr.playerListTextStyle)
        text.text = name
        linearLayout.addView(text)
    }

}