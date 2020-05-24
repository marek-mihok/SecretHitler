package sk.ferinaf.secrethitler.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_vote_results.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.activities.VotingActivity
import sk.ferinaf.secrethitler.adapters.ResultsAdapter
import sk.ferinaf.secrethitler.common.GameState
import sk.ferinaf.secrethitler.common.PlayersInfo
import sk.ferinaf.secrethitler.common.asString
import sk.ferinaf.secrethitler.models.GovernmentRole
import sk.ferinaf.secrethitler.models.Player
import sk.ferinaf.secrethitler.models.Roles
import sk.ferinaf.secrethitler.widgets.ConfirmButton

class ResultsFragment : Fragment() {

    var nominee: Player? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_vote_results, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        results_president_textView?.text = PlayersInfo.getPresident()?.name
        results_chancellor_textView?.text = nominee?.name


        // Set recycler views
        val jaAdapter = ResultsAdapter()
        jaAdapter.setType(ResultsAdapter.Voted.JA)

        results_votedJa_recyclerView?.adapter = jaAdapter
        results_votedJa_recyclerView?.layoutManager = LinearLayoutManager(context)

        val neinAdapter = ResultsAdapter()
        neinAdapter.setType(ResultsAdapter.Voted.NEIN)

        results_votedNein_recyclerView?.adapter = neinAdapter
        results_votedNein_recyclerView?.layoutManager = LinearLayoutManager(context)


        // Set Title
        val succeed = jaAdapter.itemCount > neinAdapter.itemCount
        if (succeed) {
            // On success
            vote_header?.setTitle(R.string.success.asString())
        } else {
            // On failure
            vote_header?.setTitle(R.string.failure.asString())
        }


        // Set role for nominee
        val isSpecial = (activity as? VotingActivity)?.isSpecial == true
        if (succeed) {
            if (isSpecial) {
                PlayersInfo.getPresident()?.governmentRole = null
                nominee?.governmentRole = GovernmentRole.PRESIDENT
            } else {
                PlayersInfo.getChancellor()?.governmentRole = null
                nominee?.governmentRole = GovernmentRole.CHANCELLOR

                if (nominee?.role == Roles.HITLER && GameState.enactedFascist >= 3) {
                    // TODO: FASCISTS WIN
                }
            }
        }


        // Clean votes
        PlayersInfo.players.forEach { player ->
            player.lastVote = null
        }

        (activity as? VotingActivity)?.returnQuestion = R.string.already_registered_results.asString()

        // Set button
        voteResults_confirmButton?.textView?.text = R.string.hold_to_confirm.asString()
        voteResults_confirmButton?.duration = 1000L
        voteResults_confirmButton?.listener = object : ConfirmButton.ProgressListener {
            override fun onConfirm() {
                voteResults_confirmButton?.textView?.text = R.string.release_button.asString()
            }

            override fun onFinish() {
                activity?.finish()
            }
        }
    }

}