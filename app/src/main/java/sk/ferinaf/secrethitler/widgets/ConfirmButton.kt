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

    private var timer: CountDownTimer? = null
    private var confirmed = false

    private var mListener: ProgressListener? = null
    var listener: ProgressListener?
        get() = mListener
        set(value) {
            mListener = value
        }

    private var mDuration = 1500L
    var duration: Long
        get() = mDuration
        set(value) {
            mDuration = value
            progressButtonAnimation.duration = mDuration

            timer = object: CountDownTimer(duration, duration) {
                override fun onFinish() {
                    // On confirm
                    confirmed = true
                    context.vibrate()
                    listener?.onConfirm()
                }

                override fun onTick(millisUntilFinished: Long) { }
            }
        }

    private var started = false

    /**
     * INITIALISATION
     */
    init {
        View.inflate(context, R.layout.widget_confirm_button, this)

        textView = findViewById(R.id.confirmButton_TextView)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ConfirmButton)
        val colorId = attributes.getColor(R.styleable.ConfirmButton_ConfirmButton_TextColor, 0)
        textView?.setTextColor(colorId)
        attributes.recycle()

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

        timer = object: CountDownTimer(duration, duration) {
            override fun onFinish() {
                // On confirm
                confirmed = true
                context.vibrate()
                listener?.onConfirm()
            }

            override fun onTick(millisUntilFinished: Long) { }
        }

        widget_confirm_button?.setOnTouchListener { v, event ->
            if (!interactionEnabled) return@setOnTouchListener true

            v?.performClick()
            if (event.pointerCount > 1) return@setOnTouchListener true

            when(event?.action){
                MotionEvent.ACTION_DOWN -> {
                    started = true

                    // On start
                    confirmed = false
                    listener?.onActionDown()
                    context.vibrate()
                    confirmButton_progressView?.alpha = 1F
                    confirmButton_progressView?.startAnimation(progressButtonAnimation)

                    timer?.start()
                    listener?.onStart()
                }

                MotionEvent.ACTION_UP -> {
                    if (!started) return@setOnTouchListener true

                    timer?.cancel()
                    listener?.onActionUp()

                    if (shouldFade) {
                        confirmButton_progressView?.clearAnimation()
                        confirmButton_progressView?.startAnimation(fadeOutAnim)
                    }

                    if (confirmed) {
                        // On finish
                        listener?.onFinish()
                    } else {
                        // On cancel
                        listener?.onCancel()
                    }
                    started = false
                }

            }
            true
        }

    }
}
