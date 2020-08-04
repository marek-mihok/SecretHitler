package sk.ferinaf.secrethitler.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.android.synthetic.main.item_election_tracker.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.activities.GameActivity
import sk.ferinaf.secrethitler.activities.InvestigateActivity
import sk.ferinaf.secrethitler.activities.PeekPolicyActivity
import sk.ferinaf.secrethitler.activities.VotingActivity
import sk.ferinaf.secrethitler.common.*
import sk.ferinaf.secrethitler.widgets.GameTile
import sk.ferinaf.secrethitler.widgets.PolicyCard

class GameFragment : Fragment() {

    enum class WelcomeDialog {
        FASCIST_ENACTED, LIBERAL_ENACTED, ELECTION_TRACKER, FASCISTS_WIN, LIBERALS_WIN
    }

    private val drawPile by lazy { R.string.draw_pile_info.asString() }
    private val discardPile by lazy { R.string.discard_pile_info.asString() }

    var liberalTiles: ArrayList<GameTile> = arrayListOf()
    var fascistTiles: ArrayList<GameTile> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    var developing = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PlayersInfo.players.forEach {
            if (it.name == "developer") {
                developing = true
            }
        }

        GameState.initDrawPile()

        liberalTiles = arrayListOf(liberalTile1, liberalTile2, liberalTile3, liberalTile4, liberalTile5)
        fascistTiles = arrayListOf(fascistTile1, fascistTile2, fascistTile3, fascistTile4, fascistTile5, fascistTile6)

        if (PlayersInfo.players.size == 5 || PlayersInfo.players.size == 6) {
            gameFragment_playersDescription_textView?.text = R.string.players_5_6.asString()
            fascistTiles[2].type = GameTile.TileEvent.PEEK_POLICY
            GameState.beforeSpecialEvent1 = false
            GameState.beforeSpecialEvent2 = false
        } else if (PlayersInfo.players.size == 7 || PlayersInfo.players.size == 8) {
            gameFragment_playersDescription_textView?.text = R.string.players_7_8.asString()
            fascistTiles[2].type = GameTile.TileEvent.SPECIAL_ELECTION
            fascistTiles[1].type = GameTile.TileEvent.INVESTIGATE
            GameState.beforeSpecialEvent1 = false
        } else if (PlayersInfo.players.size == 9 || PlayersInfo.players.size == 10) {
            gameFragment_playersDescription_textView?.text = R.string.players_9_10.asString()
            fascistTiles[2].type = GameTile.TileEvent.SPECIAL_ELECTION
            fascistTiles[1].type = GameTile.TileEvent.INVESTIGATE
            fascistTiles[0].type = GameTile.TileEvent.INVESTIGATE
        }

        fascistTiles[3].type = GameTile.TileEvent.EXECUTION
        fascistTiles[4].type = GameTile.TileEvent.E_AND_VETO

        updateData()

        currentActionButton?.visibility = View.GONE
        currentActionButton?.setOnClickListener {
            (activity as? GameActivity)?.switchToPlayers()
        }

    }

    fun updateData() {
        val f = GameState.enactedFascist
        val l = GameState.enactedLiberal
        val t = GameState.electionTracker

        highlightFascist(f)
        highlightLiberal(l)
        updateTracker(t)

        updatePiles()

        superInfo()

        when (GameState.getCurrentAction()) {
            GameState.PresidentActions.INVESTIGATE -> {
                currentActionButton?.visibility = View.VISIBLE
                currentActionButton?.setText(R.string.investigate)
            }
            GameState.PresidentActions.SPECIAL_ELECTION -> {
                currentActionButton?.visibility = View.VISIBLE
                currentActionButton?.setText(R.string.special_election)
            }
            GameState.PresidentActions.PEEK_POLICY -> {
                currentActionButton?.visibility = View.VISIBLE
                currentActionButton?.setText(R.string.peek_policy_title)
            }
            GameState.PresidentActions.EXECUTION -> {
                currentActionButton?.visibility = View.VISIBLE
                currentActionButton?.setText(R.string.execute_player)
            }
            null -> {
                currentActionButton?.visibility = View.GONE
            }
        }
    }

    private fun highlightFascist(count: Int) {
        for (i in 0 until fascistTiles.size) {
            fascistTiles[i].highlighted = i < count
        }
    }

    private fun highlightLiberal(count: Int) {
        for (i in 0 until liberalTiles.size) {
            liberalTiles[i].highlighted = i < count
        }
    }

    private fun updateTracker(count: Int) {
        if (count >= 1) {
            electionTracker_tile1?.setCardBackgroundColor(R.color.secretGray.asColor())
        } else {
            electionTracker_tile1?.setCardBackgroundColor(R.color.secretWhite.asColor())
        }

        if (count >= 2) {
            electionTracker_tile2?.setCardBackgroundColor(R.color.secretGray.asColor())
        } else {
            electionTracker_tile2?.setCardBackgroundColor(R.color.secretWhite.asColor())
        }

        if (count >= 3) {
            electionTracker_tile3?.setCardBackgroundColor(R.color.secretGray.asColor())
        } else {
            electionTracker_tile3?.setCardBackgroundColor(R.color.secretWhite.asColor())
        }
    }

    private fun updatePiles() {
        val discardPileSize = GameState.discardPile.size
        val drawPileSize = GameState.drawPile.size

        val pileNumber = context?.getSettingsFor(SettingsCategory.PILE_SIZE) ?: false

        val discardPileAdd = if (pileNumber) discardPileSize.toString() else generateLs(discardPileSize)
        val drawPileAdd = if (pileNumber) drawPileSize.toString() else generateLs(drawPileSize)

        gameBoard_drawPile_textView?.text = drawPile.format(drawPileAdd)
        gameBoard_discardPile_textView?.text = discardPile.format(discardPileAdd)
    }

    private fun generateLs(count: Int): String {
        var buffer = ""
        for (i in 0 until count) {
            buffer += "l"
        }
        return buffer
    }

    fun presentWelcomeDialog(welcomeDialog: WelcomeDialog) {
        updateData()

        // Log.d("state_welcome", welcomeDialog.toString())
        // Toast.makeText(context, welcomeDialog.toString(), Toast.LENGTH_LONG).show()
    }

    private fun superInfo() {
        if (developing) {
            var toShow = ""
            PlayersInfo.players.forEach {
                toShow += it.name + ": " + it.role.toString() + "\n"
            }
            Toast.makeText(context, toShow, Toast.LENGTH_LONG).show()
        }
    }

}