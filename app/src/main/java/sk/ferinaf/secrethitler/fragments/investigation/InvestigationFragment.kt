package sk.ferinaf.secrethitler.fragments.investigation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.graphics.Typeface
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_investigate_reveal.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.common.PlayersInfo
import sk.ferinaf.secrethitler.common.asColor
import sk.ferinaf.secrethitler.common.asString
import sk.ferinaf.secrethitler.models.Player
import sk.ferinaf.secrethitler.models.Roles
import sk.ferinaf.secrethitler.widgets.ConfirmButton
import sk.ferinaf.secrethitler.widgets.MembershipCard
import sk.ferinaf.secrethitler.widgets.PolicyCard
import java.util.*
import kotlin.collections.ArrayList

class InvestigationFragment(val player: Player?) : Fragment() {

    private var envelopeFront: ImageView? = null
    private var envelopeBack: FrameLayout? = null
    private var membershipCard: MembershipCard? = null
    private var confirmButton: ConfirmButton? = null

    private val releaseButton by lazy { R.string.release_button.asString() }
    private val confirm by lazy { R.string.hold_to_confirm.asString() }

    private val hideAnim by lazy { getHideAnimation() }
    private val revealAnim by lazy { getRevealAnimation() }

    private var secondStage = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_investigate_reveal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBackWarning(view)

        Log.d("toInvestigate", player?.name ?: "null")

        investigate_president_name?.text = PlayersInfo.getPresident()?.name
        investigation_reveal_player?.text = player?.name

        envelopeFront = view.findViewById(R.id.investigation_envelope_front)
        envelopeBack = view.findViewById(R.id.investigation_envelope_back)
        membershipCard = view.findViewById(R.id.investigation_membershipCard)
        confirmButton = view.findViewById(R.id.investigate_reveal_confirmButton)

        membershipCard?.party = when (player?.role) {
            Roles.LIBERAL -> { PolicyCard.PolicyType.LIBERAL }
            Roles.FASCIST -> { PolicyCard.PolicyType.FASCIST }
            Roles.HITLER -> { PolicyCard.PolicyType.FASCIST }
            null -> { null }
        }

        confirmButton?.listener = object : ConfirmButton.ProgressListener {
            override fun onStart() {
                if (!secondStage) {
                    hideAnim.start()
                }
            }

            override fun onCancel() {
                if (!secondStage) {
                    hideAnim.cancel()
                    hideAnim.start()
                    hideAnim.cancel()
                }
            }

            override fun onConfirm() {
                confirmButton?.textView?.text = releaseButton
            }

            override fun onFinish() {
                if (!secondStage) {
                    revealAnim.start()
                    confirmButton?.textView?.text = confirm
                    secondStage = true
                    player?.investigated = true
                } else {
                    activity?.finish()
                }
            }
        }
    }

    private fun getAnimationsHide(view: View?, extraOffset: Float): MutableCollection<Animator> {
        val animScaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 0.8f)
        val animScaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 0.8f)
        animScaleX.duration = 500L
        animScaleY.duration = 500L

        val offset = - resources.displayMetrics.widthPixels / 2

        val animTranslate = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, offset.toFloat() - extraOffset)
        animTranslate.duration = 1000L
        animTranslate.startDelay = 500L

        return mutableListOf(animScaleX, animScaleY, animTranslate)
    }

    private fun getHideAnimation(): AnimatorSet {
        val extraOffset = (envelopeBack?.width ?: 0) / 4f
        val set1 = getAnimationsHide(envelopeFront, extraOffset)
        val set2 = getAnimationsHide(envelopeBack, extraOffset)
        val set3 = getAnimationsHide(membershipCard, extraOffset)

        val animSet = AnimatorSet()
        animSet.playTogether(set1 + set2 + set3)

        return animSet
    }

    private fun getShowCardAnim(): MutableCollection<Animator> {
        val view = membershipCard
        val animTranslate = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 0f)
        animTranslate.duration = 1000L

        val animScaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 1f)
        animScaleX.duration = 500L
        animScaleX.startDelay = 1000L

        val animScaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1f)
        animScaleY.duration = 500L
        animScaleY.startDelay = 1000L

        return mutableListOf(animScaleX, animScaleY, animTranslate)
    }

    private fun getHideEnvelopeAnim(view: View?): Animator {
        val offset = - resources.displayMetrics.widthPixels - 16f
        val animTranslate = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, offset)
        animTranslate.startDelay = 1000L
        animTranslate.duration = 500L

        return animTranslate
    }

    private fun getRevealAnimation(): AnimatorSet {
        val set1 = getShowCardAnim()
        val anim1 = getHideEnvelopeAnim(envelopeFront)
        val anim2 = getHideEnvelopeAnim(envelopeBack)

        val revealAnimSet = AnimatorSet()
        revealAnimSet.playTogether(set1 + anim1 + anim2)

        return revealAnimSet
    }

    private fun setBackWarning(view: View) {
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                val dialog = prepareDialog()
                dialog?.show()
                return@setOnKeyListener true
            }
            false
        }
    }

    private fun prepareDialog(): AlertDialog? {
        activity?.let {
            val dialog = AlertDialog.Builder(it)
                    .setMessage(R.string.back_no_way)
                    .setTitle(R.string.back_dialog_title)
                    .setPositiveButton(R.string.ok, null)
                    .create()
            dialog.setOnShowListener {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.secretGray.asColor())
            }
            return dialog
        }
        return null
    }

}