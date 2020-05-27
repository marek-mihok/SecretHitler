package sk.ferinaf.secrethitler.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_players.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.activities.GameActivity
import sk.ferinaf.secrethitler.activities.VotingActivity
import sk.ferinaf.secrethitler.adapters.PlayersListAdapter
import sk.ferinaf.secrethitler.common.GameState
import sk.ferinaf.secrethitler.common.PlayersInfo
import sk.ferinaf.secrethitler.common.asString
import sk.ferinaf.secrethitler.dialogs.ConfirmDialog
import sk.ferinaf.secrethitler.models.GovernmentRole
import java.util.*

class PlayersFragment : Fragment() {

    enum class ButtonBehavior {
        ENACT_POLICY, NEW_GOVERNMENT, CHANCELLOR_NOMINATION, SPECIAL_ELECTION
    }

    private val nominatePresident by lazy { R.string.nominate_president.asString() }
    private val presidentString by lazy { R.string.president.asString() }
    private val passDeviceTo by lazy { R.string.pass_device_to.asString() }
    private val notYet by lazy { R.string.notYet.asString() }
    private val nominateChancellor by lazy { R.string.nominate_chancellor.asString() }
    private val enactPolicy by lazy { R.string.navigation_enact_policy.asString() }
    private val ruSure by lazy { R.string.ru_sure.asString() }
    private val electNewGovernment by lazy { R.string.elect_new_government_1.asString() }

    private var mButtonBehavior = ButtonBehavior.CHANCELLOR_NOMINATION
    var buttonBehavior
        get() = mButtonBehavior
        set(value) {
            mButtonBehavior = value
            when (value) {
                ButtonBehavior.ENACT_POLICY -> players_bottom_button?.text = enactPolicy
                ButtonBehavior.NEW_GOVERNMENT -> players_bottom_button?.text = electNewGovernment
                ButtonBehavior.CHANCELLOR_NOMINATION -> players_bottom_button?.text = nominateChancellor
                ButtonBehavior.SPECIAL_ELECTION -> players_bottom_button?.text = nominatePresident
            }
        }

    private val requestCode = 1842

    private val playersListAdapter = PlayersListAdapter()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_players, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        players_list_recycler_view?.adapter = playersListAdapter
        players_list_recycler_view?.layoutManager = LinearLayoutManager(context)

        players_bottom_button?.setOnClickListener {

            when (buttonBehavior) {
                ButtonBehavior.ENACT_POLICY -> {
                    (activity as? GameActivity)?.switchToEnact()
                }
                ButtonBehavior.NEW_GOVERNMENT -> {
                    showNewGovernmentDialog()
                }
                ButtonBehavior.CHANCELLOR_NOMINATION -> {
                    showPresidentNominationDialog()
                }
                ButtonBehavior.SPECIAL_ELECTION -> {
                    showSpecialElectionDialog()
                }
            }
        }
    }


    // After election state
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == this.requestCode) {
            if (resultCode == 1818) {
                // ON SUCCESS
                val presidentName = PlayersInfo.getPresident()?.name
                val chancellorName = PlayersInfo.getChancellor()?.name

                if (presidentName != null && chancellorName != null) {
                    // STANDARD ELECTION
                    val cards = GameState.drawCards()
                    (activity as? GameActivity)?.policyFragment?.initEnact(presidentName, chancellorName, cards[0], cards[1], cards[2])
                    buttonBehavior = ButtonBehavior.ENACT_POLICY
                    GameState.restartElectionTracker()
                } else if (presidentName != null && chancellorName == null) {
                    // SPECIAL ELECTION
                    buttonBehavior = ButtonBehavior.CHANCELLOR_NOMINATION
                }
            } else if (resultCode == 1488) {
                // FASCISTS WON BY VOTING HITLER AS CHANCELLOR
                (activity as? GameActivity)?.switchToBoard(GameFragment.WelcomeDialog.FASCISTS_WIN)
            } else if (resultCode == 2458) {
                // Vote not success
                val oldPresident = PlayersInfo.getPresident()
                val nextPresident = PlayersInfo.getNextPresident()

                oldPresident?.governmentRole = null
                oldPresident?.eligible = true
                nextPresident?.governmentRole = GovernmentRole.PRESIDENT
                nextPresident?.eligible = false

                buttonBehavior = ButtonBehavior.CHANCELLOR_NOMINATION
                GameState.advanceElectionTracker()
                (activity as? GameActivity)?.switchToBoard(GameFragment.WelcomeDialog.ELECTION_TRACKER)
            }

            // Refresh players table
            playersListAdapter.notifyDataSetChanged()
        }
    }


    // SHOW DIALOG TO CONFIRM NEW GOVERNMENT
    private fun showNewGovernmentDialog() {
        val confirmDialog = ConfirmDialog()

        confirmDialog.afterCreated = {
            confirmDialog.title?.text = ruSure
        }

        confirmDialog.onConfirm = { wantNewGovernment ->
            if (wantNewGovernment) {
                val nextPresident = PlayersInfo.getNextPresident()
                PlayersInfo.finishGovernment()
                nextPresident?.governmentRole = GovernmentRole.PRESIDENT
                nextPresident?.eligible = false

                buttonBehavior = ButtonBehavior.CHANCELLOR_NOMINATION
                playersListAdapter.notifyDataSetChanged()
            }
        }

        fragmentManager?.let {
            confirmDialog.show(it, "new_government_dialog")
        }
    }


    // Show dialog to PASS TO PRESIDENT TO ELECT CHANCELLOR
    private fun showPresidentNominationDialog() {
        val passToPresident = ConfirmDialog()

        passToPresident.afterCreated = { passToPresident.apply {
            val mTitle = "$passDeviceTo $presidentString"
            title?.text = mTitle
            detailText?.visibility = View.VISIBLE
            detailText?.text = PlayersInfo.getPresident()?.name?.toUpperCase(Locale.ROOT)
            noButton?.secondaryText = notYet
        }}

        passToPresident.onConfirm = { confirmed ->
            if (confirmed) {
                PlayersInfo.getPresident()?.eligible = false
                val voteActivityIntent = Intent(context, VotingActivity::class.java)
                startActivityForResult(voteActivityIntent, requestCode)
            }
        }

        fragmentManager?.let {
            passToPresident.show(it, "pass_to_president")
        }
    }


    // SHOW DIALOG TO PASS TO PRESIDENT TO NOMINATE NEW PRESIDENT
    private fun showSpecialElectionDialog() {
        val passToPresident = ConfirmDialog()

        passToPresident.afterCreated = { passToPresident.apply {
            val mTitle = "$passDeviceTo $presidentString"
            title?.text = mTitle
            detailText?.visibility = View.VISIBLE
            detailText?.text = PlayersInfo.getPresident()?.name?.toUpperCase(Locale.ROOT)
            noButton?.secondaryText = notYet
        }}

        passToPresident.onConfirm = { confirmed ->
            if (confirmed) {
                PlayersInfo.finishGovernment(special = true)
                val voteActivityIntent = Intent(context, VotingActivity::class.java)
                voteActivityIntent.putExtra("special", true)
                startActivityForResult(voteActivityIntent, requestCode)
            }
        }

        fragmentManager?.let {
            passToPresident.show(it, "pass_to_president")
        }
    }
}

