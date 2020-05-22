package sk.ferinaf.secrethitler.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_vote.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.common.PlayersInfo
import sk.ferinaf.secrethitler.common.asString
import sk.ferinaf.secrethitler.models.Player
import sk.ferinaf.secrethitler.widgets.ConfirmButton

class VoteFragment : Fragment() {

    private val selectCard by lazy { R.string.select_card.asString() }
    private val releaseButton by lazy { R.string.release_button.asString() }
    private val holdToConfirm by lazy { R.string.hold_to_confirm.asString() }

    private var agree: Boolean? = null
    var votingPlayer: Player? = null
    var nominee: Player? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_vote, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vote_confirmButton?.duration = 500L

        votingPlayer?.let {
            initFragment(it)
        }

        nominee?.let {
            vote_nominee?.text = it.name
        }

        vote_yes_button?.setOnClick {
            agree = true

            vote_no_button?.scaleX = 0.8f
            vote_no_button?.scaleY = 0.8f

            vote_yes_button?.scaleX = 1f
            vote_yes_button?.scaleY = 1f

            vote_confirmButton?.textView?.text = holdToConfirm
            vote_confirmButton?.interactionEnabled = true
        }

        vote_no_button?.setOnClick {
            agree = false

            vote_no_button?.scaleX = 1f
            vote_no_button?.scaleY = 1f

            vote_yes_button?.scaleX = 0.8f
            vote_yes_button?.scaleY = 0.8f

            vote_confirmButton?.textView?.text = holdToConfirm
            vote_confirmButton?.interactionEnabled = true
        }

        vote_confirmButton?.listener = object : ConfirmButton.ProgressListener {
            override fun onStart() { }
            override fun onCancel() { }
            override fun onActionDown() { }
            override fun onActionUp() { }

            override fun onConfirm() {
                vote_confirmButton?.textView?.text = releaseButton
            }

            override fun onFinish() {
                votingPlayer?.lastVote = agree
                PlayersInfo.getNextPlayer(votingPlayer)?.let {
                    if (it.lastVote == null) {
                        initFragment(it)
                    } else {
                        // TODO: show results
                    }
                }
            }
        }
    }

    fun initFragment(votingPlayer: Player) {
        this.votingPlayer = votingPlayer

        vote_voting_player_name?.text = votingPlayer.name

        vote_no_button?.scaleX = 0.8f
        vote_no_button?.scaleY = 0.8f

        vote_yes_button?.scaleX = 0.8f
        vote_yes_button?.scaleY = 0.8f

        vote_confirmButton?.textView?.text = selectCard
        vote_confirmButton?.interactionEnabled = false
    }

}