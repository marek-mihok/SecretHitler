package sk.ferinaf.secrethitler.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.widgets.PolicyCard

class ConfirmVetoFragment(val policyToPass: PolicyCard.PolicyType, val policyToDiscard: PolicyCard.PolicyType) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_confirm_veto, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}