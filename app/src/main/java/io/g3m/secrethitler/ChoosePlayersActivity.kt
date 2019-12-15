package io.g3m.secrethitler

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.view.View
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import io.g3m.secrethitler.common.FullScreenActivity
import kotlinx.android.synthetic.main.activity_choose_players.*
import kotlinx.android.synthetic.main.player_list_item.*

//sources for list and drag and drop:
//https://medium.com/@mca.himanshusharma/recyclerview-in-kotlin-f1e20b8e72dd
//https://medium.com/@mca.himanshusharma/draggable-recyclerview-in-kotlin-6c8b76af142c
//https://android.jlelse.eu/using-recyclerview-in-android-kotlin-722991e86bf3
//alternative source: https://github.com/woxblom/DragListView

//click listener: https://www.andreasjakl.com/recyclerview-kotlin-style-click-listener-android/
//https://www.andreasjakl.com/kotlin-recyclerview-for-high-performance-lists-in-android/


class ChoosePlayersActivity : FullScreenActivity() {

    // Initializing an empty ArrayList to be filled with animals
    val players: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_players)

        // Loads animals into the ArrayList
        addPlayers()

        // Creates a vertical Layout Manager
        val manager = LinearLayoutManager(this)
        rv_player_list.layoutManager = manager

        // Access the RecyclerView Adapter and load the data into it
//        val itemAdapter = PlayersAdapter(players, this)
        val itemAdapter = PlayersAdapter(players, this, { playerName : String -> playerNameClicked(playerName) })
        rv_player_list.adapter = itemAdapter

//        val dividerItemDecoration = DividerItemDecoration(this , manager.orientation)
//        rv_player_list.addItemDecoration(dividerItemDecoration)

        // Setup ItemTouchHelper
        val callback = DragManageAdapter(itemAdapter, this,
                ItemTouchHelper.UP.or(ItemTouchHelper.DOWN), ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT))
        val helper = ItemTouchHelper(callback)
        helper.attachToRecyclerView(rv_player_list)



    }

    private fun playerNameClicked(playerName : String) {

        Toast.makeText(this, "Clicked: ${playerName}", Toast.LENGTH_LONG).show()
        displayAlert(rv_player_list)
    }

    fun displayAlert(view: View){
        val alert = AlertDialog.Builder(this)
        var editTextName: EditText?=null

        // Builder
        with (alert) {
            setTitle("Edit player name")
            var name=tv_player_type.text.toString()
//            setMessage("Name:${name} \n\nEnter new name:")

            // Add any  input field here
            editTextName=EditText(context)
            editTextName!!.hint="Enter player name"
            editTextName!!.inputType = InputType.TYPE_CLASS_TEXT

            setPositiveButton("EDIT") {
                dialog, whichButton ->
                //showMessage("display the game score or anything!")
                dialog.dismiss()
                var newName=editTextName!!.text.toString()
                tv_player_type.text="${newName}"
                players[3] = "${newName}"
            }

            setNegativeButton("CANCEL") {
                dialog, whichButton ->
                //showMessage("Close the game or anything!")
                dialog.dismiss()
            }
        }

        // Dialog
        val dialog = alert.create()
        dialog.setView(editTextName)
        dialog.show()
    }

    // Adds animals to the empty animals ArrayList
    fun addPlayers() {
        players.add("Marek")
        players.add("Michal")
        players.add("Jan")
        players.add("Peter")
        players.add("Dano")
        players.add("Pitkes")
        players.add("Danko")
        players.add("Pavol")
        players.add("Pali")
        players.add("Saska")
        players.add("Matka")
        players.add("Julka")
        players.add("Nika")
        players.add("Nora")
        players.add("Jaris")
        players.add("Dezider")
        players.add("Nika")
        players.add("Miska")
        players.add("Janci")
        players.add("Chuan")
        players.add("Viktor")
    }


    override fun onResume() {
        super.onResume()
        hideNav()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        //hideNav()
    }

    // Function to hide navigation bar needs to be called at onCreate
    fun hideNav() {
        window.decorView.apply {
            systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }
}
