package sk.ferinaf.secrethitler.fragments

import android.animation.*
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_policy.*
import kotlinx.android.synthetic.main.item_discard_pile.*
import kotlinx.android.synthetic.main.item_policy_cards_small_linear_layout.*
import kotlinx.android.synthetic.main.item_use_veto_banner.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.activities.GameActivity
import sk.ferinaf.secrethitler.common.*
import sk.ferinaf.secrethitler.dialogs.ConfirmDialog
import sk.ferinaf.secrethitler.widgets.ConfirmButton
import sk.ferinaf.secrethitler.widgets.PolicyCard
import java.util.*

class PolicyFragment : Fragment() {

    private var selectedCard: PolicyCard? = null
    private var animatorSetSmall: AnimatorSet? = null
    private var smallPolicyElevation: Float? = null

    private var cardsRevealed = false
    private var interactionEnabled = true
    private var initiated = false
    private var readyToConfirm = false
    private var secondStage = false

    private var lastConfirmButtonText = ""
    private var lastInteractionEnabled: Boolean? = true
    private var vetoBannerActive = false
    private val vetoBannerOffset by lazy { resources.displayMetrics.widthPixels - 64 * resources.displayMetrics.density }

    private val selectCardToDiscard by lazy { R.string.select_card_to_discard.asString() }
    private val discardPileRelease by lazy { R.string.discard_pile_release.asString() }
    private val discardPileDragHere by lazy { R.string.discard_pile_drag_here.asString() }
    private val discardPileConfirm by lazy { R.string.discard_pile_confirm.asString() }
    private val holdToConfirm by lazy { R.string.hold_to_confirm.asString() }
    private val holdToReveal by lazy { R.string.hold_to_reveal.asString() }
    private val releaseButton by lazy { R.string.release_button.asString() }
    private val dontSpeak by lazy { R.string.dont_speak.asString() }
    private val noChoice by lazy { R.string.no_choice.asString() }
    private val passDeviceTo by lazy { R.string.pass_device_to.asString() }
    private val ja by lazy { R.string.ja.asString() }
    private val presidentString by lazy { R.string.president.asString() }
    private val chancellorString by lazy { R.string.chancellor.asString() }
    private val vetoThisAgenda by lazy { R.string.veto_this_agenda.asString() }
    private val bigNo by lazy { R.string.big_no.asString() }
    private val useVetoQ by lazy { R.string.use_veto_q.asString() }

    var chancellor: String = "???"
    var president: String = "???"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_policy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initVetoBanner()

        setCameraDistance()
        setConfirmButton()
        setName(president)

        // Set cards to be face down
        policy_card_first?.back = true
        policy_card_second?.back = true
        policy_card_third?.back = true

        // Save elevation of policy cards
        smallPolicyElevation = policy_card_first?.policyElevation

        // Set flip animation to cards
        setAnimationSmall(1000)

        // Hide big card
        policy_card_moving?.visibility = View.INVISIBLE

        // Init card types
        policy_card_first?.type = PolicyCard.PolicyType.FASCIST
        policy_card_second?.type = PolicyCard.PolicyType.LIBERAL
        policy_card_third?.type = PolicyCard.PolicyType.FASCIST

        // Help variables to moving
        var correctionX = 0f
        var correctionY = 0f

        view.setOnTouchListener { _, event ->
            if (!cardsRevealed || !interactionEnabled || vetoBannerActive) return@setOnTouchListener true

            when (event.action) {

                // On action down
                MotionEvent.ACTION_DOWN -> {
                    initiated = true
                    readyToConfirm = false

                    // Select card
                    selectedCard = when {
                        policy_card_first?.touchInside(event) == true -> {
                            policy_card_first
                        }
                        policy_card_second?.touchInside(event) == true -> {
                            policy_card_second
                        }
                        policy_card_third?.touchInside(event) == true -> {
                            policy_card_third
                        }
                        else -> null
                    }

                    // When touched on big card
                    if (policy_card_moving?.touchInside(event) == true && selectedCard == null) {
                        item_discardPile_text?.text = discardPileDragHere
                        policy_confirmButton?.interactionEnabled = false
                        policy_confirmButton?.textView?.text = selectCardToDiscard
                        resetCard(policy_card_first)
                        resetCard(policy_card_second)
                        resetCard(policy_card_third)
                        policy_card_moving?.visibility = View.INVISIBLE
                        initiated = false
                        veto_banner?.visibility = View.GONE
                        return@setOnTouchListener true
                    }

                    if (selectedCard != null) {
                        // Reset small cards
                        resetCard(policy_card_first)
                        resetCard(policy_card_second)
                        resetCard(policy_card_third)

                        // Show big card centered on
                        showBigCardOn(selectedCard)

                        // Update visuals
                        policy_confirmButton?.interactionEnabled = false
                        policy_confirmButton?.textView?.text = selectCardToDiscard
                        veto_banner?.visibility = View.GONE

                        // Calculate correction for movement
                        policy_card_moving?.visibility = View.VISIBLE
                        correctionX = policy_card_moving.x - event.rawX
                        correctionY = policy_card_moving.y - event.rawY
                    }

                }


                // Move card
                MotionEvent.ACTION_MOVE -> {
                    if (selectedCard != null && initiated) {
                        policy_card_moving?.x = event.rawX + correctionX
                        policy_card_moving?.y = event.rawY + correctionY

                        // Style discard pile
                        if (policy_discard_pile.touchInside(event)) {
                            item_discardPile_text?.text = discardPileRelease
                            idem_discardPile_overlay?.alpha = 0.2F
                        } else {
                            item_discardPile_text?.text = discardPileDragHere
                            idem_discardPile_overlay?.alpha = 0F
                        }
                    }
                }


                // On action up
                MotionEvent.ACTION_UP -> {
                    if (selectedCard != null) {

                        // On successful drag
                        if (policy_discard_pile.touchInside(event)) {
                            item_discardPile_text?.text = discardPileConfirm
                            idem_discardPile_overlay?.alpha = 0F

                            val scale = 0.88F
                            policy_card_moving.animate()
                                    .scaleX(scale).scaleY(scale)
                                    .x(policy_discard_pile.x + policy_discard_pile.width / 2 - policy_card_moving.width / 2)
                                    .y(policy_discard_pile.y + policy_discard_pile.height / 2 - policy_card_moving.height / 2 - 10 * resources.displayMetrics.density)
                                    .setDuration(500L)
                                    .setListener(object : Animator.AnimatorListener {
                                        override fun onAnimationStart(animation: Animator?) {
                                            interactionEnabled = false
                                        }

                                        override fun onAnimationEnd(animation: Animator?) {
                                            interactionEnabled = true
                                            readyToConfirm = true
                                            policy_confirmButton?.interactionEnabled = true
                                            policy_confirmButton?.textView?.text = holdToConfirm

                                            checkVetoButton()
                                        }

                                        override fun onAnimationRepeat(animation: Animator?) {}
                                        override fun onAnimationCancel(animation: Animator?) {}
                                    })
                                    .start()
                        } else if (initiated) {
                            resetCard(selectedCard)
                            policy_card_moving?.visibility = View.GONE
                        }
                        initiated = false
                    }
                }
            }

            true
        }
    }

    private fun checkVetoButton() {
        if (secondStage && GameState.isVetoAllowed()) {
            veto_banner?.visibility = View.VISIBLE
        } else {
            veto_banner?.visibility = View.GONE
        }
    }

    private fun resetState() {
        cardsRevealed = false
        interactionEnabled = true
        initiated = false
        readyToConfirm = false
        item_discardPile_text?.text = discardPileDragHere
        policy_confirmButton?.textView?.text = holdToReveal
    }

    private fun resetCard(card: PolicyCard?) {
        card?.alpha = 1f
        card?.policyElevation = smallPolicyElevation
    }

    private fun showBigCardOn(smallCard: PolicyCard?) {
        smallCard?.let {
            policy_card_moving?.visibility = View.VISIBLE
            policy_card_moving?.alpha = 1F
            policy_card_moving?.scaleX = 1F
            policy_card_moving?.scaleY = 1F
            policy_card_moving?.centerToView(it)
            policy_card_moving?.type = it.type
            it.alpha = 0.38f
            it.policyElevation = 0f
        }
    }

    private fun setCameraDistance() {
        val density = resources.displayMetrics.density
        val distance = (160 * 18.1).toInt()

        policy_card_first?.cameraDistance = distance * density
        policy_card_second?.cameraDistance = distance * density
        policy_card_second?.cameraDistance = distance * density

        policy_card_moving?.cameraDistance = distance * density
    }

    @Suppress("SameParameterValue")
    private fun setAnimationSmall(duration: Long) {
        policy_card_first?.setupAnimation(duration)
        policy_card_second?.setupAnimation(duration)
        policy_card_third?.setupAnimation(duration)
        animatorSetSmall = AnimatorSet()
        animatorSetSmall?.playTogether(policy_card_first?.anim1, policy_card_second?.anim1, policy_card_third?.anim1)
        animatorSetSmall?.duration = duration / 2

        policy_card_first?.onFlipped = {
            policy_card_first?.policyElevation = smallPolicyElevation
            policy_card_second?.policyElevation = smallPolicyElevation
            policy_card_third?.policyElevation = smallPolicyElevation
        }
    }

    private fun obtainPassCard(): PolicyCard.PolicyType? {
        return when {
            policy_card_first?.visibility == View.VISIBLE && policy_card_first?.alpha == 1f -> policy_card_first?.type
            policy_card_second?.visibility == View.VISIBLE && policy_card_second?.alpha == 1f -> policy_card_second?.type
            policy_card_third?.visibility == View.VISIBLE && policy_card_third?.alpha == 1f -> policy_card_third?.type
            else -> null
        }
    }

    private fun setConfirmButton() {
        var movingCardHideAnim: ViewPropertyAnimator? = null

        policy_confirmButton?.duration = 1000

        policy_confirmButton?.listener = object : ConfirmButton.ProgressListener {
            override fun onStart() {
                if ((!cardsRevealed || readyToConfirm) && !vetoBannerActive) {
                    selectedCard?.animate()?.alpha(0F)?.setDuration(200)?.start()

                    policy_card_first?.policyElevation = 0f
                    policy_card_second?.policyElevation = 0f
                    policy_card_third?.policyElevation = 0f
                    animatorSetSmall?.start()

                    if (readyToConfirm) {
                        movingCardHideAnim = policy_card_moving?.animate()?.alpha(0F)?.setDuration(1000L)?.setListener(object : Animator.AnimatorListener {
                            override fun onAnimationRepeat(animation: Animator?) {}
                            override fun onAnimationEnd(animation: Animator?) {}
                            override fun onAnimationCancel(animation: Animator?) {}
                            override fun onAnimationStart(animation: Animator?) {}
                        })
                        movingCardHideAnim?.start()
                    }
                }

            }

            override fun onCancel() {
                if ((!cardsRevealed || readyToConfirm) && !vetoBannerActive) {
                    selectedCard?.animate()?.alpha(0.2F)?.setDuration(200)?.start()

                    policy_card_first?.anim2?.cancel()
                    policy_card_second?.anim2?.cancel()
                    policy_card_third?.anim2?.cancel()
                    animatorSetSmall?.start()
                    animatorSetSmall?.cancel()

                    if (readyToConfirm) {
                        movingCardHideAnim?.cancel()
                        policy_card_moving?.alpha = 1F
                    }
                }
            }

            override fun onConfirm() {
                policy_confirmButton?.textView?.text = releaseButton
            }

            override fun onFinish() {

                if (vetoBannerActive) {
                    passToPresidentVetoDialog()
                } else {
                    // When cards are revealed
                    if (!cardsRevealed) {
                        cardsRevealed = true
                        policy_confirmButton?.interactionEnabled = false
                        policy_confirmButton?.textView?.text = selectCardToDiscard
                    }

                    if (readyToConfirm) {
                        policy_confirmButton?.textView?.text = holdToConfirm
                        resetState()
                        selectedCard?.type?.let {
                            // Discard(it)
                            GameState.discardPolicy(it)

                            selectedCard?.visibility = View.GONE
                            selectedCard = null
                            item_discardPile_text?.text = discardPileDragHere

                            if (secondStage) {
                                var type = PolicyCard.PolicyType.LIBERAL
                                obtainPassCard()?.let { toPass ->
                                    GameState.enactPolicy(toPass)
                                    type = toPass
                                }

                                showElectNewGovernment()

                                val welcomeDialog = if (type == PolicyCard.PolicyType.LIBERAL) {
                                    GameFragment.WelcomeDialog.LIBERAL_ENACTED
                                } else {
                                    GameFragment.WelcomeDialog.FASCIST_ENACTED
                                }

                                (activity as? GameActivity)?.let { gameActivity ->
                                    gameActivity.switchToBoard(welcomeDialog)
                                    gameActivity.playersFragment.buttonBehavior = PlayersFragment.ButtonBehavior.NEW_GOVERNMENT
                                }
                            } else {
                                showPassDialog()
                                secondStage = true
                            }
                        }
                    }
                }
            }

            override fun onActionDown() {}
            override fun onActionUp() {}

        }
    }

    private fun setName(name: String) {
        val text = name.toUpperCase(Locale.ROOT) + "\n" + dontSpeak
        policy_playerInfo_textView?.text = text
    }

    private fun passToPresidentVetoDialog() {
        fragmentManager?.let {
            policy_card_first?.back = true
            policy_card_second?.back = true
            policy_card_third?.back = true

            val confirmDialog = ConfirmDialog()
            confirmDialog.afterCreated = {
                confirmDialog.detailText?.visibility = View.VISIBLE
                confirmDialog.detailText?.text = president.toUpperCase(Locale.ROOT)
                confirmDialog.noButton?.primaryText = ja
                confirmDialog.noButton?.secondaryText = noChoice
                val textTitle = "$passDeviceTo $presidentString"
                confirmDialog.title?.text = textTitle
            }
            confirmDialog.onConfirm = {
                val discardPolicy = selectedCard?.type
                val enactPolicy = obtainPassCard()
                if (enactPolicy != null && discardPolicy != null) {
                    // val confirmVetoFragment = ConfirmVetoFragment(enactPolicy, discardPolicy)
                    (activity as? GameActivity)?.let { gameActivity ->
                        showElectNewGovernment()
                        gameActivity.showVetoPresident(enactPolicy, discardPolicy)
                    }
                }

            }
            confirmDialog.isCancelable = false
            confirmDialog.show(it, "pass dialog")
        }
    }

    private fun showPassDialog() {
        fragmentManager?.let {
            val confirmDialog = ConfirmDialog()
            confirmDialog.afterCreated = {
                confirmDialog.detailText?.visibility = View.VISIBLE
                confirmDialog.detailText?.text = chancellor.toUpperCase(Locale.ROOT)
                confirmDialog.noButton?.primaryText = ja
                confirmDialog.noButton?.secondaryText = noChoice
                val textTitle = "$passDeviceTo $chancellorString"
                confirmDialog.title?.text = textTitle
                setName(chancellor)
                policy_playerRole_image?.setImageResource(R.drawable.img_chancellor)
            }
            confirmDialog.isCancelable = false
            confirmDialog.show(it, "pass dialog")
        }
    }

    private fun showElectNewGovernment() {
        policy_playerRole_image?.setImageResource(R.drawable.img_president)
        setName("???")

        veto_overlay?.alpha = 0f
        veto_overlay?.visibility = View.GONE

        policy_card_first?.visibility = View.VISIBLE
        policy_card_second?.visibility = View.VISIBLE
        policy_card_third?.visibility = View.VISIBLE

        policy_card_first?.back = true
        policy_card_second?.back = true
        policy_card_third?.back = true

        policy_card_moving?.visibility = View.GONE

        resetCard(policy_card_first)
        resetCard(policy_card_second)
        resetCard(policy_card_third)
        elect_overlay?.visibility = View.VISIBLE

        policy_confirmButton?.textView?.text = holdToReveal

        veto_banner?.x = vetoBannerOffset
        veto_banner?.visibility = View.GONE
    }

    private fun initVetoBanner() {
        veto_banner?.x = vetoBannerOffset

        veto_banner_cardView?.setOnTouchListener(object : OnSwipeTouchListener(veto_banner_cardView.context) {
            override fun onSwipeRight() {
                deactivateVetoBanner()
            }

            override fun onSwipeLeft() {
                activateVetoBanner()
            }

            override fun onTap() {
                if (vetoBannerActive) deactivateVetoBanner() else activateVetoBanner()
            }
        })


    }

    private fun activateVetoBanner() {
        if (!vetoBannerActive) {
            veto_banner?.animate()?.x(0f)?.setDuration(200L)?.setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) {
                    vetoBannerActive = true

                    veto_banner_shortText?.text = bigNo
                    veto_banner_arrow?.setImageResource(R.drawable.ic_arrow_forward_black_18dp)

                    lastConfirmButtonText = policy_confirmButton?.textView?.text?.toString() ?: ""
                    lastInteractionEnabled = policy_confirmButton?.interactionEnabled
                    policy_confirmButton?.interactionEnabled = true
                    policy_confirmButton?.textView?.text = vetoThisAgenda
                }
            })?.start()

            veto_overlay?.visibility = View.VISIBLE
            veto_overlay?.animate()?.alpha(0.73f)?.setDuration(200L)?.setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) {}
            })?.start()
        }
    }

    private fun deactivateVetoBanner() {
        if (vetoBannerActive) {
            veto_banner?.animate()?.x(vetoBannerOffset)?.setDuration(200L)?.setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) {
                    vetoBannerActive = false

                    veto_banner_shortText?.text = useVetoQ
                    veto_banner_arrow?.setImageResource(R.drawable.ic_arrow_back_black_18dp)

                    policy_confirmButton?.interactionEnabled = lastInteractionEnabled
                    policy_confirmButton?.textView?.text = lastConfirmButtonText
                }
            })?.start()

            veto_overlay?.animate()?.alpha(0f)?.setDuration(200L)?.setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) {
                    veto_overlay?.visibility = View.GONE
                }
            })?.start()
        }
    }

    fun initEnact(president: String,
                  chancellor: String,
                  card1: PolicyCard.PolicyType,
                  card2: PolicyCard.PolicyType,
                  card3: PolicyCard.PolicyType) {

        policy_card_first?.type = card1
        policy_card_second?.type = card2
        policy_card_third?.type = card3

        this.president = president
        this.chancellor = chancellor

        selectedCard = null

        vetoBannerActive = false
        veto_banner_shortText?.text = useVetoQ
        veto_banner_arrow?.setImageResource(R.drawable.ic_arrow_back_black_18dp)
        veto_banner?.visibility = View.GONE
        policy_card_moving?.visibility = View.INVISIBLE

        policy_card_first?.back = true
        policy_card_second?.back = true
        policy_card_third?.back = true

        resetCard(policy_card_first)
        resetCard(policy_card_second)
        resetCard(policy_card_third)

        policy_confirmButton?.interactionEnabled = true

        setName(president)
        veto_overlay?.alpha = 0f
        veto_overlay?.visibility = View.GONE
        elect_overlay?.visibility = View.GONE
        resetState()
        secondStage = false
    }

}