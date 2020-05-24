package sk.ferinaf.secrethitler.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_game.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.activities.GameActivity
import sk.ferinaf.secrethitler.common.GameState
import sk.ferinaf.secrethitler.widgets.PolicyCard

class GameFragment : Fragment() {

    enum class WelcomeDialog {
        FASCIST_ENACTED, LIBERAL_ENACTED, ELECTION_TRACKER, FASCISTS_WIN, LIBERALS_WIN
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_game, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        current_state_debug_button?.setOnClickListener {
            updateData()
        }

        GameState.onLiberalEnacted = {
            updateData()
            Log.d("state", "fascist applied")
        }

        GameState.onFascistEnacted = {
            // Powers is not ignored
            updateData()
            Log.d("state", "fascist applied")
        }

        GameState.onVetoApplied = {
            updateData()
            Log.d("state", "veto applied")
        }

        GameState.onElectionTrackerAdvance = {
            updateData()
            Log.d("state", "tracer advance")
        }

        GameState.onElectionTrackerRestart = {
            updateData()
            Log.d("state", "tracer restart")
        }

        GameState.onElectionTrackerFail = {
            // Any powers granted by policy is ignored now
            updateData()
            Log.d("state", "enacted: $it")
        }
    }

    fun updateData() {
        val f = GameState.enactedFascist
        val l = GameState.enactedLiberal
        val v = GameState.electionTracker
        current_state_debug_test?.text = "F: $f, L: $l, tracker: $v"
    }

    fun presentWelcomeDialog(welcomeDialog: WelcomeDialog) {
        updateData()

        Log.d("state_welcome", welcomeDialog.toString())
    }

}