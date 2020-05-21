package sk.ferinaf.secrethitler.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_confirm_veto.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.widgets.ConfirmButton
import sk.ferinaf.secrethitler.widgets.PolicyCard

class ConfirmVetoFragment(val policyToPass: PolicyCard.PolicyType, val policyToDiscard: PolicyCard.PolicyType) : Fragment() {

    private var agreed: Boolean? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_confirm_veto, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        veto_confirm_button?.textView?.text = "CHOOSE WISELY"
        veto_confirm_button?.interactionEnabled = false

        veto_confirm_yes_button?.setOnClick {
            veto_confirm_yes_button?.scaleX = 1f
            veto_confirm_yes_button?.scaleY = 1f
            veto_confirm_no_button?.scaleX = 0.8f
            veto_confirm_no_button?.scaleY = 0.8f
            agreed = true
            veto_confirm_button?.interactionEnabled = true
            veto_confirm_button?.textView?.text = "I AGREE"
        }

        veto_confirm_no_button?.setOnClick {
            veto_confirm_yes_button?.scaleX = 0.8f
            veto_confirm_yes_button?.scaleY = 0.8f
            veto_confirm_no_button?.scaleX = 1f
            veto_confirm_no_button?.scaleY = 1f
            veto_confirm_button?.interactionEnabled = true

            agreed = false
            veto_confirm_button?.textView?.text = "I DISAGREE"
        }

        veto_confirm_button?.duration = 1000L

        veto_confirm_button?.listener = object : ConfirmButton.ProgressListener {
            override fun onStart() { }
            override fun onActionDown() { }
            override fun onActionUp() { }
            override fun onCancel() { }
            override fun onConfirm() { }

            override fun onFinish() {

            }
        }
    }

}