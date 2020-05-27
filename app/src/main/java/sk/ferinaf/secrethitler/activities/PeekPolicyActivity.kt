package sk.ferinaf.secrethitler.activities

import android.animation.AnimatorSet
import android.os.Bundle
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_peek_policy.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.common.BaseActivity
import sk.ferinaf.secrethitler.common.GameState
import sk.ferinaf.secrethitler.common.PlayersInfo
import sk.ferinaf.secrethitler.common.asString
import sk.ferinaf.secrethitler.widgets.ConfirmButton
import sk.ferinaf.secrethitler.widgets.PolicyCard

class PeekPolicyActivity : BaseActivity() {

    override var fullScreen = true
    override var askToGetBack = true

    override var statusBarColor: Int? = R.color.secretGray

    private val releaseButton by lazy { R.string.release_button.asString() }
    private val holdToConfirm by lazy { R.string.hold_to_confirm.asString() }

    private var animatorSetSmall: AnimatorSet? = null
    private var smallPolicyElevation: Float? = 0f
    private val duration = 1000L

    var card1: PolicyCard? = null
    var card2: PolicyCard? = null
    var card3: PolicyCard? = null

    private var revealed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_peek_policy)

        peek_policy_president_name?.text = PlayersInfo.getPresident()?.name

        val cardsLayout = findViewById<LinearLayout>(R.id.peek_policy_cards_layout)
        card1 = cardsLayout.findViewById(R.id.policy_card_first)
        card2 = cardsLayout.findViewById(R.id.policy_card_second)
        card3 = cardsLayout.findViewById(R.id.policy_card_third)

        card1?.back = true
        card2?.back = true
        card3?.back = true

        val cards = GameState.peekCards()

        card1?.type = cards[0]
        card2?.type = cards[1]
        card3?.type = cards[2]

        setAnimation()

        peek_policy_confirm_button?.duration = duration
        peek_policy_confirm_button?.listener = object : ConfirmButton.ProgressListener {
            override fun onStart() {
                if (!revealed) {
                    card1?.policyElevation = 0f
                    card2?.policyElevation = 0f
                    card3?.policyElevation = 0f
                    animatorSetSmall?.start()
                }
            }

            override fun onCancel() {
                if (!revealed) {
                    card1?.anim2?.cancel()
                    card2?.anim2?.cancel()
                    card3?.anim2?.cancel()
                    animatorSetSmall?.start()
                    animatorSetSmall?.cancel()
                }
            }

            override fun onConfirm() {
                peek_policy_confirm_button?.textView?.text = releaseButton
                // TODO: setResult uspesne odhalene
            }

            override fun onFinish() {
                if (!revealed) {
                    peek_policy_confirm_button?.textView?.text = holdToConfirm
                } else {
                    finish()
                }
                revealed = true
            }
        }
    }


    private fun setCameraDistance() {
        val density = resources.displayMetrics.density
        val distance = (160 * 18.1).toInt()

        card1?.cameraDistance = distance * density
        card2?.cameraDistance = distance * density
        card3?.cameraDistance = distance * density
    }


    private fun setAnimation() {
        setCameraDistance()

        smallPolicyElevation = card1?.policyElevation

        card1?.setupAnimation(duration)
        card2?.setupAnimation(duration)
        card3?.setupAnimation(duration)

        animatorSetSmall = AnimatorSet()
        animatorSetSmall?.playTogether(card1?.anim1, card2?.anim1, card3?.anim1)
        animatorSetSmall?.duration = duration / 2
        card1?.onFlipped = {
            card1?.policyElevation = smallPolicyElevation
            card2?.policyElevation = smallPolicyElevation
            card3?.policyElevation = smallPolicyElevation
        }
    }
}