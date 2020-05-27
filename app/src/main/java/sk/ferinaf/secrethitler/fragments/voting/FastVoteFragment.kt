package sk.ferinaf.secrethitler.fragments.voting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_fast_vote.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.activities.VotingActivity
import sk.ferinaf.secrethitler.common.asString
import sk.ferinaf.secrethitler.models.Player
import sk.ferinaf.secrethitler.widgets.ConfirmButton

class FastVoteFragment : AbstractVoteFragment(), EvaluationInterface {

    private val setResult by lazy { R.string.set_result.asString() }
    private val releaseButton by lazy { R.string.release_button.asString() }
    private val holdToConfirm by lazy { R.string.hold_to_confirm.asString() }

    override var votingPlayer: Player? = null
    override var nominee: Player? = null

    private var voteAllowed = true
    private var success: Boolean? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fast_vote, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fastVote_votedPlayer_textView?.text = nominee?.name

        fastVote_confirmButton?.textView?.text = setResult
        fastVote_confirmButton?.interactionEnabled = false
        fastVote_confirmButton?.duration = 1000L

        fastVote_yesButton?.setOnClick {
            if (voteAllowed) {
                success = true

                fastVote_noButton?.scaleX = 0.8f
                fastVote_noButton?.scaleY = 0.8f

                fastVote_yesButton?.scaleX = 1f
                fastVote_yesButton?.scaleY = 1f

                fastVote_confirmButton?.textView?.text = holdToConfirm
                fastVote_confirmButton?.interactionEnabled = true
            }
        }

        fastVote_noButton?.setOnClick {
            if (voteAllowed) {
                success = false

                fastVote_noButton?.scaleX = 1f
                fastVote_noButton?.scaleY = 1f

                fastVote_yesButton?.scaleX = 0.8f
                fastVote_yesButton?.scaleY = 0.8f

                fastVote_confirmButton?.textView?.text = holdToConfirm
                fastVote_confirmButton?.interactionEnabled = true
            }
        }

        fastVote_confirmButton?.listener = object : ConfirmButton.ProgressListener {
            override fun onActionDown() {
                voteAllowed = false
            }

            override fun onActionUp() {
                voteAllowed = true
            }

            override fun onConfirm() {
                fastVote_confirmButton?.textView?.text = releaseButton
            }

            override fun onFinish() {
                evaluate(this@FastVoteFragment, nominee, success)
                (activity as? VotingActivity)?.beforePlannedFinish()
                activity?.finish()
            }
        }
    }

}

