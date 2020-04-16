package sk.ferinaf.secrethitler.activities

import android.animation.AnimatorInflater
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import androidx.core.content.ContextCompat
import android.view.MotionEvent
import android.view.View
import android.view.animation.*
import sk.ferinaf.secrethitler.R
import kotlinx.android.synthetic.main.activity_reveal_identity.*
import sk.ferinaf.secrethitler.common.CustomAnimations
import sk.ferinaf.secrethitler.common.FullScreenActivity
import sk.ferinaf.secrethitler.common.PlayersInfo
import sk.ferinaf.secrethitler.common.vibrate
import java.util.*

@SuppressLint("SetTextI18n")
class RevealIdentityActivity : FullScreenActivity() {

    override var askToGetBack = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reveal_identity)

        // Needed to use same activity to show different players
        val playerIndex = intent.getIntExtra("playerIndex", 0)
        setPlayerName(PlayersInfo.getPlayerName(playerIndex))
        setPlayerRole(PlayersInfo.getPlayerIdentity(playerIndex))

        this.setOtherFascistsView(playerIndex)

        var revealed = false
        var reallyRevealed = false
        var confirmed = false

        val density = resources.displayMetrics.density

        val fadeOutAnim = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val helpTextFadeInAni = CustomAnimations.textFadeIn

        val roleToCenterAnim = AnimationUtils.loadAnimation(this, R.anim.reveal_identitiy_shift_to_center)
        val showBackAnim = AnimationUtils.loadAnimation(this, R.anim.reveal_identitiy_show_back_in_middle)
        val hideEnvelopeAnim = AnimationUtils.loadAnimation(this, R.anim.reveal_identitiy_hide_envelope_shift_left)
        val shiftAnim = AnimationUtils.loadAnimation(this, R.anim.reveal_identitiy_shift_back)

        val progressButtonAnimation = AnimationUtils.loadAnimation(this, R.anim.translate_x)
        progressButtonAnimation.duration = 1500

        val envelopeFrontAnim = AnimatorInflater.loadAnimator(this, R.animator.rotate)
        envelopeFrontAnim.setTarget(envelopeFront)

        progressView.alpha = 0F
        progressView.visibility = View.VISIBLE

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
                if (revealed) {
                    val backgroundYellow = ContextCompat.getColor(this@RevealIdentityActivity, R.color.backgroundYellow)
                    fakeCard.setBackgroundColor(backgroundYellow)
                }
            }

            override fun onAnimationStart(animation: Animation?) {
                progressView.alpha = 1F
            }

        })



        val timer = object: CountDownTimer(1500,1500) {
            override fun onFinish() {
                revealed = true
                revealPlayerInfoTextView.startAnimation(helpTextFadeInAni)
                revealButtonTextView.text = "RELEASE BUTTON"
                this@RevealIdentityActivity.vibrate()
            }

            override fun onTick(millisUntilFinished: Long) { }
        }


        val timerAfterReveal = object: CountDownTimer(1500,1500) {
            override fun onFinish() {
                confirmed = true
                revealButtonTextView.text = "RELEASE BUTTON"
                this@RevealIdentityActivity.vibrate()
            }

            override fun onTick(millisUntilFinished: Long) { }
        }


        revealButton.setOnTouchListener { v, event ->

            v?.performClick()

            if (event.pointerCount < 2) {

                when(event?.action){
                    MotionEvent.ACTION_DOWN -> {

                        revealButton.cardElevation = kotlin.math.round(2 * density)
                        this@RevealIdentityActivity.vibrate()
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

                        revealButton.cardElevation = kotlin.math.round(4 * density)

                        revealPlayerInfoTextView.clearAnimation()
                        progressView.clearAnimation()
                        progressView.startAnimation(fadeOutAnim)

                        if (revealed && !reallyRevealed) {
                            val backgroundYellow = ContextCompat.getColor(this@RevealIdentityActivity, R.color.backgroundYellow)
                            fakeCard.setBackgroundColor(backgroundYellow)

                            shiftToFinalPosition(envelopeFakeBack, envelopeFront)
                            shiftToFinalPosition(secretRoleImage, envelopeFront)

                            secretRoleImage.startAnimation(roleToCenterAnim)

                            // Hack pre postupne spustanie animacii lebo poli pototo vrstvy pri animacii
                            val timerAnim = object: CountDownTimer(750,750) {
                                override fun onFinish() {
                                    envelopeFakeBack.startAnimation(hideEnvelopeAnim)
                                    envelopeFront.startAnimation(hideEnvelopeAnim)
                                    reallyRevealed = true
                                    showOtherFascistsView()
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
                            if (playerIndex < PlayersInfo.getPlayersCount() - 1) {
                                val intent = Intent(this, RevealIdentityActivity::class.java)
                                intent.putExtra("playerIndex", playerIndex + 1)
                                startActivity(intent)
                                overridePendingTransition(R.anim.activity_slide_in, R.anim.activity_slide_out)
                                finish()
                            } else {
                                // TODO: TU TREBA HODIT ODKAZ NA ZACATIE HRY
                                finish()
                            }
                        }
                    }
                }
            }
            true
        }
    }


    // Used to shift envelope to final position
    private fun shiftToFinalPosition(view: View, shiftedView: View){
        view.translationX = -0.95F * shiftedView.width.toFloat() * 0.85F
        view.scaleX = 0.85F
        view.scaleY = 0.85F
        view.visibility = View.VISIBLE
    }


    // Add s on the end of name ... with little grammar
    private fun setPlayerName(pName: String) {
        val name = pName.toUpperCase(Locale.ROOT)
        if (name.last().toString() == "S"){
            smallNameTextView.text = "$name'S"
        } else {
            smallNameTextView.text = name + "S"
        }
        revealIdentity_title?.setTitle(name)
    }


    // Set appropriate image for player role
    private fun setPlayerRole(num: Int){
        when (num) {
            0 -> secretRoleImage.setImageResource(R.drawable.img_liberal_role)
            1 -> secretRoleImage.setImageResource(R.drawable.img_fascist_role)
            2 -> secretRoleImage.setImageResource(R.drawable.img_hitla_role)
        }
    }


    // Fill view with names of other fascists if needed
    private fun setOtherFascistsView(playerIndex: Int) {
        val otherFascists = PlayersInfo.getRevealedFascists(playerIndex)

        if (otherFascists[0] == "") {
            hitler_row.visibility = View.GONE
        }

        if (otherFascists[1] == "") {
            fascist_one_row.visibility = View.GONE
        }

        if (otherFascists[2] == "") {
            fascist_two_row.visibility = View.GONE
        }

        hitlerNameTextView.text = otherFascists[0]
        facsistOneTextView.text = otherFascists[1]
        facsistTwoTextView.text = otherFascists[2]


        if (otherFascists[0] == "" && otherFascists[1] == "" && otherFascists[2] == "") {
            otherFascistsView.visibility = View.GONE
        } else {
            otherFascistsView.visibility = View.VISIBLE
        }
    }

    // Shows view with other fascists
    fun showOtherFascistsView() {
        val anim = AnimationUtils.loadAnimation(this, R.anim.reveal_identitiy_shift_up)
        otherFascistsView.startAnimation(anim)
    }
}
