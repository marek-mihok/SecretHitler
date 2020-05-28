package sk.ferinaf.secrethitler.activities

import android.os.Bundle
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.common.*
import sk.ferinaf.secrethitler.fragments.voting.*
import sk.ferinaf.secrethitler.models.Player

class VotingActivity : BaseActivity() {

    override var fullScreen = true
    override var askToGetBack = true

    private var voteFragment: AbstractVoteFragment? = null
    private val nominationFragment = NominationFragment()
    private val resultsFragment = ResultsFragment()

    var isSpecial = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voting)

        voteFragment = if (getSettingsFor(SettingsCategory.FAST_VOTING)) {
            FastVoteFragment()
        } else {
            VoteFragment()
        }

        isSpecial = intent.getBooleanExtra("special", false)
        returnQuestion = R.string.cancel_election.asString()

        supportFragmentManager.beginTransaction().add(R.id.voting_activity_container, nominationFragment).commit()
    }

    fun showVotingFragment(votingPlayer: Player?, nominee: Player?) {
        if (nominee != null && votingPlayer != null) {
            voteFragment?.votingPlayer = votingPlayer
            voteFragment?.nominee = nominee
        }
        voteFragment?.let { supportFragmentManager.beginTransaction().add(R.id.voting_activity_container, it).remove(nominationFragment).commit() }
    }

    fun showResults(nominee: Player?) {
        nominee?.let {
            resultsFragment.nominee = it
        }
        voteFragment?.let { supportFragmentManager.beginTransaction().add(R.id.voting_activity_container, resultsFragment).remove(it).commit() }
    }

    fun beforePlannedFinish() {
        if (isSpecial) {
            GameState.beforeSpecialEvent3 = false
        }
    }
}