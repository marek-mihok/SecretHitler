package io.g3m.secrethitler

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_reveal_identity.*
import java.lang.Math.round

class RevealIdentity : FullScreenActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reveal_identity)

        var buttonClicked = false
        var shouldHideButton = false

        val density = resources.displayMetrics.density

        val fadeOutAnim = AnimationUtils.loadAnimation(this,R.anim.fade_out)
        val animation = AnimationUtils.loadAnimation(this,R.anim.translate_x)

        progressView.alpha = 0F
        progressView.visibility = View.VISIBLE
        animation.duration = 2000

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            revealButtonTextView.letterSpacing = 0.05F
        }

        fadeOutAnim.setAnimationListener(object: Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                progressView.alpha = 0F
                if (shouldHideButton) {
                    revealButton.visibility = View.GONE
                }
            }

            override fun onAnimationStart(animation: Animation?) {
                progressView.alpha = 1F
            }

        })

        animation.setAnimationListener(object: Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) { }

            override fun onAnimationEnd(animation: Animation?) {
                if (buttonClicked) {
                    shouldHideButton = true
                    this@RevealIdentity.vibrate()
                }
            }

            override fun onAnimationStart(animation: Animation?) { }
        })

        revealButton.setOnTouchListener { v, event ->
            v?.performClick()

            when(event?.action){
                MotionEvent.ACTION_DOWN -> {
                    Log.d("debug","Pressed")
                    revealButton.cardElevation = round(2 * density).toFloat()
                    this@RevealIdentity.vibrate()

                    progressView.alpha = 1F
                    buttonClicked = true
                    progressView.startAnimation(animation)

                }
                MotionEvent.ACTION_UP -> {
                    Log.d("debug", "Released")
                    revealButton.cardElevation = round(4 * density).toFloat()

                    buttonClicked = false
                    progressView.clearAnimation()
                    if (shouldHideButton) {
                        revealButton.startAnimation(fadeOutAnim)
                    } else {
                        progressView.startAnimation(fadeOutAnim)
                    }
                }
            }
            true
        }
    }
}
