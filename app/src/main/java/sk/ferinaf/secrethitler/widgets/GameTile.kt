package sk.ferinaf.secrethitler.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.common.asColor
import sk.ferinaf.secrethitler.common.asString

class GameTile @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    enum class TileEvent {
        PEEK_POLICY, INVESTIGATE, SPECIAL_ELECTION, EXECUTION, E_AND_VETO
    }

    var description: TextView? = null
    var image: ImageView? = null

    private var tileCardView: CardView? = null

    private var mIsFascist = false
    var isFascist: Boolean
        get() { return mIsFascist }
        set(value) {
            mIsFascist = value
            description?.visibility = if (mIsFascist) View.VISIBLE else View.GONE
        }

    private var mType: TileEvent? = null
    var type: TileEvent?
    get() = mType
    set(value) {
        mType = value
        when (value) {
            TileEvent.PEEK_POLICY -> {
                image?.setImageResource(R.drawable.ic_policy_peek)
                description?.text = R.string.peek_policy_event.asString()
            }
            TileEvent.INVESTIGATE -> {
                image?.setImageResource(R.drawable.ic_investigate_2)
                description?.text = R.string.investigate_event.asString()
            }
            TileEvent.SPECIAL_ELECTION -> {
                image?.setImageResource(R.drawable.ic_special_election)
                description?.text = R.string.special_election_event.asString()
            }
            TileEvent.EXECUTION -> {
                image?.setImageResource(R.drawable.ic_execute_40dp)
                description?.text = R.string.execution_event.asString()
            }
            TileEvent.E_AND_VETO -> {
                image?.setImageResource(R.drawable.ic_execute_40dp)
                description?.text = R.string.execution_veto_event.asString()
            }
            null -> {}
        }
    }

    private var mHighlited: Boolean = false
    var highlighted: Boolean
        get() = mHighlited
        set(value) {
            mHighlited = value
            val color = if (isFascist) R.color.redMedium.asColor() else R.color.blueLight.asColor()
            if (value) {
                tileCardView?.setCardBackgroundColor(color)
                image?.setColorFilter(R.color.secretWhite.asColor())
            } else {
                tileCardView?.setCardBackgroundColor(R.color.secretWhite.asColor())
                image?.setColorFilter(R.color.secretGray.asColor())
            }
        }

    init {
        View.inflate(context, R.layout.widget_game_tile, this)

        description = findViewById(R.id.gamePlan_tileDescription_textView)
        image = findViewById(R.id.gamePlan_tileIcon_imageView)

        tileCardView = findViewById(R.id.gamePlan_tileCircle_cardView)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.GameTile)

        val isFirst = attributes.getBoolean(R.styleable.GameTile_GameTile_isFirst, false)
        val isLast = attributes.getBoolean(R.styleable.GameTile_GameTile_isLast, false)
        isFascist = attributes.getBoolean(R.styleable.GameTile_GameTile_isFascist, false)

        if (!isFascist && !isLast) {
            image?.visibility = View.GONE
        }

        if (isFirst) {
            val bottomLine = this.findViewById<FrameLayout>(R.id.gamePlan_bottomLine_frameLayout)
            bottomLine?.visibility = View.GONE
        }

        if (isLast) {
            val topLine = this.findViewById<FrameLayout>(R.id.gamePlan_topLine_frameLayout)
            topLine?.visibility = View.GONE
            if (isFascist) {
                image?.setImageResource(R.drawable.ic_skull)
            } else {
                image?.setImageResource(R.drawable.ic_dove)
            }
        }

        attributes.recycle()
    }

}