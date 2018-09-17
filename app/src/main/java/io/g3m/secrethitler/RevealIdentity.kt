package io.g3m.secrethitler

import android.content.Context
import android.opengl.Visibility
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Vibrator
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
        val vibrationDuration: Long = 25
        val vb = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        val fadeOutAnim = AnimationUtils.loadAnimation(this,R.anim.fade_out)
        val animation = AnimationUtils.loadAnimation(this,R.anim.translate_x)

        val sp = getSharedPreferences("Settings", 0)
        val vibrate = sp.getBoolean("vibrations", false)

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
                    vibrate(vibrationDuration, vb, vibrate)
                }
            }

            override fun onAnimationStart(animation: Animation?) { }
        })

        revealButton.setOnTouchListener(object: View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                if (v != null) {
                    v.performClick()
                }

                when(event?.action){
                    MotionEvent.ACTION_DOWN -> {
                        Log.d("debug","Pressed")
                        revealButton.cardElevation = round(2 * density).toFloat()
                        vibrate(vibrationDuration, vb, vibrate)

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
                return true
            }
        })
    }
}
