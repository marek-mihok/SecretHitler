package sk.ferinaf.secrethitler.fragments

import android.os.Bundle
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
            val f = GameState.enactedFascist
            val l = GameState.enactedLiberal
            current_state_debug_test?.text = "F: $f, L: $l"
        }
    }

    fun presentWelcomeDialog(welcomeDialog: WelcomeDialog) {
        Toast.makeText(context, welcomeDialog.toString(), Toast.LENGTH_SHORT).show()

        val f = GameState.enactedFascist
        val l = GameState.enactedLiberal
        current_state_debug_test?.text = "F: $f, L: $l"
    }

}