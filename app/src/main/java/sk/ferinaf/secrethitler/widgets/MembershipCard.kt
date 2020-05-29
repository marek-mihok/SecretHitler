package sk.ferinaf.secrethitler.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.common.asString

class MembershipCard @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val fascist by lazy { R.string.fascist.asString() }
    private val liberal by lazy { R.string.liberal.asString() }

    var imageView: ImageView? = null
    var textView: TextView? = null

    private var mParty: PolicyCard.PolicyType? = null
    var party: PolicyCard.PolicyType?
        get() {
            return mParty
        }
        set(value) {
            mParty = value
            when (value) {
                PolicyCard.PolicyType.FASCIST -> {
                    imageView?.setImageResource(R.drawable.img_role_fascist)
                    textView?.text = fascist
                }
                PolicyCard.PolicyType.LIBERAL -> {
                    imageView?.setImageResource(R.drawable.img_role_liberal)
                    textView?.text = liberal
                }
                null -> {}
            }
        }

    init {
        View.inflate(context, R.layout.item_party_membership_card, this)

        imageView = findViewById(R.id.membership_party_image)
        textView = findViewById(R.id.membership_party_text)
    }

}