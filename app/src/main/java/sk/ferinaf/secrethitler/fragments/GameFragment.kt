package sk.ferinaf.secrethitler.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_game.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.activities.GameActivity
import sk.ferinaf.secrethitler.common.GameState
import sk.ferinaf.secrethitler.widgets.PolicyCard

class GameFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_game, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fakeButton?.setOnClickListener {
            val policyFragment = (activity as? GameActivity)?.policyFragment
            GameState.vetoAllowed = true
            policyFragment?.initEnact("Gasparovic", "Merkel", PolicyCard.PolicyType.LIBERAL, PolicyCard.PolicyType.FASCIST, PolicyCard.PolicyType.FASCIST)
        }
    }

}