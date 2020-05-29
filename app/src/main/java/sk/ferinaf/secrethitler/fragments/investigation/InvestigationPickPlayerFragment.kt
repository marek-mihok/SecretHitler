package sk.ferinaf.secrethitler.fragments.investigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_investigate_pick.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.adapters.SelectPlayersAdapter
import sk.ferinaf.secrethitler.common.PlayersInfo
import sk.ferinaf.secrethitler.common.asString
import sk.ferinaf.secrethitler.models.GovernmentRole
import sk.ferinaf.secrethitler.models.Player
import sk.ferinaf.secrethitler.widgets.ConfirmButton

class InvestigationPickPlayerFragment : Fragment() {

    private val selectPlayerString by lazy { R.string.select_player.asString() }
    private val confirmString by lazy { R.string.hold_to_confirm.asString() }
    private val releaseButtonString by lazy { R.string.release_button.asString() }

    private var recyclerView: RecyclerView? = null
    private val mAdapter = SelectPlayersAdapter()
    private var selectedPlayer: Player? = null
    private var confirmButton: ConfirmButton? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Set recycler view
        val players = PlayersInfo.players.filter { player ->
            (!player.investigated && player.governmentRole != GovernmentRole.PRESIDENT)
        }

        mAdapter.players = ArrayList(players)

        return inflater.inflate(R.layout.fragment_investigate_pick, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        investigate_pick_investigatingPlayer_textView?.text = PlayersInfo.getPresident()?.name

        recyclerView = view.findViewById(R.id.investigate_pickPlayer_recyclerView)
        confirmButton = view.findViewById(R.id.investigate_pick_confirmButton)

        val isSelected = selectedPlayer != null
        confirmButton?.interactionEnabled = isSelected
        confirmButton?.textView?.text = if (isSelected) confirmString else selectPlayerString

        mAdapter.onPlayerSelected = {
            selectedPlayer = it
            confirmButton?.textView?.text = confirmString
            confirmButton?.interactionEnabled = true
        }

        recyclerView?.adapter = mAdapter
        recyclerView?.layoutManager = LinearLayoutManager(context)


        // Set confirm button
        confirmButton?.duration = 1000L
        confirmButton?.listener = object : ConfirmButton.ProgressListener {
            override fun onActionDown() {
                mAdapter.selectionAllowed = false
            }

            override fun onActionUp() {
                mAdapter.selectionAllowed = true
            }

            override fun onConfirm() {
                confirmButton?.textView?.text = releaseButtonString
            }

            override fun onFinish() {
                // TODO: to reveal with selectedPlayer
                //fragmentManager?.beginTransaction()?.add(InvestigationFragment(selectedPlayer), "investigation_reveal")?.commit()
                fragmentManager?.beginTransaction()?.replace(R.id.investigate_container, InvestigationFragment(selectedPlayer))?.addToBackStack(null)?.commit()
            }
        }
    }

}