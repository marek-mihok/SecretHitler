package sk.ferinaf.secrethitler.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_players.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.activities.*
import sk.ferinaf.secrethitler.adapters.PlayersListAdapter
import sk.ferinaf.secrethitler.common.GameState
import sk.ferinaf.secrethitler.common.PlayersInfo
import sk.ferinaf.secrethitler.common.asString
import sk.ferinaf.secrethitler.common.setGravity
import sk.ferinaf.secrethitler.dialogs.ConfirmDialog
import sk.ferinaf.secrethitler.models.GovernmentRole
import java.util.*

class PlayersFragment : Fragment() {

    enum class ButtonBehavior {
        ENACT_POLICY, NEW_GOVERNMENT, CHANCELLOR_NOMINATION, SPECIAL_ELECTION, PEEK_POLICY, INVESTIGATE, EXECUTION
    }

    private val nominatePresident by lazy { R.string.nominate_president.asString() }
    private val presidentString by lazy { R.string.president.asString() }
    private val passDeviceTo by lazy { R.string.pass_device_to.asString() }
    private val notYet by lazy { R.string.notYet.asString() }
    private val nominateChancellor by lazy { R.string.nominate_chancellor.asString() }
    private val enactPolicy by lazy { R.string.navigation_enact_policy.asString() }
    private val ruSure by lazy { R.string.ru_sure.asString() }
    private val electNewGovernment by lazy { R.string.elect_new_government_1.asString() }
    private val peekPolicy by lazy { R.string.peek_policy_title.asString() }
    private val investigateTitle by lazy { R.string.investigate.asString() }
    private val executePlayer by lazy { R.string.execute_player.asString() }
    private val noInteractive by lazy { R.string.noInteractiveTable.asString() }

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
                ButtonBehavior.PEEK_POLICY -> players_bottom_button?.text = peekPolicy
                ButtonBehavior.INVESTIGATE -> players_bottom_button?.text = investigateTitle
                ButtonBehavior.EXECUTION -> players_bottom_button?.text = executePlayer
            }
        }

    private val requestCode = 1842
    private val peekPolicyRequestCode = 354
    private val investigateRequestCode = 300
    private val executionRequestCode = 333

    private val playersListAdapter = PlayersListAdapter()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_players, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var toast = Toast.makeText(context, noInteractive, Toast.LENGTH_SHORT)

        players_list_recycler_view?.addOnItemTouchListener(object: RecyclerView.OnItemTouchListener {
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                if (e.action == MotionEvent.ACTION_DOWN) {
                    toast.cancel()
                    toast = Toast.makeText(context, noInteractive, Toast.LENGTH_LONG)
                    toast.setGravity(Gravity.CENTER)
                    toast.show()
                }
            }

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                return true
            }
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) { }
        })

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
                ButtonBehavior.PEEK_POLICY -> {
                    showPeekPolicyDialog()
                }
                ButtonBehavior.INVESTIGATE -> {
                    showInvestigateDialog()
                }
                ButtonBehavior.EXECUTION -> {
                    showExecutionDialog()
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
        } else if (requestCode == peekPolicyRequestCode) {
            if (resultCode == 749) {
                buttonBehavior = ButtonBehavior.NEW_GOVERNMENT
            }
        } else if (requestCode == investigateRequestCode) {
            buttonBehavior = ButtonBehavior.NEW_GOVERNMENT
        } else if (requestCode == executionRequestCode) {
            if (resultCode == 443) {
                // LIBERALS WIN
                (activity as? GameActivity)?.switchToBoard(GameFragment.WelcomeDialog.LIBERALS_WIN)
            } else {
                // CONTINUE
                buttonBehavior = ButtonBehavior.NEW_GOVERNMENT
            }

            // Refresh players table
            playersListAdapter.notifyDataSetChanged()
        }
    }


    // SHOW DIALOG
    private fun basicShowDialog(onConfirm: ()->Unit = {}) {
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
                onConfirm()
            }
        }

        fragmentManager?.let {
            passToPresident.show(it, "pass_to_president")
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
        basicShowDialog {
            PlayersInfo.getPresident()?.eligible = false
            val voteActivityIntent = Intent(context, VotingActivity::class.java)
            startActivityForResult(voteActivityIntent, requestCode)
        }
    }


    // SHOW DIALOG TO PASS TO PRESIDENT TO NOMINATE NEW PRESIDENT
    private fun showSpecialElectionDialog() {
        basicShowDialog {
            PlayersInfo.finishGovernment(special = true)
            val voteActivityIntent = Intent(context, VotingActivity::class.java)
            voteActivityIntent.putExtra("special", true)
            startActivityForResult(voteActivityIntent, requestCode)
        }
    }


    // SHOW DIALOG BEFORE PEEK POLICY
    private fun showPeekPolicyDialog() {
        basicShowDialog {
            val voteActivityIntent = Intent(context, PeekPolicyActivity::class.java)
            startActivityForResult(voteActivityIntent, peekPolicyRequestCode)
        }
    }


    // SHOW DIALOG BEFORE INVESTIGATION
    private fun showInvestigateDialog() {
        basicShowDialog {
            val voteActivityIntent = Intent(context, InvestigateActivity::class.java)
            startActivityForResult(voteActivityIntent, investigateRequestCode)
        }
    }


    private fun showExecutionDialog() {
        basicShowDialog {
            val voteActivityIntent = Intent(context, ExecutionActivity::class.java)
            startActivityForResult(voteActivityIntent, executionRequestCode)
        }
    }

 }

