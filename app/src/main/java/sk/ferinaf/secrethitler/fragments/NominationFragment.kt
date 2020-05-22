package sk.ferinaf.secrethitler.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_nomination.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.adapters.NoMeasureLinearLayoutManager
import sk.ferinaf.secrethitler.adapters.SelectPlayersAdapter
import sk.ferinaf.secrethitler.common.PlayersInfo
import sk.ferinaf.secrethitler.common.asString
import sk.ferinaf.secrethitler.widgets.ConfirmButton

class NominationFragment : Fragment() {

    private val selectPlayer by lazy { R.string.select_player.asString() }
    private val holdToConfirm by lazy { R.string.hold_to_confirm.asString() }
    private val releaseButton by lazy { R.string.release_button.asString() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_nomination, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nomination_nominator_textView?.text = PlayersInfo.getPresident()?.name

        nominations_confirmButton?.interactionEnabled = false
        nominations_confirmButton?.textView?.text = selectPlayer
        nominations_confirmButton?.duration = 1000L

        nominations_confirmButton?.listener = object : ConfirmButton.ProgressListener {
            override fun onActionDown() { }
            override fun onActionUp() { }
            override fun onStart() { }
            override fun onCancel() { }
            override fun onConfirm() {
                nominations_confirmButton?.textView?.text = releaseButton
            }
            override fun onFinish() { }
        }

        initRecyclerView()
    }

    // Init recycler view
    private fun initRecyclerView() {
        val mAdapter = SelectPlayersAdapter()
        val players = PlayersInfo.players.filter { player ->
            player.eligible
        }
        mAdapter.players = ArrayList(players)
        mAdapter.onPlayerSelected = {
            Log.d("selected", it.name)

            nominations_confirmButton?.interactionEnabled = true
            nominations_confirmButton?.textView?.text = holdToConfirm
        }
        nominations_recyclerView?.adapter = mAdapter
        val llm = NoMeasureLinearLayoutManager(context)
        nominations_recyclerView?.layoutManager = llm
    }

}
