package sk.ferinaf.secrethitler.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_confirm_veto.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.activities.GameActivity
import sk.ferinaf.secrethitler.common.GameState
import sk.ferinaf.secrethitler.common.PlayersInfo
import sk.ferinaf.secrethitler.common.asString
import sk.ferinaf.secrethitler.widgets.ConfirmButton
import sk.ferinaf.secrethitler.widgets.PolicyCard

class ConfirmVetoFragment(val policyToPass: PolicyCard.PolicyType, val policyToDiscard: PolicyCard.PolicyType) : Fragment() {

    private var agreed: Boolean? = null

    private val iAgree by lazy { R.string.veto_agree.asString() }
    private val iDisAgree by lazy { R.string.veto_disagree.asString() }
    private val chooseWisely by lazy { R.string.veto_choose_wisely.asString() }
    private val dearPresident by lazy { R.string.veto_confirm_1.asString() }

    private val mr by lazy { R.string.mr.asString() }
    private val mrs by lazy { R.string.mrs.asString() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_confirm_veto, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        veto_dear_president?.text = dearPresident.format(if(PlayersInfo.getPresident()?.isMale == true) mr else mrs)

        veto_confirm_button?.textView?.text = chooseWisely
        veto_confirm_button?.interactionEnabled = false

        veto_confirm_yes_button?.setOnClick {
            veto_confirm_yes_button?.scaleX = 1f
            veto_confirm_yes_button?.scaleY = 1f
            veto_confirm_no_button?.scaleX = 0.8f
            veto_confirm_no_button?.scaleY = 0.8f
            agreed = true
            veto_confirm_button?.interactionEnabled = true
            veto_confirm_button?.textView?.text = iAgree
        }

        veto_confirm_no_button?.setOnClick {
            veto_confirm_yes_button?.scaleX = 0.8f
            veto_confirm_yes_button?.scaleY = 0.8f
            veto_confirm_no_button?.scaleX = 1f
            veto_confirm_no_button?.scaleY = 1f
            veto_confirm_button?.interactionEnabled = true

            agreed = false
            veto_confirm_button?.textView?.text = iDisAgree
        }

        veto_confirm_button?.duration = 1000L

        veto_confirm_button?.listener = object : ConfirmButton.ProgressListener {
            override fun onStart() { }
            override fun onActionDown() { }
            override fun onActionUp() { }
            override fun onCancel() { }

            override fun onConfirm() {
                veto_confirm_button?.textView?.text = R.string.release_button.asString()
            }

            override fun onFinish() {
                if (agreed == true) {
                    GameState.vetoApplied()
                } else {
                    GameState.enactPolicy(policyToPass)
                    GameState.discardPolicy(policyToDiscard)
                }
                (activity as? GameActivity)?.let {
                    it.hideVetoPresident()
                }
            }
        }
    }

}