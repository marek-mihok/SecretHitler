package sk.ferinaf.secrethitler.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_vote_results.view.*
import kotlinx.android.synthetic.main.item_settings_item.view.*
import sk.ferinaf.secrethitler.R

class SettingsItem @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var switch: Switch? = null

    init {
        inflate(context, R.layout.item_settings_item, this)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.SettingsItem)

        val primary = attributes.getString(R.styleable.SettingsItem_SettingsItem_primaryText)
        val secondary = attributes.getString(R.styleable.SettingsItem_SettingsItem_secondaryText)

        val primaryTextView = findViewById<TextView>(R.id.settings_item_primaryText)
        val secondaryTextView = findViewById<TextView>(R.id.settings_item_secondaryText)

        if (secondary ?: "" != "") {
            secondaryTextView?.visibility = View.VISIBLE
        }

        switch = findViewById(R.id.settings_item_switch)

        primaryTextView?.text = primary
        secondaryTextView?.text = secondary

        attributes.recycle()
    }

}