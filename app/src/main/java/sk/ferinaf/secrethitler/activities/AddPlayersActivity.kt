package sk.ferinaf.secrethitler.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_add_players.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.adapters.AddPlayersAdapter
import sk.ferinaf.secrethitler.adapters.AddPlayersTouchHelper
import sk.ferinaf.secrethitler.common.*
import sk.ferinaf.secrethitler.dialogs.AddPlayerDialog

class AddPlayersActivity : BaseActivity() {

    override var fullScreen = false

    private val playersAdapter = AddPlayersAdapter()
    private val startString by lazy { R.string.add_player_start_assignment.asString() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_players)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = R.color.backgroundOrange.asColor()
            window.customSetStatusBarColor(R.color.secretRed.asColor())
        }

        player_list_recyclerView?.adapter = playersAdapter
        player_list_recyclerView?.layoutManager = LinearLayoutManager(this)

        playersAdapter.onItemSelected = { position ->
            showNameDialog(position)
        }

        val itemTouchHelper = AddPlayersTouchHelper(playersAdapter)
        itemTouchHelper.attachToRecyclerView(player_list_recyclerView)

        playersAdapter.onAddPlayerSelected = {
            showNameDialog()
        }

        setStartButton(SavedPlayers.players.size)

        playersAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                setStartButton(playersAdapter.userList.size)
            }
        })

        start_assignment_button?.setOnClickListener {
            PlayersInfo.setNames(playersAdapter.userList)
            PlayersInfo.initPresident()

            // HACK TO SKIP ROLE DISTRIBUTION
            val intent: Intent?
            if (playersAdapter.userList.contains("developer")) {
                intent = Intent(this, GameActivity::class.java)
            } else {
                intent = Intent(this, RevealIdentityActivity::class.java)
                intent.putExtra("playerIndex", 0)
            }

            startActivity(intent)
            finish()
        }
    }

    private fun setStartButton(count: Int) {
        if (count < 5) {
            start_assignment_button?.setForceEnable(false)
            val buttonText = resources.getQuantityText(R.plurals.add_more_players, 5 - count)
            start_assignment_button?.text = buttonText.toString().format(5 - count)
        } else {
            start_assignment_button?.setForceEnable(true)
            start_assignment_button?.text = startString.format(count)
        }
    }

    private fun showNameDialog(index: Int? = null) {
        val name = if (index != null) playersAdapter.userList[index] else null
        val addPlayerDialog = AddPlayerDialog(name) { nameOut ->
            if (name != null && index != null) {
                if(nameOut != "") {
                    playersAdapter.userList[index] = nameOut
                    playersAdapter.saveCurrentList()
                    playersAdapter.notifyItemChanged(index)
                } else {
                    SavedPlayers.removePlayer(name)
                    playersAdapter.updateList()
                }
            } else {
                if(nameOut != "") {
                    SavedPlayers.addPlayer(nameOut)
                    playersAdapter.updateList()
                    player_list_recyclerView?.smoothScrollToPosition(10)
                }
            }
        }
        addPlayerDialog.isCancelable = false
        addPlayerDialog.show(supportFragmentManager, "show_add_player_dialog")
    }
}
