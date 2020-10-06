package sk.ferinaf.secrethitler.fragments.voting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_nomination.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.activities.VotingActivity
import sk.ferinaf.secrethitler.adapters.NoMeasureLinearLayoutManager
import sk.ferinaf.secrethitler.adapters.SelectPlayersAdapter
import sk.ferinaf.secrethitler.common.PlayersInfo
import sk.ferinaf.secrethitler.common.asString
import sk.ferinaf.secrethitler.models.GovernmentRole
import sk.ferinaf.secrethitler.models.Player
import sk.ferinaf.secrethitler.widgets.ConfirmButton

class NominationFragment : Fragment() {

    private val selectPlayer by lazy { R.string.select_player.asString() }
    private val holdToConfirm by lazy { R.string.hold_to_confirm.asString() }
    private val releaseButton by lazy { R.string.release_button.asString() }
    private val pickPresidentCandidate by lazy { R.string.pick_president_candidate.asString() }

    var nominator: Player? = null
    var nominee: Player? = null

    private var confirmButton: ConfirmButton? = null

    val mAdapter = SelectPlayersAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_nomination, container, false)
        nominator = PlayersInfo.getPresident()
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nomination_nominator_textView?.text = nominator?.name

        if ((activity as? VotingActivity)?.isSpecial == true) {
            nomination_description_TextView?.text = pickPresidentCandidate
            PlayersInfo.lastRegularPresident = nominator
        }

        confirmButton = view.findViewById(R.id.nominations_confirmButton)

        confirmButton?.interactionEnabled = false
        confirmButton?.textView?.text = selectPlayer
        confirmButton?.duration = 800L

        confirmButton?.listener = object : ConfirmButton.ProgressListener {
            override fun onActionDown() {
                mAdapter.selectionAllowed = false
            }

            override fun onActionUp() {
                mAdapter.selectionAllowed = true
            }

            override fun onConfirm() {
                confirmButton?.textView?.text = releaseButton
            }
            override fun onFinish() {
                (activity as? VotingActivity)?.showVotingFragment(nominator, nominee)
            }
        }

        initRecyclerView()
    }

    // Init recycler view
    private fun initRecyclerView() {
        val players = PlayersInfo.players.filter { player ->
            if ((activity as? VotingActivity)?.isSpecial == true) {
                player.governmentRole != GovernmentRole.PRESIDENT
            } else {
                player.eligible
            }
        }
        mAdapter.players = ArrayList(players)
        mAdapter.onPlayerSelected = {
            Log.d("selected", it.name)

            confirmButton?.interactionEnabled = true
            confirmButton?.textView?.text = holdToConfirm
            nominee = it
        }
        nominations_recyclerView?.adapter = mAdapter
        val llm = NoMeasureLinearLayoutManager(context)
        nominations_recyclerView?.layoutManager = llm
    }

}
