package sk.ferinaf.secrethitler.fragments.voting

import androidx.fragment.app.Fragment
import sk.ferinaf.secrethitler.activities.VotingActivity
import sk.ferinaf.secrethitler.common.GameState
import sk.ferinaf.secrethitler.common.PlayersInfo
import sk.ferinaf.secrethitler.models.GovernmentRole
import sk.ferinaf.secrethitler.models.Player
import sk.ferinaf.secrethitler.models.Roles

interface EvaluationInterface {

    fun evaluate(fragment: Fragment, nominee: Player?, success: Boolean?) {
        fragment.apply {
            val isSpecial = (activity as? VotingActivity)?.isSpecial == true
            if (success == true) {
                activity?.setResult(1818)
                if (isSpecial) {
                    PlayersInfo.getPresident()?.governmentRole = null
                    nominee?.governmentRole = GovernmentRole.PRESIDENT
                } else {
                    PlayersInfo.getChancellor()?.governmentRole = null
                    nominee?.governmentRole = GovernmentRole.CHANCELLOR

                    if (GameState.enactedFascist >= 3 && nominee?.role == Roles.HITLER) {
                        // TODO: FASCISTS WIN
                        activity?.setResult(1488)
                    } else if (GameState.enactedFascist >= 3 ) {
                        nominee?.notHitlerReveal = true
                    }
                }
            }
        }
    }

}