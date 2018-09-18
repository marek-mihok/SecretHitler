package io.g3m.secrethitler

import android.animation.Animator
import android.animation.AnimatorInflater
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
        val fadeInAnim = AnimationUtils.loadAnimation(this,R.anim.fade_in)
        val showBackAnim = AnimationUtils.loadAnimation(this,R.anim.show_back_in_middle)
        val progressButtonAnimation = AnimationUtils.loadAnimation(this,R.anim.translate_x)

        val shiftAnim = AnimationUtils.loadAnimation(this,R.anim.shift_back)

        val envelopeFrontAnim = AnimatorInflater.loadAnimator(this,R.animator.rotate)
        envelopeFrontAnim.setTarget(envelopeFront)


        progressView.alpha = 0F
        progressView.visibility = View.VISIBLE
        progressButtonAnimation.duration = 1500

        val distance = (160 * 18.1).toInt()
        envelopeFront.cameraDistance = distance * density
        envelopeBack.cameraDistance = distance * density

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            revealButtonTextView.letterSpacing = 0.05F
        }

        fadeOutAnim.setAnimationListener(object: Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                progressView.alpha = 0F
                if (shouldHideButton) {
                    revealButton.visibility = View.INVISIBLE
                }
            }

            override fun onAnimationStart(animation: Animation?) {
                progressView.alpha = 1F
            }

        })

        progressButtonAnimation.setAnimationListener(object: Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) { }

            override fun onAnimationEnd(animation: Animation?) {
                if (buttonClicked) {
                    shouldHideButton = true
                    this@RevealIdentity.vibrate()
                } else {
                    shouldHideButton = false
                }
            }

            override fun onAnimationStart(animation: Animation?) { }
        })

        revealButton.setOnTouchListener { v, event ->
            v?.performClick()

            if (event.pointerCount < 2) {
                when(event?.action){
                    MotionEvent.ACTION_DOWN -> {
                        Log.d("debug","Pressed")
                        revealButton.cardElevation = round(2 * density).toFloat()
                        this@RevealIdentity.vibrate()

                        progressView.alpha = 1F
                        buttonClicked = true
                        progressView.startAnimation(progressButtonAnimation)

                        envelopeBack.startAnimation(showBackAnim)
                        envelopeFront.startAnimation(shiftAnim)
                        envelopeFrontAnim.start()

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

                            envelopeFrontAnim.start()
                            envelopeFrontAnim.cancel()
                            envelopeBack.clearAnimation()
                            envelopeFront.clearAnimation()
                            //envelopeFront.startAnimation(fadeInAnim)
                        }
                    }
                }
            }
            true
        }
    }
}
