package sk.ferinaf.secrethitler.widgets

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.fragment_policy.*
import kotlinx.android.synthetic.main.widget_policy_card.view.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.common.asColor

@Suppress("MemberVisibilityCanBePrivate")
class PolicyCard @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val redColor by lazy { R.color.redDark.asColor() }
    private val blueColor by lazy { R.color.blueDark.asColor() }
    private val blackColor by lazy { R.color.secretGray.asColor() }

    var anim1: ObjectAnimator? = null
    var anim2: ObjectAnimator? = null

    var onFlipped: () -> Unit = {}

    enum class PolicyType {
        FASCIST, LIBERAL
    }

    var imageView: ImageView? = null

    private var mBack = false
    var back: Boolean
        get() = mBack
        set(value) {
            mBack = value
            setImage(mType, mBack)
        }

    private var mType: PolicyType? = null
    var type: PolicyType?
        get() {
            return mType
        }
        set(value) {
            mType = value
            setImage(mType, mBack)
        }

    var policyElevation: Float?
        get() = widget_policy_cardView?.cardElevation
        set(value) {
            widget_policy_cardView?.cardElevation = value ?: 0f
        }

    init {
        View.inflate(context, R.layout.widget_policy_card, this)
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.PolicyCard)
        imageView = findViewById(R.id.widget_policy_image)

        // Set card type
        when (attributes.getInt(R.styleable.PolicyCard_PolicyCard_type, -1)) {
            0 -> type = PolicyType.FASCIST
            1 -> type = PolicyType.LIBERAL
        }

        setPolicyCornerRadius(attributes.getDimension(R.styleable.PolicyCard_PolicyCard_cornerRadius, 0f))
        policyElevation = attributes.getDimension(R.styleable.PolicyCard_PolicyCard_elevation, 0f)

        attributes.recycle()
    }

    private fun setImage(type: PolicyType?, back: Boolean) {
        if (back) {
            imageView?.setImageResource(R.drawable.img_policy_back)
            imageView?.setColorFilter(blackColor)
        } else {
            when (type) {
                PolicyType.FASCIST -> {
                    imageView?.setImageResource(R.drawable.img_policy_fascist)
                    imageView?.setColorFilter(redColor)
                }
                PolicyType.LIBERAL -> {
                    imageView?.setImageResource(R.drawable.img_policy_liberal)
                    imageView?.setColorFilter(blueColor)
                }
            }
        }
    }

    fun setPolicyCornerRadius(radius: Float) {
        widget_policy_cardView?.radius = radius
    }


    /**
     * ROTATE ANIMATION
     */
    fun setupAnimation(animationDuration: Long) {
        anim1 = ObjectAnimator.ofFloat(this, "rotationY", 0f, 90f)
        anim1?.duration = animationDuration / 2
        anim1?.interpolator = LinearInterpolator()

        anim2 = ObjectAnimator.ofFloat(this, "rotationY", -90f, 0f)
        anim2?.duration = animationDuration / 2
        anim2?.interpolator = LinearInterpolator()

        var shouldContinue = true

        anim1?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                if (shouldContinue) {
                    back = !back
                    anim2?.start()
                }
            }

            override fun onAnimationCancel(animation: Animator?) {
                shouldContinue = false
            }

            override fun onAnimationStart(animation: Animator?) {
                shouldContinue = true
            }
        })

        var animSuccess = true
        anim2?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                if (animSuccess) onFlipped()
            }

            override fun onAnimationStart(animation: Animator?) {
                animSuccess = true
            }

            override fun onAnimationCancel(animation: Animator?) {
                animSuccess = false
                back = !back
            }
        })
    }

}