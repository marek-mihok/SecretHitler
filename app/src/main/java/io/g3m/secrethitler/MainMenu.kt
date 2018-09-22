package io.g3m.secrethitler

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main_menu.*

class MainMenu : FullScreenActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
    }

    fun startButtonPressed(sender: View){
        val intent = Intent(this, ChoosePlayersActivity::class.java)
        startActivity(intent)
    }

    fun settingsButtonPressed(sender: View){
        val intent = Intent(this, RevealIdentity::class.java)

        PlayersInfo.setNames(arrayListOf(
                "Player #1",
                "Player #2",
                "Player #3",
                "Player #4",
                "Player #5",
                "Player #6",
                "Player #7"))

        intent.putExtra("playerIndex", 0)
        startActivity(intent)
    }

    fun rulesButtonPressed(sender: View){
        val editor = getSharedPreferences("Settings", 0).edit()
        editor.putBoolean("vibrations", true)
        editor.apply()

    }

    fun aboutButtonPressed(sender: View){
        val editor = getSharedPreferences("Settings", 0).edit()
        editor.putBoolean("vibrations", false)
        editor.apply()
    }

}
