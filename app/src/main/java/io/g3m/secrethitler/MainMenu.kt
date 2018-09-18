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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
    }

    fun startButtonPressed(sender: View){
        val intent = Intent(this, ChoosePlayersActivity::class.java)
        startActivity(intent)
    }

    fun settingsButtonPressed(sender: View){
        val intent = Intent(this, RevealIdentity::class.java)
        val b = Bundle()
        b.putIntegerArrayList("player_roles", arrayListOf(0,1,2))
        b.putStringArrayList("player_names", arrayListOf("Michal", "Marek", "Peto"))
        intent.putExtras(b)
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
