package sk.ferinaf.secrethitler.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import kotlinx.android.synthetic.main.item_game_navigation.view.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.common.asColor

class GameNavigationItem @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val backgroundColor by lazy { R.color.backgroundYellow.asColor() }
    private val foregroundColor by lazy { R.color.secretGray.asColor() }

    var cardView: CardView? = null
    var textView: TextView? = null
    var imageView: ImageView? = null

    init {
        View.inflate(context, R.layout.item_game_navigation, this)

        cardView = item_gameNavigation_cardView
        textView = item_gameNavigation_text
        imageView = item_gameNavigation_icon

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.GameNavigationItem)
        val image = attributes.getResourceId(R.styleable.GameNavigationItem_GameNavigationItem_image, 0)
        val title = attributes.getString(R.styleable.GameNavigationItem_GameNavigationItem_title)

        item_gameNavigation_icon?.setImageResource(image)
        item_gameNavigation_text?.text = title

        attributes.recycle()
    }

    fun setItemSelected(selected: Boolean) {
        val density = resources.displayMetrics.density
        if (selected) {
            cardView?.cardElevation = 2 * density
            cardView?.setCardBackgroundColor(foregroundColor)
            textView?.setTextColor(backgroundColor)
            imageView?.setColorFilter(backgroundColor)
        } else {
            cardView?.cardElevation = 4 * density
            cardView?.setCardBackgroundColor(backgroundColor)
            textView?.setTextColor(foregroundColor)
            imageView?.setColorFilter(foregroundColor)
        }
    }
}