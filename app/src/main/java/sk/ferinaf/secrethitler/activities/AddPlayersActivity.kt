package sk.ferinaf.secrethitler.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_add_players.*
import androidx.recyclerview.widget.LinearLayoutManager
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.adapters.AddPlayersAdapter
import sk.ferinaf.secrethitler.adapters.AddPlayersTouchHelper
import sk.ferinaf.secrethitler.dialogs.AddPlayerDialog
import sk.ferinaf.secrethitler.dialogs.ConfirmDialog

class AddPlayersActivity : AppCompatActivity(), ConfirmDialog.ConfirmDialogListener {

    private val playersAdapter = AddPlayersAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_players)

        player_list_recyclerView.adapter = playersAdapter
        player_list_recyclerView.layoutManager = LinearLayoutManager(this)

        playersAdapter.onItemSelected = { position ->
            Log.d("selected_item", position.toString())
        }

        val itemTouchHelper = AddPlayersTouchHelper(playersAdapter)
        itemTouchHelper.attachToRecyclerView(player_list_recyclerView)

        playersAdapter.onAddPlayerSelected = {
            val addPlayerDialog = AddPlayerDialog()
            addPlayerDialog.isCancelable = true
            addPlayerDialog.show(supportFragmentManager, "show_add_player_dialog")
        }

        start_assignment_button.setOnClickListener {

        }
    }

    fun onStartPressed(v: View) {
//
//        if(names.size < 5) {
//            Toast.makeText(this, "Add at least 5 players", Toast.LENGTH_SHORT).show()
//        } else {
//            val intent = Intent(this, RevealIdentityActivity::class.java)
//            PlayersInfo.setNames(names)
//            intent.putExtra("playerIndex", 0)
//            startActivity(intent)
//            finish()
//        }
    }

    override fun onBackPressed() {
        val askToGetBack = true
        if(askToGetBack) {
            val confirmDialog = ConfirmDialog()
            confirmDialog.isCancelable = false
            confirmDialog.show(supportFragmentManager, "confirm dialog")
        } else {
            super.onBackPressed()
        }
    }

    override fun confirmDialogResult(back: Boolean) {
        if(back){
            super.onBackPressed()
        }
    }
}
