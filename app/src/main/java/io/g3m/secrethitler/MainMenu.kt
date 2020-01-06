package io.g3m.secrethitler

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import io.g3m.secrethitler.common.ConfirmDialog
import io.g3m.secrethitler.common.FullScreenActivity
import io.g3m.secrethitler.common.PlayersInfo

class MainMenu : FullScreenActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
    }

    fun startButtonPressed(sender: View){
        // val intent = Intent(this, ChoosePlayersActivity::class.java)
        val intent = Intent(this, AddPlayersActivity::class.java)
        startActivity(intent)
    }

    fun settingsButtonPressed(sender: View){
        Toast.makeText(this, "To do ...", Toast.LENGTH_SHORT).show()
//        val intent = Intent(this, RevealIdentity::class.java)
//
//        PlayersInfo.setNames(arrayListOf(
//                "Player #1",
//                "Player #2",
//                "Player #3",
//                "Player #4",
//                "Player #5"))
//
//        intent.putExtra("playerIndex", 0)
//        startActivity(intent)
    }

    fun rulesButtonPressed(sender: View){
        Toast.makeText(this, "To do ...", Toast.LENGTH_SHORT).show()
//        val editor = getSharedPreferences("Settings", 0).edit()
//        editor.putBoolean("vibrations", true)
//        editor.apply()

    }

    fun aboutButtonPressed(sender: View){
        Toast.makeText(this, "To do ...", Toast.LENGTH_SHORT).show()
//        val editor = getSharedPreferences("Settings", 0).edit()
//        editor.putBoolean("vibrations", false)
//        editor.apply()
    }

}
