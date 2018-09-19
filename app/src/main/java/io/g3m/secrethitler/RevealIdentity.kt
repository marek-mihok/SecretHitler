package io.g3m.secrethitler

import android.animation.AnimatorInflater
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_reveal_identity.*
import java.lang.Math.round

class RevealIdentity : FullScreenActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reveal_identity)

        var shouldContinue = true

        var playerNames = ArrayList<String>()
        var playerRoles = ArrayList<Int>()

        @Suppress("UNCHECKED_CAST")
        if (intent?.extras?.get("player_names") != null) {
            playerNames = intent?.extras?.get("player_names") as ArrayList<String>
            setPlayerName(playerNames.first())
            playerNames.removeAt(0)
            if (playerNames.isEmpty()) {
                shouldContinue = false
            }
        }

        @Suppress("UNCHECKED_CAST")
        if (intent?.extras?.get("player_roles") != null) {
            playerRoles = intent?.extras?.get("player_roles") as ArrayList<Int>
            setPlayerRole(playerRoles[0])
            playerRoles.removeAt(0)
            if (playerRoles.isEmpty()) {
                shouldContinue = false
            }
        }

        var revealed = false
        var reallyRevealed = false
        var confirmed = false

        val density = resources.displayMetrics.density

        val roleToCenterAnim = AnimationUtils.loadAnimation(this,R.anim.shift_to_center)


        val fadeOutAnim = AnimationUtils.loadAnimation(this,R.anim.fade_out)

        val showBackAnim = AnimationUtils.loadAnimation(this,R.anim.show_back_in_middle)
        val progressButtonAnimation = AnimationUtils.loadAnimation(this,R.anim.translate_x)

        val hideEnvelopeAnim = AnimationUtils.loadAnimation(this,R.anim.hide_envelope_shift_left)

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
            nameTextView.letterSpacing = 0.05F
        }


        fadeOutAnim.setAnimationListener(object: Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                progressView.alpha = 0F
                if (revealed) {
                    fakeCard.setBackgroundColor(resources.getColor(R.color.backgroundYellow))
                }
            }

            override fun onAnimationStart(animation: Animation?) {
                progressView.alpha = 1F
            }

        })


        val timer = object: CountDownTimer(1500,1500) {
            override fun onFinish() {
                revealed = true
                revealButtonTextView.text = "RELEASE BUTTON"
                this@RevealIdentity.vibrate()
            }

            override fun onTick(millisUntilFinished: Long) { }
        }


        val timerAfterReveal = object: CountDownTimer(1500,1500) {
            override fun onFinish() {
                confirmed = true
                // Toast.makeText(this@RevealIdentity,"Confirmed",Toast.LENGTH_SHORT).show()
                revealButtonTextView.text = "RELEASE BUTTON"
                this@RevealIdentity.vibrate()
            }

            override fun onTick(millisUntilFinished: Long) { }
        }


        revealButton.setOnTouchListener { v, event ->

            v?.performClick()

            if (event.pointerCount < 2) {

                when(event?.action){
                    MotionEvent.ACTION_DOWN -> {

                        revealButton.cardElevation = round(2 * density).toFloat()
                        this@RevealIdentity.vibrate()
                        progressView.alpha = 1F
                        progressView.startAnimation(progressButtonAnimation)

                        if (!revealed) {
                            timer.start()
                            envelopeBack.startAnimation(showBackAnim)
                            fakeCard.startAnimation(showBackAnim)
                            envelopeFront.startAnimation(shiftAnim)
                            envelopeFrontAnim.start()
                        } else {
                            timerAfterReveal.start()
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        timer.cancel()

                        revealButton.cardElevation = round(4 * density).toFloat()

                        progressView.clearAnimation()
                        progressView.startAnimation(fadeOutAnim)

                        if (revealed && !reallyRevealed) {
                            fakeCard.setBackgroundColor(resources.getColor(R.color.backgroundYellow))

                            shiftToFinalPosition(envelopeFakeBack, envelopeFront)
                            shiftToFinalPosition(secretRoleImage, envelopeFront)

                            secretRoleImage.startAnimation(roleToCenterAnim)

                            val timerAnim = object: CountDownTimer(750,750) {
                                override fun onFinish() {
                                    envelopeFakeBack.startAnimation(hideEnvelopeAnim)
                                    envelopeFront.startAnimation(hideEnvelopeAnim)
                                    reallyRevealed = true
                                }

                                override fun onTick(millisUntilFinished: Long) { }
                            }
                            timerAnim.start()
                            revealButtonTextView.text = "HOLD TO CONFIRM"

                        } else if (!reallyRevealed) {
                            fakeCard.clearAnimation()
                            envelopeFrontAnim.start()
                            envelopeFrontAnim.cancel()
                            envelopeBack.clearAnimation()
                            envelopeFront.clearAnimation()
                            envelopeFakeBack.clearAnimation()
                            secretRoleImage.clearAnimation()
                        } else if (reallyRevealed) {
                            timerAfterReveal.cancel()
                        }

                        if (confirmed) {
                            if (shouldContinue) {
                                // Toast.makeText(this@RevealIdentity,"Time for another player",Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, RevealIdentity::class.java)
                                val b = Bundle()
                                b.putStringArrayList("player_names", playerNames)
                                b.putIntegerArrayList("player_roles", playerRoles)
                                intent.putExtras(b)
                                startActivity(intent)
                                overridePendingTransition(R.anim.activity_slide_in, R.anim.activity_slide_out)
                                finish()
                            } else {
                                // TU TREBA HODIT ODKAZ NA ZACATIE HRY
                                finish()
                            }
                        }
                    }
                }
            }
            true
        }
    }

    private fun shiftToFinalPosition(view: View, shiftedView: View){
        view.translationX = -0.95F * shiftedView.width.toFloat() * 0.85F
        view.scaleX = 0.85F
        view.scaleY = 0.85F
        view.visibility = View.VISIBLE
    }


    @SuppressLint("SetTextI18n")
    private fun setPlayerName(pName: String) {
        val name = pName.toUpperCase()
        if (name.last().toString() == "S"){
            smallNameTextView.text = "$name'S"
        } else {
            smallNameTextView.text = name + "S"
        }
        nameTextView.text = name
    }

    private fun setPlayerRole(num: Int){
        when (num) {
            0 -> secretRoleImage.setImageResource(R.drawable.img_liberal_role)
            1 -> secretRoleImage.setImageResource(R.drawable.img_fascist_role)
            2 -> secretRoleImage.setImageResource(R.drawable.img_hitla_role)
        }
    }
}
