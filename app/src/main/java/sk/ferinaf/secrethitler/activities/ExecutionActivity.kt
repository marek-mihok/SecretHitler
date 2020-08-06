package sk.ferinaf.secrethitler.activities

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_execution.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.adapters.SelectPlayersAdapter
import sk.ferinaf.secrethitler.common.BaseActivity
import sk.ferinaf.secrethitler.common.GameState
import sk.ferinaf.secrethitler.common.PlayersInfo
import sk.ferinaf.secrethitler.common.asString
import sk.ferinaf.secrethitler.models.GovernmentRole
import sk.ferinaf.secrethitler.models.Player
import sk.ferinaf.secrethitler.models.Roles
import sk.ferinaf.secrethitler.widgets.ConfirmButton

class ExecutionActivity : BaseActivity() {

    override var fullScreen = true

    private val formallyExecute by lazy { R.string.i_formally_execute.asString() }
    private val releaseButton by lazy { R.string.release_button.asString() }
    private val selectPlayer by lazy { R.string.select_player.asString() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_execution)

        execution_confirmButton?.duration = 1000L
        execution_confirmButton?.interactionEnabled = false
        execution_confirmButton?.textView?.text = selectPlayer

        setName("???")

        execution_president_textView?.text = PlayersInfo.getPresident()?.name

        var selectedPlayer: Player? = null

        // SET RECYCLER
        val adapter = SelectPlayersAdapter()
        val players = PlayersInfo.players.filter { player ->
            player.alive && player.governmentRole != GovernmentRole.PRESIDENT
        }
        adapter.players = ArrayList(players)

        adapter.onPlayerSelected = { player ->
            setName(player.name)
            execution_confirmButton?.interactionEnabled = true
            selectedPlayer = player
        }

        execution_recyclerView?.adapter = adapter
        execution_recyclerView?.layoutManager = LinearLayoutManager(this)


        // SET CONFIRM BUTTON
        execution_confirmButton?.listener = object : ConfirmButton.ProgressListener {
            override fun onActionDown() {
                adapter.selectionAllowed = false
            }

            override fun onActionUp() {
                adapter.selectionAllowed = true
            }

            override fun onConfirm() {
                execution_confirmButton?.textView?.text = releaseButton
            }

            override fun onFinish() {
                selectedPlayer?.alive = false
                if (selectedPlayer?.role == Roles.HITLER) {
                    // Liberals win
                    setResult(443)
                    finish()
                } else {
                    // TODO: Continue
                    if (GameState.beforeSpecialEvent4) {
                        GameState.beforeSpecialEvent4 = false
                    } else if (!GameState.beforeSpecialEvent4 && GameState.beforeSpecialEvent5) {
                        GameState.beforeSpecialEvent5 = false
                    }
                    finish()
                }
            }
        }
    }

    private fun setName(name: String?) {
        execution_description?.text = formallyExecute.format(name)
    }

}