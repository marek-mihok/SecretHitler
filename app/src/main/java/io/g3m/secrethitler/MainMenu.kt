package io.g3m.secrethitler

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main_menu.*

class MainMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        hideNav()
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

    fun startButtonPressed(sender: View){
        val intent = Intent(this, ChoosePlayersActivity::class.java)
//        intent.putExtra("keyIdentifier", value) //to pass any data to next activity
        startActivity(intent)
    }

    fun settingsButtonPressed(sender: View){
        val intent = Intent(this, RevealIdentity::class.java)
        startActivity(intent)
    }

    fun rulesButtonPressed(sender: View){

    }

    fun aboutButtonPressed(sender: View){

    }

}
