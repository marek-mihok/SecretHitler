package sk.ferinaf.secrethitler.widgets

import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.cardview.widget.CardView
import kotlinx.android.synthetic.main.widget_confirm_button.view.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.common.vibrate

class ConfirmButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    interface ProgressListener {
        fun onStart()
        fun onCancel()
        fun onConfirm()
        fun onFinish()

        fun onActionDown()
        fun onActionUp()
    }

    var textView: TextView? = null
    var interactionEnabled = true
    var shouldFade = true

    private val fadeOutAnim by lazy { AnimationUtils.loadAnimation(context, R.anim.fade_out) }
    private val progressButtonAnimation by lazy { AnimationUtils.loadAnimation(context, R.anim.translate_x) }

    init {
        View.inflate(context, R.layout.widget_confirm_button, this)

        textView = findViewById(R.id.confirmButton_TextView)

//        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ShHeader)
//        val colorId = attributes.getColor(R.styleable.ShHeader_ShHeader_text_color, 0)
//        headerWidget_title_textView?.setTextColor(colorId)
//        headerWidget_title_textView?.text = attributes.getString(R.styleable.ShHeader_ShHeader_text_title)
//        attributes.recycle()
    }

    fun setProgressListener(duration: Long = 1500, listener: ProgressListener) {
        var confirmed = false

        progressButtonAnimation.duration = duration
        confirmButton_progressView?.alpha = 0F
        confirmButton_progressView?.visibility = View.VISIBLE

        fadeOutAnim.setAnimationListener(object: Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                confirmButton_progressView?.alpha = 0F
            }

            override fun onAnimationStart(animation: Animation?) {
                confirmButton_progressView?.alpha = 1F
            }

        })

        val timer = object: CountDownTimer(duration, duration) {
            override fun onFinish() {
                // On confirm
                confirmed = true
                context.vibrate()
                listener.onConfirm()
            }

            override fun onTick(millisUntilFinished: Long) { }
        }

        widget_confirm_button?.setOnTouchListener { v, event ->
            if (!interactionEnabled) return@setOnTouchListener true

            v?.performClick()
            if (event.pointerCount > 1) return@setOnTouchListener true

            when(event?.action){
                MotionEvent.ACTION_DOWN -> {
                    // On start
                    confirmed = false
                    listener.onActionDown()
                    context.vibrate()
                    confirmButton_progressView?.alpha = 1F
                    confirmButton_progressView?.startAnimation(progressButtonAnimation)

                    timer.start()
                    listener.onStart()
                }

                MotionEvent.ACTION_UP -> {
                    timer.cancel()
                    listener.onActionUp()

                    if (shouldFade) {
                        confirmButton_progressView?.clearAnimation()
                        confirmButton_progressView?.startAnimation(fadeOutAnim)
                    }

                    if (confirmed) {
                        // On finish
                        listener.onFinish()
                    } else {
                        // On cancel
                        listener.onCancel()
                    }
                }

            }
            true
        }
    }
}
