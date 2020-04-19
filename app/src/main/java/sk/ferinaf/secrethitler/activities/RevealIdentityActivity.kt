package sk.ferinaf.secrethitler.activities

import android.animation.AnimatorInflater
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.animation.*
import sk.ferinaf.secrethitler.R
import kotlinx.android.synthetic.main.activity_reveal_identity.*
import kotlinx.android.synthetic.main.activity_reveal_identity.otherFascistsView
import kotlinx.android.synthetic.main.item_other_fascists.*
import sk.ferinaf.secrethitler.common.*
import sk.ferinaf.secrethitler.widgets.ConfirmButton
import java.util.*

@SuppressLint("SetTextI18n")
class RevealIdentityActivity : FullScreenActivity() {

    override var askToGetBack = true

    private var playerIndex: Int = 0
    private var revealedFirstStage = false
    private var revealedSecondStage = false
    private var confirmed = false
    private var revealTimer: CountDownTimer? = null
    private var confirmTimer: CountDownTimer? = null

    private val showBackAnim by lazy { AnimationUtils.loadAnimation(this, R.anim.reveal_identitiy_show_back_in_middle) }
    private val shiftAnim by lazy { AnimationUtils.loadAnimation(this, R.anim.reveal_identitiy_shift_back) }
    private val envelopeFrontAnim by lazy { AnimatorInflater.loadAnimator(this, R.animator.rotate) }
    private val roleToCenterAnim by lazy { AnimationUtils.loadAnimation(this, R.anim.reveal_identitiy_shift_to_center) }
    private val hideEnvelopeAnim by lazy { AnimationUtils.loadAnimation(this, R.anim.reveal_identitiy_hide_envelope_shift_left) }

    private val backgroundYellow by lazy { R.color.backgroundYellow.asColor() }
    private val density by lazy { resources.displayMetrics.density }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reveal_identity)

        // Needed to use same activity to show different players
        playerIndex = intent.getIntExtra("playerIndex", 0)
        setPlayerName(PlayersInfo.getPlayerName(playerIndex))
        setPlayerRole(PlayersInfo.getPlayerIdentity(playerIndex))

        // SETUP VIEWS
        this.setOtherFascistsView(playerIndex)

        val distance = (160 * 18.1).toInt()
        envelopeFront.cameraDistance = distance * density
        envelopeBack.cameraDistance = distance * density

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            revealButton?.textView?.letterSpacing = 0.05F
        }

        // SET ANIMATIONS
        envelopeFrontAnim.setTarget(envelopeFront)

        val animationDuration = 1500L
        val helpTextFadeInAni = CustomAnimations.textFadeIn
        revealTimer = object: CountDownTimer(animationDuration, animationDuration) {
            override fun onFinish() {
                revealedFirstStage = true
                revealPlayerInfoTextView.startAnimation(helpTextFadeInAni)
                revealButton?.textView?.text = "RELEASE BUTTON" // TODO: Resource
                this@RevealIdentityActivity.vibrate()
            }

            override fun onTick(millisUntilFinished: Long) { }
        }

        confirmTimer = object: CountDownTimer(animationDuration, animationDuration) {
            override fun onFinish() {
                confirmed = true
                revealButton?.textView?.text = "RELEASE BUTTON"
                this@RevealIdentityActivity.vibrate()
            }

            override fun onTick(millisUntilFinished: Long) { }
        }

        // SET PROGRESS LISTENER
        revealButton.setProgressListener(animationDuration, object : ConfirmButton.ProgressListener {
            override fun onStart() {
                onConfirmStart()
            }

            override fun onCancel() {
                onConfirmCancel()
            }

            override fun onConfirm() { }

            override fun onFinish() {
                onConfirmFinish()
            }

            override fun onActionDown() {
                revealButton?.cardElevation = kotlin.math.round(2 * density)
            }

            override fun onActionUp() {
                revealButton?.cardElevation = kotlin.math.round(4 * density)
            }
        })
    }

    private fun onConfirmStart() {
        if (!revealedFirstStage) {
            revealTimer?.start()
            envelopeBack.startAnimation(showBackAnim)
            fakeCard.startAnimation(showBackAnim)
            envelopeFront.startAnimation(shiftAnim)
            envelopeFrontAnim.start()
        } else {
            confirmTimer?.start()
        }
    }

    private fun onConfirmCancel() {
        if (!revealedFirstStage) {
            revealTimer?.cancel()
            fakeCard?.clearAnimation()
            envelopeBack?.clearAnimation()
            envelopeFront?.clearAnimation()
            envelopeFakeBack?.clearAnimation()
            secretRoleImage?.clearAnimation()
            envelopeFrontAnim.start()
            envelopeFrontAnim.cancel()
        } else {
            confirmTimer?.cancel()
        }
    }

    private fun onConfirmFinish() {
        if (revealedFirstStage && !revealedSecondStage) {
            revealButton?.textView?.text = "HOLD TO CONFIRM"
            revealPlayerInfoTextView.clearAnimation()
            fakeCard.setBackgroundColor(backgroundYellow)

            shiftToFinalPosition(envelopeFakeBack, envelopeFront)
            shiftToFinalPosition(secretRoleImage, envelopeFront)

            secretRoleImage.startAnimation(roleToCenterAnim)

            val timerShowRole = object: CountDownTimer(750,750) {
                override fun onFinish() {
                    envelopeFakeBack.startAnimation(hideEnvelopeAnim)
                    envelopeFront.startAnimation(hideEnvelopeAnim)
                    revealedSecondStage = true
                    showOtherFascistsView()
                }

                override fun onTick(millisUntilFinished: Long) { }
            }
            timerShowRole.start()
        }

        if (revealedSecondStage) {
            revealButton?.interactionEnabled = false
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


    // Used to shift envelope to final position
    private fun shiftToFinalPosition(view: View, shiftedView: View){
        view.translationX = -0.95F * shiftedView.width.toFloat() * 0.85F
        view.scaleX = 0.85F
        view.scaleY = 0.85F
        view.visibility = View.VISIBLE
    }


    // Add s on the end of name
    private fun setPlayerName(pName: String) {
        val name = pName.toUpperCase(Locale.ROOT)
        smallNameTextView.text = "$name'S"
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
