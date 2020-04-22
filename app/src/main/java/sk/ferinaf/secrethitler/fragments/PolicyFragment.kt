package sk.ferinaf.secrethitler.fragments

import android.animation.*
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_policy.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.common.centerToView
import sk.ferinaf.secrethitler.common.touchInside
import sk.ferinaf.secrethitler.widgets.ConfirmButton
import sk.ferinaf.secrethitler.widgets.PolicyCard

class PolicyFragment : Fragment() {

    private var selectedCard = -1
    private var animatorSetSmall: AnimatorSet? = null
    private var smallPolicyElevation: Float? = null  //by lazy { 4 * resources.displayMetrics.density }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_policy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCameraDistance()
        setConfirmButton()

        smallPolicyElevation = policy_card_first?.policyElevation

        setAnimationSmall(1000)

        policy_card_moving?.visibility = View.INVISIBLE

        policy_card_first?.type = PolicyCard.PolicyType.FASCIST
        policy_card_second?.type = PolicyCard.PolicyType.LIBERAL
        policy_card_third?.type = PolicyCard.PolicyType.FASCIST

        var correctionX = 0f
        var correctionY = 0f

        view.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {

                    selectedCard = when {
                        policy_card_first?.touchInside(event) == true -> 0
                        policy_card_second?.touchInside(event) == true -> 1
                        policy_card_third?.touchInside(event) == true -> 2
                        else -> -1
                    }

                    when (selectedCard) {
                        0 -> {
                            policy_card_moving?.centerToView(policy_card_first)
                            policy_card_moving?.type = policy_card_first?.type
                            policy_card_first?.alpha = 0.38f
                            policy_card_first?.policyElevation = 0f
                        }
                        1 -> {
                            policy_card_moving?.centerToView(policy_card_second)
                            policy_card_moving?.type = policy_card_second?.type
                            policy_card_second?.alpha = 0.38f
                            policy_card_second?.policyElevation = 0f
                        }
                        2 -> {
                            policy_card_moving?.centerToView(policy_card_third)
                            policy_card_moving?.type = policy_card_third?.type
                            policy_card_third?.alpha = 0.38f
                            policy_card_third?.policyElevation = 0f
                        }
                    }

                    if (selectedCard != -1) {
                        policy_card_moving?.visibility = View.VISIBLE

                        correctionX = policy_card_moving.x - event.rawX
                        correctionY = policy_card_moving.y - event.rawY
                    }
                }

                MotionEvent.ACTION_MOVE -> {
                    if (selectedCard != -1) {
                        policy_card_moving?.x = event.rawX + correctionX
                        policy_card_moving?.y = event.rawY + correctionY
                    }
                }

                MotionEvent.ACTION_UP -> {
                    when (selectedCard) {
                        0 -> {
                            policy_card_first?.alpha = 1f
                            policy_card_first?.policyElevation = smallPolicyElevation
                        }
                        1 -> {
                            policy_card_second?.alpha = 1f
                            policy_card_second?.policyElevation = smallPolicyElevation
                        }
                        2 -> {
                            policy_card_third?.alpha = 1f
                            policy_card_third?.policyElevation = smallPolicyElevation
                        }
                    }

                    // policy_card_moving?.visibility = View.GONE
                }
            }

            true
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

    fun setAnimationSmall(duration: Long) {
        policy_card_first?.setupAnimation(duration)
        policy_card_second?.setupAnimation(duration)
        policy_card_third?.setupAnimation(duration)
        animatorSetSmall = AnimatorSet()
        animatorSetSmall?.playTogether(policy_card_first?.anim1, policy_card_second?.anim1, policy_card_third?.anim1)
        animatorSetSmall?.duration = duration / 2

        policy_card_first?.onFlipped = {
            // TODO: HANDLE FIRST FLIP
            policy_card_first?.policyElevation = smallPolicyElevation
            policy_card_second?.policyElevation = smallPolicyElevation
            policy_card_third?.policyElevation = smallPolicyElevation
            Log.d("state", "FLIPPED!!")
        }
    }


    private fun setConfirmButton() {
        policy_confirmButton?.duration = 1000
        policy_confirmButton?.listener = object : ConfirmButton.ProgressListener {
            override fun onStart() {
                policy_card_first?.policyElevation = 0f
                policy_card_second?.policyElevation = 0f
                policy_card_third?.policyElevation = 0f
                animatorSetSmall?.start()
            }

            override fun onCancel() {
                policy_card_first?.anim2?.cancel()
                policy_card_second?.anim2?.cancel()
                policy_card_third?.anim2?.cancel()
                animatorSetSmall?.start()
                animatorSetSmall?.cancel()
            }

            override fun onConfirm() {

            }

            override fun onFinish() {

            }

            override fun onActionDown() {

            }

            override fun onActionUp() {

            }

        }
    }

}