package sk.ferinaf.secrethitler.widgets

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.widget_header.view.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.common.asColor

class ShHeader @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.widget_header, this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            headerWidget_title_textView?.letterSpacing = 0.05F
        }

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ShHeader)

        val colorId = attributes.getColor(R.styleable.ShHeader_ShHeader_text_color, 0)
        headerWidget_title_textView?.setTextColor(colorId)

        headerWidget_title_textView?.text = attributes.getString(R.styleable.ShHeader_ShHeader_text_title)

        val colorBack = attributes.getColor(R.styleable.ShHeader_ShHeader_color, 0)
        shHeader_backView?.setBackgroundColor(colorBack)

        attributes.recycle()
    }

    fun setTitle(title: String) {
        headerWidget_title_textView?.text = title
    }

}