package sk.ferinaf.secrethitler.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.widget_black_button.view.*
import kotlinx.android.synthetic.main.widget_white_button.view.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.common.touchInside

class CardButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var button: Button? = null

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CardButton)
        val type = attributes.getInt(R.styleable.CardButton_CardButton_type, 0)
        val primaryText = attributes.getString(R.styleable.CardButton_CardButton_primaryText)
        val secondaryText = attributes.getString(R.styleable.CardButton_CardButton_secondaryText)

        if (type == 0) {
            View.inflate(context, R.layout.widget_white_button, this)
            whiteButton_primaryText?.text = primaryText
            whiteButton_secondaryText?.text = secondaryText
            button = widget_whiteButton_button
        } else {
            View.inflate(context, R.layout.widget_black_button, this)
            blackButton_primaryText?.text = primaryText
            blackButton_secondaryText?.text = secondaryText
            button = widget_blackButton_button
        }

        attributes.recycle()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setOnClick(onClick: ()->Unit) {
        button?.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    setCardElevation(2f)
                }
                MotionEvent.ACTION_UP -> {
                    setCardElevation(4f)
                    if(v.touchInside(event)) {
                        onClick()
                    }
                }
            }
            true }
    }

    private fun setCardElevation(elevation: Float) {
        widget_blackButton_cardView?.cardElevation = (elevation * context.resources.displayMetrics.density)
    }

}