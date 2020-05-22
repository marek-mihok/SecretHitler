package sk.ferinaf.secrethitler.activities

import android.os.Bundle
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.common.BaseActivity
import sk.ferinaf.secrethitler.common.asString
import sk.ferinaf.secrethitler.fragments.NominationFragment
import sk.ferinaf.secrethitler.fragments.VoteFragment
import sk.ferinaf.secrethitler.models.Player

class VotingActivity : BaseActivity() {

    override var fullScreen = true
    override var askToGetBack = true

    private val voteFragment =  VoteFragment()
    private val nominationFragment = NominationFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voting)

        returnQuestion = R.string.cancel_election.asString()

        supportFragmentManager.beginTransaction().add(R.id.voting_activity_container, nominationFragment).commit()
    }

    fun showVotingFragment(votingPlayer: Player?, nomineeName: Player?) {
        if (nomineeName != null && votingPlayer != null) {
            voteFragment.votingPlayer = votingPlayer
            voteFragment.nominee = nomineeName
        }
        supportFragmentManager.beginTransaction().add(R.id.voting_activity_container, voteFragment).remove(nominationFragment).commit()
    }
}