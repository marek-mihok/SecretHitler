package sk.ferinaf.secrethitler.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.widget_no_button.view.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.common.touchInside

class NoButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    val button: Button? = widget_neinButton

    init {
        View.inflate(context, R.layout.widget_no_button, this)
    }

    fun setOnClick(onClick: ()->Unit){
        widget_neinButton?.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    setCardElevation(2f)
                }
                MotionEvent.ACTION_UP -> {
                    setCardElevation(4f)
                    if(touchInside(v, event)) {
                        onClick()
                    }
                }
            }
            true }
    }

    private fun setCardElevation(elevation: Float) {
        widget_neinButton_cardView?.cardElevation = (elevation * context.resources.displayMetrics.density)
    }

}