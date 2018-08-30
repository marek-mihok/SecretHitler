package io.g3m.secrethitler

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.view.View
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import kotlinx.android.synthetic.main.activity_choose_players.*

//sources for list and drag and drop:
//https://medium.com/@mca.himanshusharma/recyclerview-in-kotlin-f1e20b8e72dd
//https://medium.com/@mca.himanshusharma/draggable-recyclerview-in-kotlin-6c8b76af142c
//https://android.jlelse.eu/using-recyclerview-in-android-kotlin-722991e86bf3
//alternative source: https://github.com/woxblom/DragListView

class ChoosePlayersActivity : AppCompatActivity() {

    // Initializing an empty ArrayList to be filled with animals
    val players: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_players)

        hideNav()

        // Loads animals into the ArrayList
        addPlayers()

        // Creates a vertical Layout Manager
        val manager = LinearLayoutManager(this)
        rv_player_list.layoutManager = manager

        // Access the RecyclerView Adapter and load the data into it
        val itemAdapter = PlayersAdapter(players, this)
        rv_player_list.adapter = itemAdapter

//        val dividerItemDecoration = DividerItemDecoration(this , manager.orientation)
//        rv_player_list.addItemDecoration(dividerItemDecoration)

        // Setup ItemTouchHelper
        val callback = DragManageAdapter(itemAdapter, this,
                ItemTouchHelper.UP.or(ItemTouchHelper.DOWN), ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT))
        val helper = ItemTouchHelper(callback)
        helper.attachToRecyclerView( rv_player_list)

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
